package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

import java.util.Collection;
import java.util.function.Supplier;

public class PreparableSpawnerInfo extends PreparablePosInfo {

	private final CompoundNBT tileEntityData;

	public PreparableSpawnerInfo(BlockPos pos, CompoundNBT tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), tileEntityData);
	}

	public PreparableSpawnerInfo(int x, int y, int z, CompoundNBT tileEntityData) {
		super(x, y, z);
		this.tileEntityData = tileEntityData;
	}

	public PreparableSpawnerInfo(BlockPos pos, Collection<Entity> entities) {
		this(pos.getX(), pos.getY(), pos.getZ(), getNBTTagCompoundFromEntityList(entities.toArray(new Entity[0])));
	}

	public PreparableSpawnerInfo(int x, int y, int z, Collection<Entity> entities) {
		this(x, y, z, getNBTTagCompoundFromEntityList(entities.toArray(new Entity[0])));
	}

	public PreparableSpawnerInfo(BlockPos pos, Entity... entities) {
		this(pos.getX(), pos.getY(), pos.getZ(), getNBTTagCompoundFromEntityList(entities));
	}

	public PreparableSpawnerInfo(int x, int y, int z, Entity... entities) {
		this(x, y, z, getNBTTagCompoundFromEntityList(entities));
	}

	private static CompoundNBT getNBTTagCompoundFromEntityList(Entity... entities) {
		TileEntitySpawner tileEntitySpawner = new TileEntitySpawner();
		for (int i = 0; i < entities.length && i < tileEntitySpawner.inventory.getSlots(); i++) {
			if (entities[i] != null) {
				tileEntitySpawner.inventory.setStackInSlot(i, SpawnerFactory.getSoulBottleItemStackForEntity(entities[i]));
			}
		}
		return tileEntitySpawner.writeToNBT(new CompoundNBT());
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		BlockState state;
		TileEntity tileEntity;
		BlockPos p = pos.toImmutable();

		if (this.tileEntityData.getBoolean("vanillaSpawner")) {
			state = Blocks.MOB_SPAWNER.getDefaultState();
			tileEntity = state.getBlock().createTileEntity(world, state);

			if (tileEntity instanceof MobSpawnerTileEntity) {
				this.vanillaSpawnerReadFromNBT(world, placement, p, (MobSpawnerTileEntity) tileEntity);
			}
		} else {
			state = CQRBlocks.SPAWNER.getDefaultState();
			tileEntity = state.getBlock().createTileEntity(world, state);

			if (tileEntity instanceof TileEntitySpawner) {
				this.cqrSpawnerReadFromNBT(world, placement, p, (TileEntitySpawner) tileEntity);
			}
		}

		return new GeneratableBlockInfo(p, state, tileEntity);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		BlockState transformedState = CQRBlocks.SPAWNER.getDefaultState().withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = null;

		if (this.tileEntityData != null) {
			tileEntity = transformedState.getBlock().createTileEntity(world, transformedState);
			if (tileEntity != null) {
				this.tileEntityData.setInteger("x", pos.getX());
				this.tileEntityData.setInteger("y", pos.getY());
				this.tileEntityData.setInteger("z", pos.getZ());
				tileEntity.readFromNBT(this.tileEntityData);
				tileEntity.mirror(placement.getMirror());
				tileEntity.rotate(placement.getRotation());
				this.tileEntityData.removeTag("x");
				this.tileEntityData.removeTag("y");
				this.tileEntityData.removeTag("z");
			}
		}

		return new GeneratableBlockInfo(pos, transformedState, tileEntity);
	}

	private void vanillaSpawnerReadFromNBT(World world, DungeonPlacement placement, BlockPos pos, MobSpawnerTileEntity tileEntity) {
		AbstractSpawner spawnerBaseLogic = tileEntity.getSpawnerBaseLogic();
		CompoundNBT compound = new CompoundNBT();

		compound.setShort("Delay", (short) 20);
		if (this.tileEntityData.hasKey("MinSpawnDelay", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("MinSpawnDelay", this.tileEntityData.getShort("MinSpawnDelay"));
			compound.setShort("MaxSpawnDelay", this.tileEntityData.getShort("MaxSpawnDelay"));
			compound.setShort("SpawnCount", this.tileEntityData.getShort("SpawnCount"));
		}
		if (this.tileEntityData.hasKey("MaxNearbyEntities", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("MaxNearbyEntities", this.tileEntityData.getShort("MaxNearbyEntities"));
			compound.setShort("RequiredPlayerRange", this.tileEntityData.getShort("RequiredPlayerRange"));
		}
		if (this.tileEntityData.hasKey("SpawnRange", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("SpawnRange", this.tileEntityData.getShort("SpawnRange"));
		}
		ListNBT nbttaglist = new ListNBT();
		ListNBT items = this.tileEntityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); i++) {
			CompoundNBT itemTag = items.getCompoundTagAt(i);
			CompoundNBT entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = createEntityFromTag(world, placement, pos, entityTag);

			if (entity != null) {
				CompoundNBT newEntityTag = new CompoundNBT();
				entity.writeToNBTAtomically(newEntityTag);

				newEntityTag.removeTag("UUIDLeast");
				newEntityTag.removeTag("UUIDMost");
				newEntityTag.removeTag("Pos");

				if (nbttaglist.isEmpty()) {
					compound.setTag("SpawnData", newEntityTag);
				}
				nbttaglist.appendTag(new WeightedSpawnerEntity(itemTag.getByte("Count"), newEntityTag).toCompoundTag());
			}
		}
		compound.setTag("SpawnPotentials", nbttaglist);

		spawnerBaseLogic.readFromNBT(compound);
	}

	private void cqrSpawnerReadFromNBT(World world, DungeonPlacement placement, BlockPos pos, TileEntitySpawner tileEntity) {
		ListNBT items = this.tileEntityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount() && i < tileEntity.inventory.getSlots(); i++) {
			CompoundNBT itemTag = items.getCompoundTagAt(i);
			CompoundNBT entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = createEntityFromTag(world, placement, pos, entityTag);

			if (entity != null) {
				CompoundNBT newEntityTag = new CompoundNBT();
				entity.writeToNBTAtomically(newEntityTag);

				newEntityTag.removeTag("UUIDLeast");
				newEntityTag.removeTag("UUIDMost");
				newEntityTag.removeTag("Pos");

				ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE, itemTag.getByte("Count"));
				CompoundNBT stackTag = new CompoundNBT();
				stackTag.setTag("EntityIn", newEntityTag);
				stack.setTagCompound(stackTag);
				tileEntity.inventory.insertItem(i, stack, false);
			}
		}
	}

	@Nullable
	public static Entity createEntityFromTag(World world, DungeonPlacement placement, BlockPos pos, CompoundNBT entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		entityTag.removeTag("UUIDLeast");
		entityTag.removeTag("UUIDMost");
		entityTag.removeTag("Pos");

		String id = entityTag.getString("id");

		try {
			if (id.equals(CQRMain.MODID + ":dummy")) {
				entityTag.setString("id", placement.getInhabitant().getEntityID().toString());
			}

			Entity entity = EntityList.createEntityFromNBT(entityTag, world);

			if (entity == null) {
				return null;
			}

			entity.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

			if (entity instanceof EntityLivingBase) {
				// fix attribute modifiers being applied in the first tick instead of directly when creating the entity from nbt
				for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
					ItemStack stack = ((EntityLiving) entity).getItemStackFromSlot(slot);

					if (!stack.isEmpty()) {
						((EntityLiving) entity).getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers(slot));

						if (slot.getSlotType() == Type.HAND) {
							((EntityLivingBase) entity).handInventory.set(slot.getIndex(), stack);
						} else {
							((EntityLivingBase) entity).armorArray.set(slot.getIndex(), stack);
						}
					}
				}

				if (entity instanceof EntityLiving) {
					((EntityLiving) entity).enablePersistence();

					if (entity instanceof AbstractEntityCQR) {
						((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(placement);
					}
				}
			}

			NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (NBTBase passengerNBT : passengers) {
				Entity passenger = createEntityFromTag(world, placement, pos, (NBTTagCompound) passengerNBT);
				passenger.startRiding(entity);
			}

			return entity;
		} finally {
			entityTag.setString("id", id);
		}
	}

	public CompoundNBT getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<TileEntitySpawner> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, BlockState state, Supplier<TileEntitySpawner> tileEntitySupplier) {
			TileEntitySpawner tileEntity = tileEntitySupplier.get();
			return new PreparableSpawnerInfo(x, y, z, getNBTFromTileEntity(world, tileEntity.getPos(), tileEntity));
		}

		private static CompoundNBT getNBTFromTileEntity(World world, BlockPos pos, TileEntitySpawner tileEntity) {
			CompoundNBT compound = tileEntity.writeToNBT(new CompoundNBT());
			compound.removeTag("x");
			compound.removeTag("y");
			compound.removeTag("z");
			ListNBT items = compound.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < items.tagCount(); i++) {
				CompoundNBT itemTag = items.getCompoundTagAt(i);
				CompoundNBT itemTagCompound = itemTag.getCompoundTag("tag");
				CompoundNBT entityTag = itemTagCompound.getCompoundTag("EntityIn");
				Entity entity = createEntityForExporting(entityTag, world, pos);
				if (entity != null) {
					CompoundNBT newEntityTag = new CompoundNBT();
					entity.writeToNBTAtomically(newEntityTag);
					itemTagCompound.setTag("EntityIn", newEntityTag);
				}
			}
			return compound;
		}

		private static Entity createEntityForExporting(CompoundNBT entityTag, World world, BlockPos pos) {
			Entity entity = EntityList.createEntityFromNBT(entityTag, world);
			if (entity != null) {
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onExportFromWorld();
				}
				ListNBT passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
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
			ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
			nbtList.appendTag(preparable.tileEntityData);
		}

		@Override
		public PreparableSpawnerInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			CompoundNBT tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			return new PreparableSpawnerInfo(x, y, z, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableSpawnerInfo read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			CompoundNBT tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			return new PreparableSpawnerInfo(x, y, z, tileEntityData);
		}

	}

}
