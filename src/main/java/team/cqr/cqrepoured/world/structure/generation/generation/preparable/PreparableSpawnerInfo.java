package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.Collection;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

public class PreparableSpawnerInfo extends PreparablePosInfo {

	private final NBTTagCompound tileEntityData;

	public PreparableSpawnerInfo(BlockPos pos, NBTTagCompound tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), tileEntityData);
	}

	public PreparableSpawnerInfo(int x, int y, int z, NBTTagCompound tileEntityData) {
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

	private static NBTTagCompound getNBTTagCompoundFromEntityList(Entity... entities) {
		TileEntitySpawner tileEntitySpawner = new TileEntitySpawner();
		for (int i = 0; i < entities.length && i < tileEntitySpawner.inventory.getSlots(); i++) {
			if (entities[i] != null) {
				tileEntitySpawner.inventory.setStackInSlot(i, SpawnerFactory.getSoulBottleItemStackForEntity(entities[i]));
			}
		}
		return tileEntitySpawner.writeToNBT(new NBTTagCompound());
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		IBlockState state;
		TileEntity tileEntity;
		BlockPos p = pos.toImmutable();

		if (this.tileEntityData.getBoolean("vanillaSpawner")) {
			state = Blocks.MOB_SPAWNER.getDefaultState();
			tileEntity = state.getBlock().createTileEntity(world, state);

			if (tileEntity instanceof TileEntityMobSpawner) {
				this.vanillaSpawnerReadFromNBT(world, placement, p, (TileEntityMobSpawner) tileEntity);
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
		IBlockState transformedState = CQRBlocks.SPAWNER.getDefaultState().withMirror(placement.getMirror()).withRotation(placement.getRotation());
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

	private void vanillaSpawnerReadFromNBT(World world, DungeonPlacement placement, BlockPos pos, TileEntityMobSpawner tileEntity) {
		MobSpawnerBaseLogic spawnerBaseLogic = tileEntity.getSpawnerBaseLogic();
		NBTTagCompound compound = new NBTTagCompound();

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
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagList items = this.tileEntityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound itemTag = items.getCompoundTagAt(i);
			NBTTagCompound entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = createEntityFromTag(world, placement, pos, entityTag);

			if (entity != null) {
				NBTTagCompound newEntityTag = new NBTTagCompound();
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
		NBTTagList items = this.tileEntityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount() && i < tileEntity.inventory.getSlots(); i++) {
			NBTTagCompound itemTag = items.getCompoundTagAt(i);
			NBTTagCompound entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = createEntityFromTag(world, placement, pos, entityTag);

			if (entity != null) {
				NBTTagCompound newEntityTag = new NBTTagCompound();
				entity.writeToNBTAtomically(newEntityTag);

				newEntityTag.removeTag("UUIDLeast");
				newEntityTag.removeTag("UUIDMost");
				newEntityTag.removeTag("Pos");

				ItemStack stack = new ItemStack(CQRItems.SOUL_BOTTLE, itemTag.getByte("Count"));
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setTag("EntityIn", newEntityTag);
				stack.setTagCompound(stackTag);
				tileEntity.inventory.insertItem(i, stack, false);
			}
		}
	}

	@Nullable
	public static Entity createEntityFromTag(World world, DungeonPlacement placement, BlockPos pos, NBTTagCompound entityTag) {
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

	public NBTTagCompound getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<TileEntitySpawner> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntitySpawner> tileEntitySupplier) {
			TileEntitySpawner tileEntity = tileEntitySupplier.get();
			return new PreparableSpawnerInfo(x, y, z, getNBTFromTileEntity(world, tileEntity.getPos(), tileEntity));
		}

		private static NBTTagCompound getNBTFromTileEntity(World world, BlockPos pos, TileEntitySpawner tileEntity) {
			NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
			compound.removeTag("x");
			compound.removeTag("y");
			compound.removeTag("z");
			NBTTagList items = compound.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < items.tagCount(); i++) {
				NBTTagCompound itemTag = items.getCompoundTagAt(i);
				NBTTagCompound itemTagCompound = itemTag.getCompoundTag("tag");
				NBTTagCompound entityTag = itemTagCompound.getCompoundTag("EntityIn");
				Entity entity = createEntityForExporting(entityTag, world, pos);
				if (entity != null) {
					NBTTagCompound newEntityTag = new NBTTagCompound();
					entity.writeToNBTAtomically(newEntityTag);
					itemTagCompound.setTag("EntityIn", newEntityTag);
				}
			}
			return compound;
		}

		private static Entity createEntityForExporting(NBTTagCompound entityTag, World world, BlockPos pos) {
			Entity entity = EntityList.createEntityFromNBT(entityTag, world);
			if (entity != null) {
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onExportFromWorld();
				}
				NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
				for (NBTBase passengerNBT : passengers) {
					Entity passenger = createEntityForExporting((NBTTagCompound) passengerNBT, world, pos);
					passenger.startRiding(entity);
				}
			}
			return entity;
		}

	}

	public static class Serializer implements ISerializer<PreparableSpawnerInfo> {

		@Override
		public void write(PreparableSpawnerInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
			nbtList.appendTag(preparable.tileEntityData);
		}

		@Override
		public PreparableSpawnerInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			NBTTagCompound tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			return new PreparableSpawnerInfo(x, y, z, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableSpawnerInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			NBTTagCompound tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			return new PreparableSpawnerInfo(x, y, z, tileEntityData);
		}

	}

}
