package com.teamcqr.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory
				: super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		if (compound.hasKey("isDungeonSpawner")) {
			spawnedInDungeon = compound.getBoolean("isDungeonSpawner");
		}
		if(compound.hasKey("overrideMob")) {
			mobOverride = compound.getString("overrideMob");
		}
		if(compound.hasKey("dungeonChunkX") && compound.hasKey("dungeonChunkZ")) {
			dungeonChunkX = compound.getInteger("dungeonChunkX");
			dungeonChunkZ = compound.getInteger("dungeonChunkZ");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("inventory", inventory.serializeNBT());
		if (spawnedInDungeon) {
			compound.setBoolean("isDungeonSpawner", true);
		}
		if(mobOverride != null) {
			compound.setString("overrideMob", mobOverride);
		}
		if(!(dungeonChunkX == 0 && dungeonChunkZ == 0)) {
			compound.setInteger("dungeonChunkX", dungeonChunkX);
			compound.setInteger("dungeonChunkZ", dungeonChunkZ);
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
	
	public void setInDungeon(DungeonBase dungeon, int dunChunkX, int dunChunkZ) {
		this.spawnedInDungeon = true;
		this.mobOverride = dungeon.getDungeonMob().name().toUpperCase(); 
		
		this.dungeonChunkX = dunChunkX;
		this.dungeonChunkZ = dunChunkZ;
		
		this.markDirty();
	}

	protected void turnBackIntoEntity() {
		if (!this.world.isRemote) {
			for (int i = 0; i < this.inventory.getSlots(); i++) {
				ItemStack stack = this.inventory.getStackInSlot(i);

				if (!stack.isEmpty() && stack.getTagCompound() != null) {
					//for(int stackIndex = 0; stackIndex < stack.getCount(); stackIndex++) {
						//DONE: Set "id" section of the nbt tag, type is resourcelocation
						NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("EntityIn");
						ResourceLocation resLocCurrent = new ResourceLocation(nbt.getString("id"));
						boolean isCurrentCQDummy = (resLocCurrent.getResourceDomain().equalsIgnoreCase(Reference.MODID) && resLocCurrent.getResourcePath().equalsIgnoreCase("dummy"));
						if(mobOverride != null && isCurrentCQDummy && EDungeonMobType.byString(mobOverride) != null) {
							EDungeonMobType newMob = EDungeonMobType.byString(mobOverride);
							//if(!newMob.equals(EDungeonMobType.DONT_REPLACE)) {
								if(newMob.equals(EDungeonMobType.DEFAULT)) {
									nbt.setString("id", EDungeonMobType.getMobDependingOnDistance(dungeonChunkX /16, dungeonChunkZ /16).toString());
								} else {
									nbt.setString("id", newMob.getEntityResourceLocation().toString());
								}
							//}
						}
						for(int stackIndex = 0; stackIndex < stack.getCount(); stackIndex++) {
							this.spawnEntityFromNBT(nbt);
						}

						this.inventory.setStackInSlot(i, ItemStack.EMPTY);
					//}
				}
			}

			this.world.setBlockToAir(this.pos);
		}
	}

	protected void spawnEntityFromNBT(NBTTagCompound nbt) {
		Entity entity = EntityList.createEntityFromNBT(nbt, this.world);

		if (entity != null) {
			entity.setUniqueId(MathHelper.getRandomUUID());
			entity.setPosition((double) this.pos.getX() + this.world.rand.nextDouble(), (double) this.pos.getY(),
					(double) this.pos.getZ() + this.world.rand.nextDouble());

			if (entity instanceof EntityLiving) {
				if (Reference.CONFIG_HELPER_INSTANCE.areMobsFromCQSpawnersPersistent()) {
					((EntityLiving) entity).enablePersistence();
				}

				if (this.spawnedInDungeon && entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon();
				}
			}

			this.world.spawnEntity(entity);
		}
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			for (EntityPlayer player : this.world.playerEntities) {
				if (!player.isCreative() && !player.isSpectator()
						&& player.getDistance(this.pos.getX(), this.pos.getY(), this.pos.getZ()) < range) {
					return true;
				}
			}
		}
		return false;
	}

	public void setDungeonSpawner() {
		spawnedInDungeon = true;
	}
}
