package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.Collection;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.ByteBufUtil;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.IEntityFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableSpawnerInfo extends PreparablePosInfo {

	private final CompoundNBT tileEntityData;

	public PreparableSpawnerInfo(CompoundNBT tileEntityData) {
		this.tileEntityData = tileEntityData;
	}

	public PreparableSpawnerInfo(Collection<Entity> entities) {
		this(getNBTTagCompoundFromEntityList(entities.toArray(new Entity[0])));
	}

	public PreparableSpawnerInfo(Entity... entities) {
		this(getNBTTagCompoundFromEntityList(entities));
	}

	private static CompoundNBT getNBTTagCompoundFromEntityList(Entity... entities) {
		TileEntitySpawner tileEntitySpawner = new TileEntitySpawner();
		for (int i = 0; i < entities.length && i < tileEntitySpawner.inventory.getContainerSize(); i++) {
			if (entities[i] != null) {
				tileEntitySpawner.inventory.setItem(i, SpawnerFactory.getSoulBottleItemStackForEntity(entities[i]));
			}
		}
		return tileEntitySpawner.save(new CompoundNBT());
	}

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		if (this.tileEntityData.getBoolean("vanillaSpawner")) {
			level.setBlockState(transformedPos, Blocks.SPAWNER.defaultBlockState(), tileEntity -> {
				if (tileEntity instanceof MobSpawnerTileEntity) {
					this.vanillaSpawnerReadFromNBT(level, transformedPos, placement, (MobSpawnerTileEntity) tileEntity);
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
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
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

	private void vanillaSpawnerReadFromNBT(ICQRLevel level, BlockPos pos, DungeonPlacement placement, MobSpawnerTileEntity tileEntity) {
		AbstractSpawner spawnerBaseLogic = tileEntity.getSpawner();
		CompoundNBT compound = new CompoundNBT();

		compound.putShort("Delay", (short) 20);
		if (this.tileEntityData.contains("MinSpawnDelay", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.putShort("MinSpawnDelay", this.tileEntityData.getShort("MinSpawnDelay"));
			compound.putShort("MaxSpawnDelay", this.tileEntityData.getShort("MaxSpawnDelay"));
			compound.putShort("SpawnCount", this.tileEntityData.getShort("SpawnCount"));
		}
		if (this.tileEntityData.contains("MaxNearbyEntities", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.putShort("MaxNearbyEntities", this.tileEntityData.getShort("MaxNearbyEntities"));
			compound.putShort("RequiredPlayerRange", this.tileEntityData.getShort("RequiredPlayerRange"));
		}
		if (this.tileEntityData.contains("SpawnRange", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.putShort("SpawnRange", this.tileEntityData.getShort("SpawnRange"));
		}
		ListNBT nbttaglist = new ListNBT();
		ListNBT items = this.tileEntityData.getCompound("inventory").getList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.size(); i++) {
			CompoundNBT itemTag = items.getCompound(i);
			CompoundNBT entityTag = itemTag.getCompound("tag").getCompound("EntityIn");
			Entity entity = createEntityFromTag(placement, pos, entityTag);

			if (entity != null) {
				CompoundNBT newEntityTag = new CompoundNBT();
				entity.save(newEntityTag);

				newEntityTag.remove("UUIDLeast");
				newEntityTag.remove("UUIDMost");
				newEntityTag.remove("Pos");

				if (nbttaglist.isEmpty()) {
					compound.put("SpawnData", newEntityTag);
				}
				nbttaglist.add(new WeightedSpawnerEntity(itemTag.getByte("Count"), newEntityTag).save());
			}
		}
		compound.put("SpawnPotentials", nbttaglist);

		spawnerBaseLogic.load(compound);
	}

	private void cqrSpawnerReadFromNBT(ICQRLevel level, BlockPos pos, DungeonPlacement placement, TileEntitySpawner tileEntity) {
		ListNBT items = this.tileEntityData.getCompound("inventory").getList("Items", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.size() && i < tileEntity.inventory.getContainerSize(); i++) {
			CompoundNBT itemTag = items.getCompound(i);
			CompoundNBT entityTag = itemTag.getCompound("tag").getCompound("EntityIn");
			Entity entity = createEntityFromTag(placement, pos, entityTag);

			if (entity != null) {
				CompoundNBT newEntityTag = IEntityFactory.save(entity);

				newEntityTag.remove("UUIDLeast");
				newEntityTag.remove("UUIDMost");
				newEntityTag.remove("Pos");

				ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE.get(), itemTag.getByte("Count"));
				CompoundNBT stackTag = new CompoundNBT();
				stackTag.put("EntityIn", newEntityTag);
				stack.setTag(stackTag);
				tileEntity.inventory.setItem(i, stack);
			}
		}
	}

	@Nullable
	public static Entity createEntityFromTag(DungeonPlacement placement, BlockPos pos, CompoundNBT entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		entityTag.remove("UUIDLeast");
		entityTag.remove("UUIDMost");
		entityTag.remove("Pos");

		String id = entityTag.getString("id");

		try {
			if (id.equals(CQRMain.MODID + ":dummy")) {
				entityTag.putString("id", placement.getInhabitant().getEntityID().toString());
			}

			Entity entity = placement.getEntityFactory().createEntity(entityTag);

			if (entity == null) {
				return null;
			}

			entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

			if (entity instanceof LivingEntity) {
				// fix attribute modifiers being applied in the first tick instead of directly when creating the entity from nbt
				for (EquipmentSlotType slot : EquipmentSlotType.values()) {
					ItemStack stack = ((LivingEntity) entity).getItemBySlot(slot);

					if (!stack.isEmpty()) {
						((LivingEntity) entity).getAttributes().addTransientAttributeModifiers(stack.getAttributeModifiers(slot));
						((LivingEntity) entity).setItemSlot(slot, stack);
					}
				}

				if (entity instanceof MobEntity) {
					((MobEntity) entity).setPersistenceRequired();

					if (entity instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
					}
				}
			}

			ListNBT passengers = entityTag.getList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (INBT passengerNBT : passengers) {
				Entity passenger = createEntityFromTag(placement, pos, (CompoundNBT) passengerNBT);
				passenger.startRiding(entity);
			}

			return entity;
		} finally {
			entityTag.putString("id", id);
		}
	}

	public CompoundNBT getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<TileEntitySpawner> {

		@Override
		public PreparablePosInfo create(World level, BlockPos pos, BlockState state, LazyOptional<TileEntitySpawner> blockEntityLazy) {
			return new PreparableSpawnerInfo(getNBTFromTileEntity(level, pos, blockEntityLazy.orElseThrow(NullPointerException::new)));
		}

		private static CompoundNBT getNBTFromTileEntity(World world, BlockPos pos, TileEntitySpawner tileEntity) {
			CompoundNBT compound = tileEntity.save(new CompoundNBT());
			compound.remove("x");
			compound.remove("y");
			compound.remove("z");
			ListNBT items = compound.getCompound("inventory").getList("Items", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < items.size(); i++) {
				CompoundNBT itemTag = items.getCompound(i);
				CompoundNBT itemTagCompound = itemTag.getCompound("tag");
				CompoundNBT entityTag = itemTagCompound.getCompound("EntityIn");
				Entity entity = createEntityForExporting(entityTag, world, pos);
				if (entity != null) {
					CompoundNBT newEntityTag = new CompoundNBT();
					entity.save(newEntityTag);
					itemTagCompound.put("EntityIn", newEntityTag);
				}
			}
			return compound;
		}

		private static Entity createEntityForExporting(CompoundNBT entityTag, World world, BlockPos pos) {
			Entity entity = EntityList.createEntityFromNBT(entityTag, world);
			if (entity != null) {
				entity.setPos(pos.getX(), pos.getY(), pos.getZ());
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onExportFromWorld();
				}
				ListNBT passengers = entityTag.getList("Passengers", Constants.NBT.TAG_COMPOUND);
				for (INBT passengerNBT : passengers) {
					Entity passenger = createEntityForExporting((CompoundNBT) passengerNBT, world, pos);
					passenger.startRiding(entity);
				}
			}
			return entity;
		}

	}

	public static class Serializer implements ISerializer<PreparableSpawnerInfo> {

		@Override
		public void write(PreparableSpawnerInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			ByteBufUtil.writeVarInt(buf, nbtList.size(), 5);
			nbtList.add(preparable.tileEntityData);
		}

		@Override
		public PreparableSpawnerInfo read(ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			CompoundNBT tileEntityData = nbtList.getCompound(ByteBufUtil.readVarInt(buf, 5));
			return new PreparableSpawnerInfo(tileEntityData);
		}

	}

}
