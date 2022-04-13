package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
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
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBossInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableBossInfo extends PreparablePosInfo {

	@Nullable
	private final NBTTagCompound bossTag;

	public PreparableBossInfo(BlockPos pos, TileEntityBoss tileEntityBoss) {
		this(pos.getX(), pos.getY(), pos.getZ(), tileEntityBoss);
	}

	public PreparableBossInfo(int x, int y, int z, TileEntityBoss tileEntityBoss) {
		this(x, y, z, getBossTag(tileEntityBoss));
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

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		TileEntityBoss tileEntity = new TileEntityBoss();
		if (this.bossTag != null) {
			ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag(ItemSoulBottle.ENTITY_IN_TAG, this.bossTag);
			stack.setTagCompound(tag);
			tileEntity.inventory.setStackInSlot(0, stack);
		}
		return new GeneratableBlockInfo(pos, CQRBlocks.BOSS_BLOCK.getDefaultState(), tileEntity);
	}

	private Entity createEntityFromTag(World world, DungeonPlacement placement, BlockPos pos) {
		Entity entity = PreparableSpawnerInfo.createEntityFromTag(world, placement, pos, bossTag);
		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).enableBossBar();
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
			EntityLiving living = (EntityLiving) entity;

			living.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			living.enablePersistence();

			if (entity instanceof AbstractEntityCQR) {
				AbstractEntityCQR entityCQR = (AbstractEntityCQR) entity;
				entityCQR.onSpawnFromCQRSpawnerInDungeon(placement);
				entityCQR.enableBossBar();
				entityCQR.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(entityCQR.getBaseHealth() * 5.0D);
			} else {
				living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
			}

			living.setHealth(living.getMaxHealth());
			living.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("temp_boss_speed_buff", 0.35D, 2));

			// Some gear
			living.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			living.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.CURSED_BONE));

			living.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND));
			living.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND));
			living.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND));
			living.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND));
		}

		return entity;
	}

	@Nullable
	public NBTTagCompound getBossTag() {
		return this.bossTag;
	}

	public static class Factory implements IFactory<TileEntityBoss> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntityBoss> tileEntitySupplier) {
			return new PreparableBossInfo(x, y, z, tileEntitySupplier.get());
		}

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
			return new PreparableBossInfo(x, y, z, (NBTTagCompound) null);
		}

	}

}
