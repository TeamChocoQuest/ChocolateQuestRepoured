package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.AbandonedDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.GuardedCastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.RuinDungeon;
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

	private Set<DungeonBase> dungeonSet = new HashSet<DungeonBase>();
	private Map<ResourceLocation, Set<DungeonBase>> biomeDungeonMap = new HashMap<ResourceLocation, Set<DungeonBase>>();
	private Map<BiomeDictionary.Type, Set<DungeonBase>> biomeTypeDungeonMap = new HashMap<BiomeDictionary.Type, Set<DungeonBase>>();
	private Set<DungeonBase> coordinateSpecificDungeons = new HashSet<DungeonBase>();

	public static DungeonRegistry getInstance() {
		return instance;
	}

	public void reloadDungeonFiles() {
		this.dungeonSet.clear();
		this.biomeDungeonMap.clear();
		this.biomeTypeDungeonMap.clear();
		this.coordinateSpecificDungeons.clear();
		this.loadDungeons();
	}

	public void loadDungeonFiles() {
		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading " + files.size() + " dungeon configuration files...");

		for (File file : files) {
			Properties dungeonConfig = new Properties();
			FileInputStream stream = null;
			try {
				stream = new FileInputStream(file);
				dungeonConfig.load(stream);

				String generatorType = dungeonConfig.getProperty("generator", "TEMPLATE_SURFACE");

				if (EDungeonGenerator.isValidDungeonGenerator(generatorType)) {
					DungeonBase dungeon = this.getDungeonByType(generatorType, file);

					if (dungeon != null && dungeon.isRegisteredSuccessful()) {
						if (!this.areDependenciesMissing(dungeon)) {
							if (PropertyFileHelper.getBooleanProperty(dungeonConfig, "spawnAtCertainPosition", false)) {
								// Position restriction stuff here
								if (this.handleLockedPos(dungeon, dungeonConfig)) {
									this.coordinateSpecificDungeons.add(dungeon);
								}
							} else if (dungeon.getSpawnChance() > 0) {
								// Biome map filling
								String[] biomeNames = PropertyFileHelper.getStringArrayProperty(dungeonConfig, "biomes", new String[] { "PLAINS" });
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
							} else {
								CQRMain.logger.warn(file.getName() + ": Dungeon spawnrate is set to or below 0!");
							}
						} else {
							CQRMain.logger.warn(file.getName() + ": Dungeon is missing mod dependencies!");
						}

						this.dungeonSet.add(dungeon);
					} else {
						CQRMain.logger.warn(file.getName() + ": Couldn't create dungeon for generator type " + generatorType + "!");
					}
				} else {
					CQRMain.logger.warn(file.getName() + ": Generator type " + generatorType + " is invalid!");
				}
			} catch (IOException e) {
				CQRMain.logger.error(file.getName() + ": Failed to load file!");
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					CQRMain.logger.error(file.getName() + ": Failed to close input stream!");
				}
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
		} catch (NumberFormatException e) {
			CQRMain.logger.error(dungeon.getDungeonName() + ": Failed to read spawn position!");
			return false;
		}
		return true;
	}

	private DungeonBase getDungeonByType(String dunType, File dungeonPropertiesFile) {
		switch (EDungeonGenerator.valueOf(dunType.toUpperCase())) {
		case ABANDONED:
			return new AbandonedDungeon(dungeonPropertiesFile);
		case CASTLE:
			return new CastleDungeon(dungeonPropertiesFile);
		case CAVERNS:
			return new CavernDungeon(dungeonPropertiesFile);
		case FLOATING_NETHER_CITY:
			return new FloatingNetherCity(dungeonPropertiesFile);
		case NETHER_CITY:
			return new ClassicNetherCity(dungeonPropertiesFile);
		case RUIN:
			return new RuinDungeon(dungeonPropertiesFile);
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
		case JUNGLE_CAVE:
			// TODO Jungle cave generator
			CQRMain.logger.warn("Dungeon Generator JUNGLE_CAVE is not yet implemented!");
			break;
		case SWAMP_CAVE:
			// TODO SWAMP CAVE GENERATOR
			CQRMain.logger.warn("Dungeon Generator SWAMP_CAVE is not yet implemented!");
			break;
		default:
			return null;
		}
		return null;
	}

	public Set<DungeonBase> getDungeonsForChunk(World world, int chunkX, int chunkZ, boolean behindWall) {
		Set<DungeonBase> dungeons = new HashSet<DungeonBase>();

		Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 1, 0, chunkZ * 16 + 1));
		Set<DungeonBase> biomeDungeonSet = this.biomeDungeonMap.get(biome.getRegistryName());
		if (biomeDungeonSet != null) {
			for (DungeonBase dungeon : biomeDungeonSet) {
				if (dungeon.isDimensionAllowed(world.provider.getDimension()) && (behindWall || !dungeon.doesSpawnOnlyBehindWall())) {
					dungeons.add(dungeon);
				}
			}
		} else {
			this.biomeDungeonMap.put(biome.getRegistryName(), new HashSet<DungeonBase>());
		}
		for (BiomeDictionary.Type biomeType : BiomeDictionary.getTypes(biome)) {
			Set<DungeonBase> biomeTypeDungeonSet = this.biomeTypeDungeonMap.get(biomeType);
			if (biomeTypeDungeonSet != null) {
				for (DungeonBase dungeon : biomeTypeDungeonSet) {
					if (dungeon.isDimensionAllowed(world.provider.getDimension()) && (behindWall || !dungeon.doesSpawnOnlyBehindWall())) {
						dungeons.add(dungeon);
					}
				}
			} else {
				this.biomeTypeDungeonMap.put(biomeType, new HashSet<DungeonBase>());
			}
		}

		return dungeons;
	}

	public Set<DungeonBase> getCoordinateSpecificsMap() {
		return this.coordinateSpecificDungeons;
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
			dungeonSet = new HashSet<DungeonBase>();
			dungeonSet.add(dungeon);
			this.biomeDungeonMap.put(biome, dungeonSet);
		}
	}

	private void addDungeonToBiomeType(DungeonBase dungeon, BiomeDictionary.Type biomeType) {
		Set<DungeonBase> dungeonSet = this.biomeTypeDungeonMap.get(biomeType);
		if (dungeonSet != null) {
			dungeonSet.add(dungeon);
		} else {
			dungeonSet = new HashSet<DungeonBase>();
			dungeonSet.add(dungeon);
			this.biomeTypeDungeonMap.put(biomeType, dungeonSet);
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

	public void loadDungeons() {
		for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
			this.biomeDungeonMap.put(biome.getRegistryName(), new HashSet<DungeonBase>());
		}
		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
			this.biomeTypeDungeonMap.put(biomeType, new HashSet<DungeonBase>());
		}

		this.loadDungeonFiles();
	}

	public DungeonBase getDungeon(String name) {
		for (DungeonBase dungeon : this.dungeonSet) {
			if (dungeon.getDungeonName().equals(name)) {
				return dungeon;
			}
		}
		return null;
	}

	public Set<DungeonBase> getLoadedDungeons() {
		return this.dungeonSet;
	}

	private boolean areDependenciesMissing(DungeonBase dungeon) {
		for (String modid : dungeon.getDependencies()) {
			if (!Loader.isModLoaded(modid)) {
				return true;
			}
		}
		return false;
	}

}
