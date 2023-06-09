package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableBossInfo extends PreparablePosInfo {

	@Nullable
	private final CompoundTag bossTag;

	public PreparableBossInfo(TileEntityBoss tileEntityBoss) {
		this(getBossTag(tileEntityBoss));
	}

	public PreparableBossInfo(@Nullable CompoundTag bossTag) {
		this.bossTag = bossTag;
	}

	@Nullable
	private static CompoundTag getBossTag(TileEntityBoss tileEntityBoss) {
		ItemStack stack = tileEntityBoss.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseThrow(NullPointerException::new).getStackInSlot(0);
		if (!stack.hasTag()) {
			return null;
		}
		if (!stack.getTag().contains("EntityIn", Constants.NBT.TAG_COMPOUND)) {
			return null;
		}
		return stack.getTag().getCompound("EntityIn");
	}

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		Entity entity;

		if (this.bossTag != null) {
			entity = this.createEntityFromTag(placement, transformedPos);
		} else if (placement.getInhabitant().getBossID() != null) {
			entity = this.createEntityFromBossID(placement, transformedPos);
		} else {
			entity = this.createEntityFromEntityID(placement, transformedPos);
		}

		placement.getProtectedRegionBuilder().addEntity(entity);
		level.addEntity(entity);
		level.setBlockState(transformedPos, Blocks.AIR.defaultBlockState());
	}

	@Override
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.BOSS_BLOCK.get().defaultBlockState(), blockEntity -> {
			if (blockEntity instanceof TileEntityBoss && this.bossTag != null) {
				ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE.get());
				stack.addTagElement(ItemSoulBottle.ENTITY_IN_TAG, this.bossTag);
				((TileEntityBoss) blockEntity).getInventory().setItem(0, stack);
			}
		});
	}

	private Entity createEntityFromTag(DungeonPlacement placement, BlockPos pos) {
		Entity entity = PreparableSpawnerInfo.createEntityFromTag(placement, pos, bossTag);
		if (entity instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) entity).enableBossBar();
		}
		return entity;
	}

	private Entity createEntityFromBossID(DungeonPlacement placement, BlockPos pos) {
		Entity entity = placement.getEntityFactory().createEntity(placement.getInhabitant().getBossID());
		entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

		if (entity instanceof MobEntity) {
			MobEntity mobEntity = (MobEntity) entity;
			placement.getEntityFactory().finalizeSpawn(mobEntity, pos, MobSpawnType.STRUCTURE, null, null);
			mobEntity.setPersistenceRequired();

			if (entity instanceof AbstractEntityCQR) {
				AbstractEntityCQR cqrEntity = (AbstractEntityCQR) entity;
				cqrEntity.onSpawnFromCQRSpawnerInDungeon(placement);
				cqrEntity.enableBossBar();
			}
		}

		return entity;
	}

	private Entity createEntityFromEntityID(DungeonPlacement placement, BlockPos pos) {
		Entity entity = placement.getEntityFactory().createEntity(placement.getInhabitant().getEntityID());
		entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
		entity.setCustomName(new TextComponent("Temporary Boss"));

		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;

			if (entity instanceof MobEntity) {
				MobEntity mobEntity = (MobEntity) entity;
				placement.getEntityFactory().finalizeSpawn(mobEntity, pos, MobSpawnType.STRUCTURE, null, null);
				mobEntity.setPersistenceRequired();
			}

			if (entity instanceof AbstractEntityCQR) {
				AbstractEntityCQR cqrEntity = (AbstractEntityCQR) entity;
				cqrEntity.onSpawnFromCQRSpawnerInDungeon(placement);
				cqrEntity.enableBossBar();
				cqrEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(cqrEntity.getBaseHealth() * 5.0D);
			} else {
				livingEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
			}

			livingEntity.setHealth(livingEntity.getMaxHealth());
			livingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("temp_boss_speed_buff", 0.35D, Operation.MULTIPLY_TOTAL));

			// Some gear
			livingEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			livingEntity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(CQRItems.CURSED_BONE.get()));

			livingEntity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(CQRItems.HELMET_HEAVY_DIAMOND.get()));
			livingEntity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(CQRItems.CHESTPLATE_HEAVY_DIAMOND.get()));
			livingEntity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(CQRItems.LEGGINGS_HEAVY_DIAMOND.get()));
			livingEntity.setItemSlot(EquipmentSlot.FEET, new ItemStack(CQRItems.BOOTS_HEAVY_DIAMOND.get()));
		}

		return entity;
	}

	@Nullable
	public CompoundTag getBossTag() {
		return this.bossTag;
	}

	public static class Factory implements IFactory<TileEntityBoss> {

		@Override
		public PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<TileEntityBoss> blockEntityLazy) {
			return new PreparableBossInfo(blockEntityLazy.orElseThrow(NullPointerException::new));
		}

	}

	public static class Serializer implements ISerializer<PreparableBossInfo> {

		@Override
		public void write(PreparableBossInfo preparable, ByteBuf buf, BlockStatePalette palette, ListTag nbtList) {
			ByteBufUtil.writeVarInt(buf, preparable.bossTag != null ? (nbtList.size() << 1) | 1 : 0, 5);
			if (preparable.bossTag != null) {
				nbtList.add(preparable.bossTag);
			}
		}

		@Override
		public PreparableBossInfo read(ByteBuf buf, BlockStatePalette palette, ListTag nbtList) {
			int data = ByteBufUtil.readVarInt(buf, 5);
			CompoundTag bossTag = null;
			if ((data & 1) == 1) {
				bossTag = nbtList.getCompound(data >>> 1);
			}
			return new PreparableBossInfo(bossTag);
		}

	}

}
