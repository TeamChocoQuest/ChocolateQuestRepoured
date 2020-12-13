package team.cqr.cqrepoured.structuregen.structurefile;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
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
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.factories.SpawnerFactory;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.Reference;

public class BlockInfoSpawner extends AbstractBlockInfo {

	protected NBTTagCompound tileentityData;

	public BlockInfoSpawner(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockInfoSpawner(BlockPos pos) {
		super(pos);
	}

	public BlockInfoSpawner(int x, int y, int z, NBTTagCompound tileentityData) {
		super(x, y, z);
		this.tileentityData = tileentityData;
	}

	public BlockInfoSpawner(BlockPos pos, NBTTagCompound tileentityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), tileentityData);
	}

	public BlockInfoSpawner(int x, int y, int z, Entity... entities) {
		this(x, y, z, getNBTTagCompoundFromEntityList(entities));
	}

	public BlockInfoSpawner(BlockPos pos, Entity... entities) {
		this(pos.getX(), pos.getY(), pos.getZ(), getNBTTagCompoundFromEntityList(entities));
	}

	public BlockInfoSpawner(int x, int y, int z, List<Entity> entityList) {
		this(x, y, z, getNBTTagCompoundFromEntityList(entityList.toArray(new Entity[entityList.size()])));
	}

	public BlockInfoSpawner(BlockPos pos, List<Entity> entityList) {
		this(pos.getX(), pos.getY(), pos.getZ(), getNBTTagCompoundFromEntityList(entityList.toArray(new Entity[entityList.size()])));
	}

	@Override
	public void generateAt(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, BlockPos pos) {
		if (this.tileentityData == null) {
			BlockPlacingHelper.setBlockState(world, pos, Blocks.AIR.getDefaultState(), 18, false);
		} else if (this.tileentityData.getBoolean("vanillaSpawner")) {
			BlockPlacingHelper.setBlockState(world, pos, Blocks.MOB_SPAWNER.getDefaultState(), 18, false);
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityMobSpawner) {
				this.vanillaSpawnerReadFromNBT(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, (TileEntityMobSpawner) tileentity, this.tileentityData);
			} else {
				CQRMain.logger.warn("Failed to place vanilla spawner at {}", pos);
			}
		} else {
			BlockPlacingHelper.setBlockState(world, pos, CQRBlocks.SPAWNER.getDefaultState(), 18, false);
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntitySpawner) {
				this.cqrSpawnerReadFromNBT(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, (TileEntitySpawner) tileentity, this.tileentityData);
			} else {
				CQRMain.logger.warn("Failed to place cqr spawner at {}", pos);
			}
		}
	}

	@Override
	public byte getId() {
		return SPAWNER_INFO_ID;
	}

	@Override
	protected void writeToByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		ByteBufUtils.writeVarInt(buf, compoundTagList.tagCount(), 5);
		compoundTagList.appendTag(this.tileentityData);
	}

	@Override
	protected void readFromByteBufInternal(ByteBuf buf, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		this.tileentityData = compoundTagList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
	}

	public NBTTagCompound getTileentityData() {
		return this.tileentityData;
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

	private void vanillaSpawnerReadFromNBT(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, TileEntityMobSpawner tileEntityMobSpawner, NBTTagCompound tileentityData) {
		MobSpawnerBaseLogic spawnerBaseLogic = tileEntityMobSpawner.getSpawnerBaseLogic();
		NBTTagCompound compound = new NBTTagCompound();

		compound.setShort("Delay", (short) 20);
		if (tileentityData.hasKey("MinSpawnDelay", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("MinSpawnDelay", tileentityData.getShort("MinSpawnDelay"));
			compound.setShort("MaxSpawnDelay", tileentityData.getShort("MaxSpawnDelay"));
			compound.setShort("SpawnCount", tileentityData.getShort("SpawnCount"));
		}
		if (tileentityData.hasKey("MaxNearbyEntities", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("MaxNearbyEntities", tileentityData.getShort("MaxNearbyEntities"));
			compound.setShort("RequiredPlayerRange", tileentityData.getShort("RequiredPlayerRange"));
		}
		if (tileentityData.hasKey("SpawnRange", Constants.NBT.TAG_ANY_NUMERIC)) {
			compound.setShort("SpawnRange", tileentityData.getShort("SpawnRange"));
		}
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagList items = tileentityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound itemTag = items.getCompoundTagAt(i);
			NBTTagCompound entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = this.createEntityFromTag(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, entityTag);

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

	private void cqrSpawnerReadFromNBT(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, TileEntitySpawner tileEntitySpawner, NBTTagCompound tileentityData) {
		NBTTagList items = tileentityData.getCompoundTag("inventory").getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount() && i < tileEntitySpawner.inventory.getSlots(); i++) {
			NBTTagCompound itemTag = items.getCompoundTagAt(i);
			NBTTagCompound entityTag = itemTag.getCompoundTag("tag").getCompoundTag("EntityIn");
			Entity entity = this.createEntityFromTag(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, entityTag);

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
				tileEntitySpawner.inventory.insertItem(i, stack, false);
			}
		}
	}

	private Entity createEntityFromTag(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion, NBTTagCompound entityTag) {
		if (entityTag.isEmpty()) {
			return null;
		}

		entityTag.removeTag("UUIDLeast");
		entityTag.removeTag("UUIDMost");
		entityTag.removeTag("Pos");

		String id = entityTag.getString("id");
		if (id.equals(Reference.MODID + ":dummy")) {
			entityTag.setString("id", dungeonMob.getEntityID().toString());
		}

		Entity entity = EntityList.createEntityFromNBT(entityTag, world);

		entityTag.setString("id", id);

		if (entity != null) {
			BlockPos pos = this.getTransformedBlockPos(dungeonPartPos, settings.getMirror(), settings.getRotation());
			entity.setPosition(pos.getX(), pos.getY() + 1.0D, pos.getZ());

			if (entity instanceof EntityLiving) {
				if (CQRConfig.general.mobsFromCQSpawnerDontDespawn) {
					((EntityLiving) entity).enablePersistence();
				}

				if (entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(dungeonPos, settings, dungeonMob);
				}
			}

			NBTTagList passengers = entityTag.getTagList("Passengers", Constants.NBT.TAG_COMPOUND);
			for (NBTBase passengerNBT : passengers) {
				Entity passenger = this.createEntityFromTag(world, dungeonPos, dungeonPartPos, settings, dungeonMob, protectedRegion, (NBTTagCompound) passengerNBT);
				passenger.startRiding(entity);
			}
		}

		return entity;
	}

	@Deprecated
	public BlockInfoSpawner(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		super(x, y, z);
		int[] ints = nbtTagIntArray.getIntArray();
		if (ints.length > 2) {
			this.tileentityData = compoundTagList.getCompoundTagAt(ints[2]);
		}
	}

}
