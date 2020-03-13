package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.GuardedCastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdOpenDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonRegistry {

	private static DungeonRegistry instance = new DungeonRegistry();

	private Set<DungeonBase> dungeons = new HashSet<>();
	private Map<ResourceLocation, Set<DungeonBase>> biomeDungeonMap = new HashMap<>();
	private Map<BiomeDictionary.Type, Set<DungeonBase>> biomeTypeDungeonMap = new HashMap<>();
	private Set<DungeonBase> coordinateSpecificDungeons = new HashSet<>();

	private Map<World, Set<String>> worldDungeonSpawnedMap = new HashMap<>();

	public static DungeonRegistry getInstance() {
		return instance;
	}

	public DungeonBase getDungeon(String name) {
		for (DungeonBase dungeon : this.dungeons) {
			if (dungeon.getDungeonName().equals(name)) {
				return dungeon;
			}
		}
		return null;
	}

	public Set<DungeonBase> getDungeonsForChunk(World world, int chunkX, int chunkZ, boolean behindWall) {
		Set<DungeonBase> dungeonsForChunk = new HashSet<>();
		Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 1, 0, chunkZ * 16 + 1));

		Set<DungeonBase> biomeDungeonSet = this.biomeDungeonMap.get(biome.getRegistryName());
		if (biomeDungeonSet != null) {
			for (DungeonBase dungeon : biomeDungeonSet) {
				if (this.canDungeonSpawnInWorld(world, dungeon, behindWall)) {
					dungeonsForChunk.add(dungeon);
				}
			}
		} else {
			this.biomeDungeonMap.put(biome.getRegistryName(), new HashSet<DungeonBase>());
		}

		for (BiomeDictionary.Type biomeType : BiomeDictionary.getTypes(biome)) {
			Set<DungeonBase> biomeTypeDungeonSet = this.biomeTypeDungeonMap.get(biomeType);
			if (biomeTypeDungeonSet != null) {
				for (DungeonBase dungeon : biomeTypeDungeonSet) {
					if (this.canDungeonSpawnInWorld(world, dungeon, behindWall)) {
						dungeonsForChunk.add(dungeon);
					}
				}
			} else {
				this.biomeTypeDungeonMap.put(biomeType, new HashSet<DungeonBase>());
			}
		}

		return dungeonsForChunk;
	}

	public Set<DungeonBase> getLoadedDungeons() {
		return this.dungeons;
	}

	public Set<DungeonBase> getCoordinateSpecificDungeons() {
		return this.coordinateSpecificDungeons;
	}

	public void loadDungeons() {
		for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
			this.biomeDungeonMap.put(biome.getRegistryName(), new HashSet<DungeonBase>());
		}
		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
			this.biomeTypeDungeonMap.put(biomeType, new HashSet<DungeonBase>());
		}

		this.loadDungeonFiles();
	}

	public void reloadDungeons() {
		this.dungeons.clear();
		this.biomeDungeonMap.clear();
		this.biomeTypeDungeonMap.clear();
		this.coordinateSpecificDungeons.clear();
		this.loadDungeons();
	}

	public void loadDungeonFiles() {
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} dungeon configuration files...", files.size());

		for (File file : files) {
			Properties dungeonConfig = new Properties();
			try (FileInputStream stream = new FileInputStream(file)) {
				dungeonConfig.load(stream);
			} catch (IOException e) {
				CQRMain.logger.error("{}: Failed to load file!", file.getName());
				continue;
			}
			String generatorType = dungeonConfig.getProperty("generator", "TEMPLATE_SURFACE");

			if (EDungeonGenerator.isValidDungeonGenerator(generatorType)) {
				DungeonBase dungeon = this.getDungeonByType(generatorType, file);

				if (dungeon != null && dungeon.isRegisteredSuccessful()) {
					if (!this.isDungeonMissingModDependencies(dungeon)) {
						if (PropertyFileHelper.getBooleanProperty(dungeonConfig, "spawnAtCertainPosition", false)) {
							// Position restriction stuff here
							if (this.handleLockedPos(dungeon, dungeonConfig)) {
								this.coordinateSpecificDungeons.add(dungeon);
							}
						} else if (dungeon.getSpawnChance() > 0) {
							// Biome map filling
							String[] biomeNames = PropertyFileHelper.getStringArrayProperty(dungeonConfig, "biomes", new String[] { "PLAINS" });
							// Biome blacklist
							String[] biomeNamesBlackList = PropertyFileHelper.getStringArrayProperty(dungeonConfig, "disallowedBiomes", new String[] {});
							for (String biomeName : biomeNames) {
								if (biomeName.equalsIgnoreCase("*") || biomeName.equalsIgnoreCase("ALL")) {
									// Add dungeon to all biomes
									this.addDungeonToAllBiomes(dungeon);
									this.addDungeonToAllBiomeTypes(dungeon);
									break;
								} else if (this.isBiomeType(biomeName)) {
									// Add dungeon to all biomes from biome type
									this.addDungeonToBiomeType(dungeon, this.getBiomeTypeByName(biomeName));
								} else {
									// Add dungeon to biome from registry name
									this.addDungeonToBiome(dungeon, new ResourceLocation(biomeName));
								}
							}
							if (biomeNamesBlackList.length > 0) {
								for (String nope : biomeNamesBlackList) {
									if (nope.equalsIgnoreCase("*") || nope.equalsIgnoreCase("ALL")) {
										// Remove from everything
										this.removeDungeonFromAllBiomes(dungeon);
										this.removeDungeonFromAllBiomeTypes(dungeon);
										break;
									} else if (this.isBiomeType(nope)) {
										// Remove from type
										this.removeDungeonFromBiomeType(dungeon, this.getBiomeTypeByName(nope));
									} else {
										// Remove from biome
										this.removeDungeonFromBiome(dungeon, new ResourceLocation(nope));
									}
								}
							}
						} else {
							CQRMain.logger.warn("{}: Dungeon spawnrate is set to or below 0!", file.getName());
						}
					} else {
						CQRMain.logger.warn("{}: Dungeon is missing mod dependencies!", file.getName());
					}

					this.dungeons.add(dungeon);
				} else {
					CQRMain.logger.warn("{}: Couldn't create dungeon for generator type {}!", file.getName(), generatorType);
				}
			} else {
				CQRMain.logger.warn("{}: Generator type {} is invalid!", file.getName(), generatorType);
			}
		}
	}

	private boolean handleLockedPos(DungeonBase dungeon, Properties dungeonConfig) {
		String[] coordinates = dungeonConfig.getProperty("spawnAt", "-;-;-").split(";");
		try {
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);
			int z = Integer.parseInt(coordinates[2]);
			dungeon.setLockPos(new BlockPos(x, y, z), true);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			CQRMain.logger.error("{}: Failed to read spawn position!", dungeon.getDungeonName());
			return false;
		}
		return true;
	}

	private DungeonBase getDungeonByType(String dunType, File dungeonPropertiesFile) {
		switch (EDungeonGenerator.valueOf(dunType.toUpperCase())) {
		case CASTLE:
			return new CastleDungeon(dungeonPropertiesFile);
		case CAVERNS:
			return new CavernDungeon(dungeonPropertiesFile);
		case FLOATING_NETHER_CITY:
			return new FloatingNetherCity(dungeonPropertiesFile);
		case NETHER_CITY:
			return new ClassicNetherCity(dungeonPropertiesFile);
		case STRONGHOLD:
			return new StrongholdOpenDungeon(dungeonPropertiesFile);
		case TEMPLATE_OCEAN_FLOOR:
			return new DungeonOceanFloor(dungeonPropertiesFile);
		case TEMPLATE_SURFACE:
			return new DefaultSurfaceDungeon(dungeonPropertiesFile);
		case GUARDED_CASTLE:
			return new GuardedCastleDungeon(dungeonPropertiesFile);
		case VOLCANO:
			return new VolcanoDungeon(dungeonPropertiesFile);
		case CLASSIC_STRONGHOLD:
			return new StrongholdLinearDungeon(dungeonPropertiesFile);
		case GREEN_CAVE:
			// TODO SWAMP CAVE GENERATOR
			CQRMain.logger.warn("Dungeon Generator GREEN_CAVE is not yet implemented!");
			break;
		default:
			return null;
		}
		return null;
	}

	private void addDungeonToAllBiomes(DungeonBase dungeon) {
		for (Set<DungeonBase> dungeonSet : this.biomeDungeonMap.values()) {
			dungeonSet.add(dungeon);
		}
	}

	private void addDungeonToAllBiomeTypes(DungeonBase dungeon) {
		for (Set<DungeonBase> dungeonSet : this.biomeTypeDungeonMap.values()) {
			dungeonSet.add(dungeon);
		}
	}

	private void addDungeonToBiome(DungeonBase dungeon, ResourceLocation biome) {
		Set<DungeonBase> dungeonSet = this.biomeDungeonMap.get(biome);
		if (dungeonSet != null) {
			dungeonSet.add(dungeon);
		} else {
			dungeonSet = new HashSet<>();
			dungeonSet.add(dungeon);
			this.biomeDungeonMap.put(biome, dungeonSet);
		}
	}

	private void addDungeonToBiomeType(DungeonBase dungeon, BiomeDictionary.Type biomeType) {
		Set<DungeonBase> dungeonSet = this.biomeTypeDungeonMap.get(biomeType);
		if (dungeonSet != null) {
			dungeonSet.add(dungeon);
		} else {
			dungeonSet = new HashSet<>();
			dungeonSet.add(dungeon);
			this.biomeTypeDungeonMap.put(biomeType, dungeonSet);
		}
	}

	private void removeDungeonFromAllBiomes(DungeonBase dungeon) {
		for (Set<DungeonBase> dungeonSet : this.biomeDungeonMap.values()) {
			dungeonSet.remove(dungeon);
		}
	}

	private void removeDungeonFromAllBiomeTypes(DungeonBase dungeon) {
		for (Set<DungeonBase> dungeonSet : this.biomeTypeDungeonMap.values()) {
			dungeonSet.remove(dungeon);
		}
	}

	private void removeDungeonFromBiome(DungeonBase dungeon, ResourceLocation biome) {
		Set<DungeonBase> dungeonSet = this.biomeDungeonMap.get(biome);
		if (dungeonSet != null) {
			dungeonSet.remove(dungeon);
		}
	}

	private void removeDungeonFromBiomeType(DungeonBase dungeon, BiomeDictionary.Type biomeType) {
		Set<DungeonBase> dungeonSet = this.biomeTypeDungeonMap.get(biomeType);
		if (dungeonSet != null) {
			dungeonSet.remove(dungeon);
		}
	}

	private BiomeDictionary.Type getBiomeTypeByName(String biomeTypeName) {
		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
			if (biomeTypeName.equalsIgnoreCase(biomeType.getName())) {
				return biomeType;
			}
		}
		return null;
	}

	private boolean isBiomeType(String biomeName) {
		return this.getBiomeTypeByName(biomeName) != null;
	}

	public void insertDungeonEntries(World world, String... dungeonNames) {
		Set<String> set = new HashSet<>();
		Collections.addAll(set, dungeonNames);
		this.insertDungeonEntries(world, set);
	}

	public void insertDungeonEntries(World world, Set<String> dungeonNames) {
		Set<String> spawnedDungeons = this.worldDungeonSpawnedMap.getOrDefault(world, new HashSet<String>());
		spawnedDungeons.addAll(dungeonNames);
		this.worldDungeonSpawnedMap.put(world, spawnedDungeons);
	}

	private boolean isDungeonMissingModDependencies(DungeonBase dungeon) {
		for (String modid : dungeon.getDependencies()) {
			if (!Loader.isModLoaded(modid)) {
				return true;
			}
		}
		return false;
	}

	public boolean canDungeonSpawnInWorld(World world, DungeonBase dungeon, boolean behindWall) {
		int dim = world.provider.getDimension();
		if (!dungeon.isDimensionAllowed(dim)) {
			return false;
		}
		if (dim == 0 && !behindWall && dungeon.doesSpawnOnlyBehindWall()) {
			return false;
		}
		if (this.isDungeonMissingDungeonDependencies(world, dungeon)) {
			return false;
		}
		return this.canDungeonSpawnAgain(world, dungeon);
	}

	public boolean isDungeonMissingDungeonDependencies(World world, DungeonBase dungeon) {
		if (!dungeon.dependsOnOtherStructures()) {
			return false;
		}
		Set<String> spawned = this.worldDungeonSpawnedMap.getOrDefault(world, Collections.emptySet());
		if (spawned.isEmpty()) {
			return true;
		}
		for (String s : dungeon.getDungeonDependencies()) {
			if (!spawned.contains(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean canDungeonSpawnAgain(World world, DungeonBase dungeon) {
		return !dungeon.isUnique() || !this.hasUniqueDungeonAlreadyBeenSpawned(world, dungeon.getDungeonName());
	}

	public boolean hasUniqueDungeonAlreadyBeenSpawned(World world, String dungeonName) {
		return this.worldDungeonSpawnedMap.getOrDefault(world, Collections.emptySet()).contains(dungeonName);
	}

}
