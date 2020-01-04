package com.teamcqr.chocolatequestrepoured.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySpawner extends TileEntitySyncClient implements ITickable {

	public ItemStackHandler inventory = new ItemStackHandler(9);
	private boolean spawnedInDungeon = false;
	private String mobOverride = null;
	private int dungeonChunkX = 0;
	private int dungeonChunkZ = 0;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		if (compound.hasKey("isDungeonSpawner")) {
			this.spawnedInDungeon = compound.getBoolean("isDungeonSpawner");
		}
		if (compound.hasKey("overrideMob")) {
			this.mobOverride = compound.getString("overrideMob");
		}
		if (compound.hasKey("dungeonChunkX") && compound.hasKey("dungeonChunkZ")) {
			this.dungeonChunkX = compound.getInteger("dungeonChunkX");
			this.dungeonChunkZ = compound.getInteger("dungeonChunkZ");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("inventory", this.inventory.serializeNBT());
		if (this.spawnedInDungeon) {
			compound.setBoolean("isDungeonSpawner", true);
		}
		if (this.mobOverride != null) {
			compound.setString("overrideMob", this.mobOverride);
		}
		if (this.dungeonChunkX != 0 && this.dungeonChunkZ != 0) {
			compound.setInteger("dungeonChunkX", this.dungeonChunkX);
			compound.setInteger("dungeonChunkZ", this.dungeonChunkZ);
		}
		return compound;
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(I18n.format("tile.spawner.name"));
	}

	@Override
	public void update() {
		if (!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isNonCreativePlayerInRange(Reference.CONFIG_HELPER_INSTANCE.getSpawnerActivationDistance())) {
			this.turnBackIntoEntity();
		}
	}

	public void setInDungeon(DungeonBase dungeon, int dunChunkX, int dunChunkZ, EDungeonMobType mobOverride) {
		this.spawnedInDungeon = true;
		this.mobOverride = mobOverride.name().toUpperCase();
		// System.out.println("Dungeon mob: " + dungeon.getDungeonMob().name().toUpperCase());
		this.dungeonChunkX = dunChunkX;
		this.dungeonChunkZ = dunChunkZ;

		this.markDirty();
	}

	protected void turnBackIntoEntity() {
		if (!this.world.isRemote) {
			for (int i = 0; i < this.inventory.getSlots(); i++) {
				ItemStack stack = this.inventory.getStackInSlot(i);

				if (!stack.isEmpty() && stack.getTagCompound() != null) {
					// for(int stackIndex = 0; stackIndex < stack.getCount(); stackIndex++) {
					// DONE: Set "id" section of the nbt tag, type is resourcelocation
					NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("EntityIn");
					ResourceLocation resLocCurrent = new ResourceLocation(nbt.getString("id"));
					// System.out.println("Spawner Entity: " + resLocCurrent.toString());
					boolean isCurrentCQDummy = (resLocCurrent.getResourceDomain().equalsIgnoreCase(Reference.MODID) && resLocCurrent.getResourcePath().equalsIgnoreCase("dummy"));
					if (this.mobOverride != null && isCurrentCQDummy && EDungeonMobType.byString(this.mobOverride) != null) {
						EDungeonMobType newMob = EDungeonMobType.byString(this.mobOverride);
						// if(!newMob.equals(EDungeonMobType.DONT_REPLACE)) {
						if (newMob.equals(EDungeonMobType.DEFAULT)) {
							// DONE: Fix this not working sometimes...
							nbt.setString("id", EDungeonMobType.getMobTypeDependingOnDistance(/* this.pos.getX() */this.dungeonChunkX * 16, /* this.pos.getZ() */ this.dungeonChunkZ * 16).getEntityResourceLocation().toString());
						} else {
							nbt.setString("id", newMob.getEntityResourceLocation().toString());
						}
						// }
					}
					// System.out.println("Spawning entities...");
					for (int stackIndex = 0; stackIndex < stack.getCount(); stackIndex++) {
						this.spawnEntityFromNBT(nbt);
					}

					this.inventory.setStackInSlot(i, ItemStack.EMPTY);
					// }
				}
			}

			this.world.setBlockToAir(this.pos);
		}
	}

	protected Entity spawnEntityFromNBT(NBTTagCompound nbt) {
		{
			// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an entity
			nbt.removeTag("UUIDLeast");
			nbt.removeTag("UUIDMost");
			nbt.removeTag("Pos");
			NBTTagList passengers = nbt.getTagList("Passengers", 10);
			for (NBTBase passenger : passengers) {
				((NBTTagCompound) passenger).removeTag("UUIDLeast");
				((NBTTagCompound) passenger).removeTag("UUIDMost");
				((NBTTagCompound) passenger).removeTag("Pos");
			}
		}
		Entity entity = EntityList.createEntityFromNBT(nbt, this.world);

		if (entity != null) {
			Random rand = new Random();
			Vec3d pos = new Vec3d(this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D);
			double offset = entity.width < 0.96F ? 0.5D - entity.width * 0.5D : 0.02D;
			pos = pos.addVector(rand.nextDouble() * offset * 2.0D - offset, 0.0D, rand.nextDouble() * offset * 2.0D - offset);
			entity.setPosition(pos.x, pos.y, pos.z);

			if (entity instanceof EntityLiving) {
				if (Reference.CONFIG_HELPER_INSTANCE.areMobsFromCQSpawnersPersistent()) {
					((EntityLiving) entity).enablePersistence();
				}

				if (this.spawnedInDungeon && entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon();
				}
			}

			this.world.spawnEntity(entity);

			NBTTagList list = nbt.getTagList("Passengers", 10);
			if (!list.hasNoTags()) {
				Entity rider = this.spawnEntityFromNBT(list.getCompoundTagAt(0));
				rider.startRiding(entity);
			}
		}

		return entity;
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			for (EntityPlayer player : this.world.playerEntities) {
				if (!player.isCreative() && !player.isSpectator() && player.getDistance(this.pos.getX(), this.pos.getY(), this.pos.getZ()) < range) {
					return true;
				}
			}
		}
		return false;
	}

	public void setDungeonSpawner() {
		this.spawnedInDungeon = true;
	}
}
