package team.cqr.cqrepoured.util;

import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
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
	public static void placeSpawner(Entity[] entities, boolean multiUseSpawner, @Nullable CompoundTag spawnerSettingsOverrides, Level world, BlockPos pos) {
		CompoundTag[] entCompounds = new CompoundTag[entities.length];
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
	public static void placeSpawner(CompoundTag[] entities, boolean multiUseSpawner, @Nullable CompoundTag spawnerSettingsOverrides, Level world, BlockPos pos) {
		BlockState blockState =  multiUseSpawner ? Blocks.SPAWNER.defaultBlockState() : CQRBlocks.SPAWNER.get().defaultBlockState();
		world.setBlockAndUpdate(pos,blockState);

		BlockEntity tileEntity = world.getBlockEntity(pos);
		if (multiUseSpawner) {
			SpawnerBlockEntity tileEntityMobSpawner = (SpawnerBlockEntity) tileEntity;
			CompoundTag compound = tileEntityMobSpawner.saveWithFullMetadata();
			ListTag spawnPotentials = new ListTag();

			// Store entity ids into NBT tag
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] != null) {
					{
						// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
						// entity
						entities[i].remove("UUID");

						entities[i].remove("Pos");
						ListTag passengers = entities[i].getList("Passengers", 10);
						for (Tag passenger : passengers) {
							((CompoundTag) passenger).remove("UUID");
							((CompoundTag) passenger).remove("Pos");
						}
					}
					CompoundTag spawnPotential = new CompoundTag();
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
			tileEntityMobSpawner.load(compound);

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
	public static void createSimpleMultiUseSpawner(Level world, BlockPos pos, ResourceLocation entityResLoc) {
		world.setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState());
		SpawnerBlockEntity spawner = (SpawnerBlockEntity) world.getBlockEntity(pos);

		spawner.getSpawner().setEntityId(ForgeRegistries.ENTITY_TYPES.getValue(entityResLoc), world, world.getRandom(), pos);

		spawner.setChanged();
		
		//Correct method?
		spawner.requestModelDataUpdate();
		SpawnerBlockEntity.serverTick(world, pos, world.getBlockState(pos), spawner);
	}

	public static SpawnerBlockEntity getSpawnerTile(Level world, ResourceLocation entity, BlockPos pos) {
		SpawnerBlockEntity spawner = (SpawnerBlockEntity) world.getBlockEntity(pos);
		spawner.getSpawner().setEntityId(ForgeRegistries.ENTITY_TYPES.getValue(entity), world, world.getRandom(), pos);
		return spawner;
	}

	/**
	 * Overloaded variant of normal createSimpleMultiUseSpawner method that accepts an Entity object rather than a resource
	 * location in its parameter
	 */
	public static void createSimpleMultiUseSpawner(Level world, BlockPos pos, Entity entity) {
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
	public static void convertCQSpawnerToVanillaSpawner(Level world, BlockPos pos, @Nullable CompoundTag spawnerSettings) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner) tile;

			// Entity[] entities = new Entity[spawner.inventory.getSlots()];
			CompoundTag[] entities = new CompoundTag[spawner.getInventory().getContainerSize()];
			// Random rand = new Random();

			for (int i = 0; i < entities.length; i++) {
				ItemStack stack = spawner.getInventory().removeItem(i, spawner.getInventory().getItem(i).getCount()/*, false*/);// getStackInSlot(i);
				if (!stack.isEmpty()) {
					try {
						CompoundTag tag = stack.getOrCreateTag();

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
	public static void convertVanillaSpawnerToCQSpawner(Level world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile != null && tile instanceof SpawnerBlockEntity) {
			SpawnerBlockEntity spawnerMultiUseTile = (SpawnerBlockEntity) tile;

			//TODO: Create mixin to retrieve this
			SimpleWeightedRandomList<SpawnData> spawnerEntries = ((AccessorAbstractSpawner)spawnerMultiUseTile.getSpawner()).getSpawnPotentials();
			if (!spawnerEntries.isEmpty()) {
				Iterator<Wrapper<SpawnData>> iterator = spawnerEntries.unwrap().iterator();

				// Entity[] entities = new Entity[9];
				CompoundTag[] entityCompound = new CompoundTag[9];

				int entriesRead = 0;
				while (entriesRead < 9 && iterator.hasNext()) {
					/*
					 * Entity entity = createEntityFromNBTWithoutSpawningIt(iterator.next().getNbt(), world); entities[entriesRead] =
					 * entity;
					 */
					entityCompound[entriesRead] = iterator.next().getData().getEntityToSpawn();
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
	public static Entity createEntityFromNBTWithoutSpawningIt(CompoundTag tag, Level worldIn) {
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.load(tag);

		return entity;
	}

	public static CompoundTag createSpawnerNBTFromEntity(Entity entity) {
		return createSpawnerNBTFromEntity(entity, (!(entity instanceof AbstractEntityCQRBoss) /*&& entity.isNonBoss()*/));
	}

	public static CompoundTag createSpawnerNBTFromEntity(Entity entity, boolean removeUUID) {
		CompoundTag entityCompound = new CompoundTag();
		if (entity instanceof AbstractEntityCQR) {
			// ((AbstractEntityCQR) entity).onPutInSpawner();
		}
		entity.save(entityCompound);
		if (removeUUID) {
			entityCompound.remove("UUID");
		}
		entityCompound.remove("Pos");
		ListTag passengerList = entityCompound.getList("Passengers", 10);
		for (Tag passengerTag : passengerList) {
			if (removeUUID) {
				((CompoundTag) passengerTag).remove("UUID");
			}
			((CompoundTag) passengerTag).remove("Pos");
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
		CompoundTag entityTag = new CompoundTag();
		if (entity.save(entityTag)) {
			return getSoulBottleItemStackForEntity(entityTag);
		}
		return null;

	}

	public static ItemStack getSoulBottleItemStackForEntity(CompoundTag entityTag) {
		ItemStack bottle = new ItemStack(CQRItems.SOUL_BOTTLE.get());
		bottle.setCount(1);
		CompoundTag mobToSpawnerItem = new CompoundTag();

		mobToSpawnerItem.put("EntityIn", entityTag);
		bottle.setTag(mobToSpawnerItem);
		return bottle;
	}

}
