package team.cqr.cqrepoured.gentest.preparable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.CapabilityItemHandler;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.GeneratableBossInfo;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.gentest.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;

public class PreparableBossInfo extends PreparablePosInfo {

	@Nullable
	private final NBTTagCompound bossTag;

	public PreparableBossInfo(BlockPos pos, TileEntityBoss tileEntityBoss) {
		this(pos, getBossTag(tileEntityBoss));
	}

	public PreparableBossInfo(BlockPos pos, @Nullable NBTTagCompound bossTag) {
		this(pos.getX(), pos.getY(), pos.getZ(), bossTag);
	}

	public PreparableBossInfo(int x, int y, int z, @Nullable NBTTagCompound bossTag) {
		super(x, y, z);
		this.bossTag = bossTag;
	}

	@Nullable
	private static NBTTagCompound getBossTag(TileEntityBoss tileEntityBoss) {
		ItemStack stack = tileEntityBoss.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
		if (!stack.hasTagCompound()) {
			return null;
		}
		if (!stack.getTagCompound().hasKey("EntityIn", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return stack.getTagCompound().getCompoundTag("EntityIn");
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity;

		if (this.bossTag != null) {
			entity = this.createEntityFromTag(world, placement, pos);
		} else if (placement.getInhabitant().getBossID() != null) {
			entity = this.createEntityFromBossID(world, placement, pos);
		} else {
			entity = this.createEntityFromEntityID(world, placement, pos);
		}

		placement.getProtectedRegionBuilder().addEntity(entity);
		return new GeneratableBossInfo(pos, entity);
	}

	private Entity createEntityFromTag(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = EntityList.createEntityFromNBT(this.bossTag, world);
		entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

		if (entity instanceof EntityLiving) {
			((EntityLiving) entity).enablePersistence();

			if (entity instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
				((AbstractEntityCQR) entity).enableBossBar();
			}
		}

		return entity;
	}

	private Entity createEntityFromBossID(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = EntityList.createEntityByIDFromName(placement.getInhabitant().getBossID(), world);
		entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

		if (entity instanceof EntityLiving) {
			((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
			((EntityLiving) entity).enablePersistence();

			if (entity instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
				((AbstractEntityCQR) entity).enableBossBar();
			}
		}

		return entity;
	}

	private Entity createEntityFromEntityID(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = EntityList.createEntityByIDFromName(placement.getInhabitant().getEntityID(), world);
		entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
		entity.setCustomNameTag("Temporary Boss");

		if (entity instanceof EntityLiving) {
			((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
			((EntityLiving) entity).enablePersistence();

			if (entity instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
				((AbstractEntityCQR) entity).setSizeVariation(1.1F);
				((AbstractEntityCQR) entity).enableBossBar();
			}

			((EntityLiving) entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
			((EntityLiving) entity).setHealth(((EntityLiving) entity).getMaxHealth());

			// Some gear
			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CQRItems.GREAT_SWORD_DIAMOND));
			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(placement.getInhabitant().getShieldReplacement()));

			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND));
			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND));
			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND));
			((EntityLiving) entity).setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND));
		}

		return entity;
	}

	@Nullable
	public NBTTagCompound getBossTag() {
		return this.bossTag;
	}

	public static class Serializer implements ISerializer<PreparableBossInfo> {

		@Override
		public void write(PreparableBossInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ByteBufUtils.writeVarInt(buf, preparable.bossTag != null ? (nbtList.tagCount() << 1) | 1 : 0, 5);
			if (preparable.bossTag != null) {
				nbtList.appendTag(preparable.bossTag);
			}
		}

		@Override
		public PreparableBossInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			NBTTagCompound bossTag = null;
			if ((data & 1) == 1) {
				bossTag = nbtList.getCompoundTagAt(data >>> 1);
			}
			return new PreparableBossInfo(x, y, z, bossTag);
		}

		@Override
		@Deprecated
		public PreparableBossInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableBossInfo(x, y, z, null);
		}

	}

}
