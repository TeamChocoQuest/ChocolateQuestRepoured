package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.generation.init.CQRBlocks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.entity.EntityFactory;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;

public class PreparableSpawnerInfo implements IBlockInfo {

	private final CompoundTag tileEntityData;

	public PreparableSpawnerInfo(CompoundTag tileEntityData) {
		this.tileEntityData = tileEntityData;
	}

	public PreparableSpawnerInfo(Collection<Entity> entities) {
		this(getNBTTagCompoundFromEntityList(entities.toArray(new Entity[0])));
	}

	public PreparableSpawnerInfo(Entity... entities) {
		this(getNBTTagCompoundFromEntityList(entities));
	}

	private static CompoundTag getNBTTagCompoundFromEntityList(Entity... entities) {
		TileEntitySpawner tileEntitySpawner = new TileEntitySpawner();
		for (int i = 0; i < entities.length && i < tileEntitySpawner.getInventory().getContainerSize(); i++) {
			if (entities[i] != null) {
				tileEntitySpawner.getInventory().setItem(i, SpawnerFactory.getSoulBottleItemStackForEntity(entities[i]));
			}
		}
		return tileEntitySpawner.save(new CompoundTag());
	}

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		if (this.tileEntityData.getBoolean("vanillaSpawner")) {
			level.setBlockState(transformedPos, Blocks.SPAWNER.defaultBlockState(), tileEntity -> {
				if (tileEntity instanceof SpawnerBlockEntity) {
					this.vanillaSpawnerReadFromNBT(level, transformedPos, placement, (SpawnerBlockEntity) tileEntity);
				}
			});
		} else {
			level.setBlockState(transformedPos, CQRBlocks.SPAWNER.get().defaultBlockState(), tileEntity -> {
				if (tileEntity instanceof TileEntitySpawner) {
					this.cqrSpawnerReadFromNBT(level, transformedPos, placement, (TileEntitySpawner) tileEntity);
				}
			});
		}
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState state = CQRBlocks.SPAWNER.get().defaultBlockState();

		level.setBlockState(transformedPos, state, tileEntity -> {
			if (tileEntity != null && this.tileEntityData != null) {
				this.tileEntityData.putInt("x", transformedPos.getX());
				this.tileEntityData.putInt("y", transformedPos.getY());
				this.tileEntityData.putInt("z", transformedPos.getZ());
				tileEntity.load(state, this.tileEntityData);
				this.tileEntityData.remove("x");
				this.tileEntityData.remove("y");
				this.tileEntityData.remove("z");
			}
		});
	}

	private void vanillaSpawnerReadFromNBT(StructureLevel level, BlockPos pos, DungeonPlacement placement, SpawnerBlockEntity tileEntity) {
		BaseSpawner spawnerBaseLogic = tileEntity.getSpawner();
		CompoundTag compound = new CompoundTag();

		compound.putShort("Delay", (short) 20);
		if (this.tileEntityData.contains("MinSpawnDelay", Tag.TAG_ANY_NUMERIC)) {
			compound.putShort("MinSpawnDelay", this.tileEntityData.getShort("MinSpawnDelay"));
			compound.putShort("MaxSpawnDelay", this.tileEntityData.getShort("MaxSpawnDelay"));
			compound.putShort("SpawnCount", this.tileEntityData.getShort("SpawnCount"));
		}
		if (this.tileEntityData.contains("MaxNearbyEntities", Tag.TAG_ANY_NUMERIC)) {
			compound.putShort("MaxNearbyEntities", this.tileEntityData.getShort("MaxNearbyEntities"));
			compound.putShort("RequiredPlayerRange", this.tileEntityData.getShort("RequiredPlayerRange"));
		}
		if (this.tileEntityData.contains("SpawnRange", Tag.TAG_ANY_NUMERIC)) {
			compound.putShort("SpawnRange", this.tileEntityData.getShort("SpawnRange"));
		}
		ListTag nbttaglist = new ListTag();
		ListTag items = this.tileEntityData.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < items.size(); i++) {
			CompoundTag itemTag = items.getCompound(i);
			CompoundTag entityTag = itemTag.getCompound("tag").getCompound("EntityIn");
			Entity entity = createEntityFromTag(placement, pos, entityTag);

			if (entity != null) {
				CompoundTag newEntityTag = new CompoundTag();
				entity.save(newEntityTag);

				newEntityTag.remove("UUIDLeast");
				newEntityTag.remove("UUIDMost");
				newEntityTag.remove("Pos");

				if (nbttaglist.isEmpty()) {
					compound.put("SpawnData", newEntityTag);
				}
				//nbttaglist.add(new WeightedSpawnerEntity(itemTag.getByte("Count"), newEntityTag).save());
				for(int j = 0; j < itemTag.getByte("Count"); j++) {
					SpawnData sd = new SpawnData(newEntityTag, Optional.empty());
					
					nbttaglist.add(SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, sd).result().orElseThrow(() -> {
						return new IllegalStateException("Invalid SpawnData");
					}));
				}
			}
		}
		compound.put("SpawnPotentials", nbttaglist);

		spawnerBaseLogic.load(compound);
	}

	private void cqrSpawnerReadFromNBT(StructureLevel level, BlockPos pos, DungeonPlacement placement, TileEntitySpawner tileEntity) {
		ListTag items = this.tileEntityData.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);

		for (int i = 0; i < items.size() && i < tileEntity.getInventory().getContainerSize(); i++) {
			CompoundTag itemTag = items.getCompound(i);
			CompoundTag entityTag = itemTag.getCompound("tag").getCompound("EntityIn");
			Entity entity = createEntityFromTag(placement, pos, entityTag);

			if (entity != null) {
				CompoundTag newEntityTag = EntityFactory.save(entity);

				newEntityTag.remove("UUIDLeast");
				newEntityTag.remove("UUIDMost");
				newEntityTag.remove("Pos");

				ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE.get(), itemTag.getByte("Count"));
				CompoundTag stackTag = new CompoundTag();
				stackTag.put("EntityIn", newEntityTag);
				stack.setTag(stackTag);
				tileEntity.getInventory().setItem(i, stack);
			}
		}
	}

	@Nullable
	public static Entity createEntityFromTag(DungeonPlacement placement, BlockPos pos, CompoundTag entityTag) {
		return createEntityFromTag(placement, pos, entityTag, false);
	}
	
	@Nullable
	public static Entity createEntityFromTag(DungeonPlacement placement, BlockPos pos, CompoundTag entityTag, boolean boss) {
		if (entityTag.isEmpty()) {
			return null;
		}

		entityTag.remove("UUIDLeast");
		entityTag.remove("UUIDMost");
		entityTag.remove("Pos");
		
		// Exchanges the entity type
		placement.inhabitant().prepareEntityNBT(entityTag, placement.random(), boss);

		Entity entity = placement.entityFactory().createEntity(entityTag);

		if (entity == null) {
			return null;
		}

		entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

		if (entity instanceof LivingEntity) {
			// fix attribute modifiers being applied in the first tick instead of directly when creating the entity from nbt
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = ((LivingEntity) entity).getItemBySlot(slot);

				if (!stack.isEmpty()) {
					((LivingEntity) entity).getAttributes().addTransientAttributeModifiers(stack.getAttributeModifiers(slot));
					((LivingEntity) entity).setItemSlot(slot, stack);
				}
			}

			if (entity instanceof Mob) {
				((Mob) entity).setPersistenceRequired();

				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
				}
			}
		}

		ListTag passengers = entityTag.getList("Passengers", Tag.TAG_COMPOUND);
		for (Tag passengerNBT : passengers) {
			Entity passenger = createEntityFromTag(placement, pos, (CompoundTag) passengerNBT);
			passenger.startRiding(entity);
		}

		return entity;
	}

	public CompoundTag getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IBlockInfoFactory<TileEntitySpawner> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<TileEntitySpawner> blockEntitySupplier) {
			return new PreparableSpawnerInfo(getNBTFromTileEntity(level, pos, blockEntitySupplier.orElseThrow(NullPointerException::new)));
		}

		private static CompoundTag getNBTFromTileEntity(Level world, BlockPos pos, TileEntitySpawner tileEntity) {
			CompoundTag compound = tileEntity.saveWithFullMetadata();
			compound.remove("x");
			compound.remove("y");
			compound.remove("z");
			ListTag items = compound.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);
			for (int i = 0; i < items.size(); i++) {
				CompoundTag itemTag = items.getCompound(i);
				CompoundTag itemTagCompound = itemTag.getCompound("tag");
				CompoundTag entityTag = itemTagCompound.getCompound("EntityIn");
				Entity entity = EntityType.loadEntityRecursive(entityTag, world, e -> {
					e.setPos(pos.getCenter());
					return e;
				});
				if (entity != null) {
					CompoundTag newEntityTag = new CompoundTag();
					entity.save(newEntityTag);
					itemTagCompound.put("EntityIn", newEntityTag);
				}
			}
			return compound;
		}

		private static Optional<Entity> createEntityForExporting(CompoundTag entityTag, Level world, BlockPos pos) {
			Optional<Entity> entity = EntityType.create(entityTag, world);
			entity.ifPresent(e -> {
				e.setPos(pos.getX(), pos.getY(), pos.getZ());
				if (e instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) e).onExportFromWorld();
				}
				ListTag passengers = entityTag.getList("Passengers", Tag.TAG_COMPOUND);
				for (Tag passengerNBT : passengers) {
					Optional<Entity> passenger = createEntityForExporting((CompoundTag) passengerNBT, world, pos);
					if (passenger.isPresent()) {
						passenger.get().startRiding(e);
					}
				}
			});
			return entity;
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableSpawnerInfo> {

		@Override
		public void write(PreparableSpawnerInfo spawnerInfo, DataOutput out, SimplePalette palette) throws IOException {
			NbtIo.write(spawnerInfo.tileEntityData, out);
		}

		@Override
		public PreparableSpawnerInfo read(DataInput in, SimplePalette palette) throws IOException {
			return new PreparableSpawnerInfo(NbtIo.read(in));
		}

	}

}
