package team.cqr.cqrepoured.util;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.mixin.AccessorAbstractSpawner;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

/**
 * A static utility class for generating CQR/vanilla spawners and converting them to/from the other
 * 
 * @author DerToaster, Meldexun, jdawg3636
 * @version 11 October 2019
 */
public final class SpawnerFactory {

	/*
	 * Creation/Modification
	 */

	/**
	 * Places a spawner in the provided world at the provided position. Spawner type (CQR/vanilla) is determined dynamically
	 * based upon the requested capabilities.
	 * 
	 * @param entities                 Entities for spawner to spawn
	 * @param multiUseSpawner          Determines spawner type. Vanilla = true; CQR = false.
	 * @param spawnerSettingsOverrides Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world                    World in which to place spawner
	 * @param pos                      Position at which to place spawner
	 */
	public static void placeSpawner(Entity[] entities, boolean multiUseSpawner, @Nullable CompoundNBT spawnerSettingsOverrides, World world, BlockPos pos) {
		CompoundNBT[] entCompounds = new CompoundNBT[entities.length];
		for (int i = 0; i < entities.length; i++) {
			Entity ent = entities[i];
			if (ent == null) {
				continue;
			}
			entCompounds[i] = createSpawnerNBTFromEntity(ent);
		}
		placeSpawner(entCompounds, multiUseSpawner, spawnerSettingsOverrides, world, pos);
	}

	/**
	 * Places a spawner in the provided world at the provided position. Spawner type (CQR/vanilla) is determined dynamically
	 * based upon the requested capabilities.
	 * 
	 * @param entities                 Entities as NBT Tag (From Entity.writeToNBTOptional(COMPOUND) for spawner to spawn
	 * @param multiUseSpawner          Determines spawner type. Vanilla = true; CQR = false.
	 * @param spawnerSettingsOverrides Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world                    World in which to place spawner
	 * @param pos                      Position at which to place spawner
	 */
	public static void placeSpawner(CompoundNBT[] entities, boolean multiUseSpawner, @Nullable CompoundNBT spawnerSettingsOverrides, World world, BlockPos pos) {
		BlockState blockState =  multiUseSpawner ? Blocks.SPAWNER.defaultBlockState() : CQRBlocks.SPAWNER.get().defaultBlockState(); 
		world.setBlockAndUpdate(pos,blockState);

		TileEntity tileEntity = world.getBlockEntity(pos);
		if (multiUseSpawner) {
			MobSpawnerTileEntity tileEntityMobSpawner = (MobSpawnerTileEntity) tileEntity;
			CompoundNBT compound = tileEntityMobSpawner.save(new CompoundNBT());
			ListNBT spawnPotentials = new ListNBT();

			// Store entity ids into NBT tag
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] != null) {
					{
						// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
						// entity
						entities[i].remove("UUID");

						entities[i].remove("Pos");
						ListNBT passengers = entities[i].getList("Passengers", 10);
						for (INBT passenger : passengers) {
							((CompoundNBT) passenger).remove("UUID");
							((CompoundNBT) passenger).remove("Pos");
						}
					}
					CompoundNBT spawnPotential = new CompoundNBT();
					spawnPotential.putInt("Weight", 1);
					spawnPotential.put("Entity", entities[i]);
					spawnPotentials.add(spawnPotential);
				}
			}
			compound.put("SpawnPotentials", spawnPotentials);
			compound.remove("SpawnData");

			// Store default settings into NBT
			if (spawnerSettingsOverrides != null) {
				compound.putInt("MinSpawnDelay", spawnerSettingsOverrides.getInt("MinSpawnDelay"));
				compound.putInt("MaxSpawnDelay", spawnerSettingsOverrides.getInt("MaxSpawnDelay"));
				compound.putInt("SpawnCount", spawnerSettingsOverrides.getInt("SpawnCount"));
				compound.putInt("MaxNearbyEntities", spawnerSettingsOverrides.getInt("MaxNearbyEntities"));
				compound.putInt("SpawnRange", spawnerSettingsOverrides.getInt("SpawnRange"));
				compound.putInt("RequiredPlayerRange", spawnerSettingsOverrides.getInt("RequiredPlayerRange"));
			}

			// Read data from modified nbt
			tileEntityMobSpawner.load(blockState, compound);

			tileEntityMobSpawner.setChanged();
		} else {
			TileEntitySpawner tileEntitySpawner = (TileEntitySpawner) tileEntity;

			for (int i = 0; i < entities.length && i < 9; i++) {
				if (entities[i] != null) {
					tileEntitySpawner.getInventory().setItem(i, getSoulBottleItemStackForEntity(entities[i]));
				}
			}

			tileEntitySpawner.setChanged();
		}
	}

	/**
	 * Places a vanilla spawner in the provided world at the provided position using the provided ResourceLocation for the
	 * entity that it should spawn.
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, ResourceLocation entityResLoc) {
		world.setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState());
		MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) world.getBlockEntity(pos);

		spawner.getSpawner().setEntityId(ForgeRegistries.ENTITIES.getValue(entityResLoc));

		spawner.setChanged();
		
		//Correct method?
		spawner.requestModelDataUpdate();
		spawner.tick();
	}

	public static MobSpawnerTileEntity getSpawnerTile(World world, ResourceLocation entity, BlockPos pos) {
		MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) world.getBlockEntity(pos);
		spawner.getSpawner().setEntityId(ForgeRegistries.ENTITIES.getValue(entity));
		return spawner;
	}

	/**
	 * Overloaded variant of normal createSimpleMultiUseSpawner method that accepts an Entity object rather than a resource
	 * location in its parameter
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, Entity entity) {
		createSimpleMultiUseSpawner(world, pos, EntityList.getKey(entity));
	}

	/*
	 * CQR/Vanilla Conversion
	 */

	/**
	 * Converts the CQR spawner at the provided World/BlockPos to a vanilla spawner
	 * 
	 * @param spawnerSettings
	 */
	public static void convertCQSpawnerToVanillaSpawner(World world, BlockPos pos, @Nullable CompoundNBT spawnerSettings) {
		TileEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner) tile;

			// Entity[] entities = new Entity[spawner.inventory.getSlots()];
			CompoundNBT[] entities = new CompoundNBT[spawner.getInventory().getContainerSize()];
			// Random rand = new Random();

			for (int i = 0; i < entities.length; i++) {
				ItemStack stack = spawner.getInventory().removeItem(i, spawner.getInventory().getItem(i).getCount()/*, false*/);// getStackInSlot(i);
				if (!stack.isEmpty()) {
					try {
						CompoundNBT tag = stack.getOrCreateTag();

						// NBTTagCompound entityTag = (NBTTagCompound)tag.getTag("EntityIn");
						entities[i] = tag.getCompound("EntityIn");
						/*
						 * entities[i] = createEntityFromNBTWithoutSpawningIt(entityTag, world);
						 * 
						 * entities[i].setUniqueId(MathHelper.getRandomUUID(rand));
						 */
					} catch (NullPointerException ignored) {
					}
				} else {
					entities[i] = null;
				}
			}
			world.destroyBlock(pos, false);

			placeSpawner(entities, true, spawnerSettings, world, pos);
		}
	}

	/**
	 * Converts the vanilla spawner at the provided World/BlockPos to a CQR spawner
	 */
	public static void convertVanillaSpawnerToCQSpawner(World world, BlockPos pos) {
		TileEntity tile = world.getBlockEntity(pos);
		if (tile != null && tile instanceof MobSpawnerTileEntity) {
			MobSpawnerTileEntity spawnerMultiUseTile = (MobSpawnerTileEntity) tile;

			//TODO: Create mixin to retrieve this
			List<WeightedSpawnerEntity> spawnerEntries = ((AccessorAbstractSpawner)spawnerMultiUseTile.getSpawner()).getSpawnPotentials();
			if (!spawnerEntries.isEmpty()) {
				Iterator<WeightedSpawnerEntity> iterator = spawnerEntries.iterator();

				// Entity[] entities = new Entity[9];
				CompoundNBT[] entityCompound = new CompoundNBT[9];

				int entriesRead = 0;
				while (entriesRead < 9 && iterator.hasNext()) {
					/*
					 * Entity entity = createEntityFromNBTWithoutSpawningIt(iterator.next().getNbt(), world); entities[entriesRead] =
					 * entity;
					 */
					entityCompound[entriesRead] = iterator.next().getTag();
					entriesRead++;
				}
				// placeSpawner(entities, false, null, world, pos);
				placeSpawner(entityCompound, false, null, world, pos);
			}
		}
	}

	/*
	 * Miscellaneous
	 */

	/**
	 * Converts provided NBT data into an entity in the provided world without actually spawning it
	 * 
	 * @return Generated entity object
	 */
	public static Entity createEntityFromNBTWithoutSpawningIt(CompoundNBT tag, World worldIn) {
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.load(tag);

		return entity;
	}

	public static CompoundNBT createSpawnerNBTFromEntity(Entity entity) {
		return createSpawnerNBTFromEntity(entity, (!(entity instanceof AbstractEntityCQRBoss) /*&& entity.isNonBoss()*/));
	}

	public static CompoundNBT createSpawnerNBTFromEntity(Entity entity, boolean removeUUID) {
		CompoundNBT entityCompound = new CompoundNBT();
		if (entity instanceof AbstractEntityCQR) {
			// ((AbstractEntityCQR) entity).onPutInSpawner();
		}
		entity.save(entityCompound);
		if (removeUUID) {
			entityCompound.remove("UUID");
		}
		entityCompound.remove("Pos");
		ListNBT passengerList = entityCompound.getList("Passengers", 10);
		for (INBT passengerTag : passengerList) {
			if (removeUUID) {
				((CompoundNBT) passengerTag).remove("UUID");
			}
			((CompoundNBT) passengerTag).remove("Pos");
		}

		return entityCompound;
	}

	/**
	 * Used internally for the placeSpawner method
	 */
	public static ItemStack getSoulBottleItemStackForEntity(Entity entity) {
		if (entity == null) {
			return null;
		}
		CompoundNBT entityTag = new CompoundNBT();
		if (entity.save(entityTag)) {
			return getSoulBottleItemStackForEntity(entityTag);
		}
		return null;

	}

	public static ItemStack getSoulBottleItemStackForEntity(CompoundNBT entityTag) {
		ItemStack bottle = new ItemStack(CQRItems.SOUL_BOTTLE.get());
		bottle.setCount(1);
		CompoundNBT mobToSpawnerItem = new CompoundNBT();

		mobToSpawnerItem.put("EntityIn", entityTag);
		bottle.setTag(mobToSpawnerItem);
		return bottle;
	}

}
