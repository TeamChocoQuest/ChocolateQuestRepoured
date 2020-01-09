package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonRegistry {

	protected List<DungeonBase> dungeonList = new ArrayList<DungeonBase>();
	private HashMap<ResourceLocation, List<DungeonBase>> biomeDungeonMap = new HashMap<ResourceLocation, List<DungeonBase>>();
	private HashMap<BiomeDictionary.Type, List<DungeonBase>> biomeTypeDungeonMap = new  HashMap<>();
	private HashMap<BlockPos, List<DungeonBase>> coordinateSpecificDungeons = new HashMap<BlockPos, List<DungeonBase>>();

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
									this.dungeonList.add(dungeon);
								}
							} else {
								// Biome map filling
								String[] biomeNames = PropertyFileHelper.getStringArrayProperty(dungeonConfig, "biomes", new String[] { "PLAINS" });
								for (String biomeName : biomeNames) {
									if (biomeName.equalsIgnoreCase("*") || biomeName.equalsIgnoreCase("ALL")) {
										// Add dungeon to all biomes
										this.addDungeonToAllBiomes(dungeon);
										break;
									} else if (this.isBiomeType(biomeName)) {
										// Add dungeon to all biomes from biome type
										BiomeDictionary.Type biomeType = this.getBiomeTypeByName(biomeName);
										for (Biome biome : BiomeDictionary.getBiomes(biomeType)) {
											this.addDungeonToBiome(dungeon, biome);
										}
										List<DungeonBase> dungeonTmp = biomeTypeDungeonMap.getOrDefault(biomeType, new ArrayList<DungeonBase>());
										dungeonTmp.add(dungeon);
										biomeTypeDungeonMap.replace(biomeType, dungeonTmp);
									} else {
										// Add dungeon to biome from registry name
										Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
										if (biome != null) {
											this.addDungeonToBiome(dungeon, biome);
										}
									}
								}

								this.dungeonList.add(dungeon);
							}
						} else {
							CQRMain.logger.warn(file.getName() + ": Dungeon is missing mod dependencies!");
						}
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

	public Set<DungeonBase> getDungeonsForBiome(Biome biome, Set<BiomeDictionary.Type> types) {
		Set<DungeonBase> dungeons = new HashSet<>();
		if (biome != null && this.biomeDungeonMap.containsKey(biome.getRegistryName()) && !this.biomeDungeonMap.get(biome.getRegistryName()).isEmpty()) {
			dungeons.addAll(biomeDungeonMap.get(biome.getRegistryName()));
		}
		if(!types.isEmpty()) {
			for(BiomeDictionary.Type type : types) {
				if(type != null && biomeTypeDungeonMap.containsKey(type) && !this.biomeTypeDungeonMap.get(type).isEmpty()) {
					dungeons.addAll(biomeTypeDungeonMap.get(type));
				}
			}
		}
		return dungeons;
	}

	public void addBiomeEntryToMap(Biome biome) {
		if (this.biomeDungeonMap != null) {
			if (!this.biomeDungeonMap.containsKey(biome.getRegistryName())) {
				this.biomeDungeonMap.put(biome.getRegistryName(), new ArrayList<DungeonBase>());
			}
		}
	}

	public int getDungeonDistance() {
		return CQRConfig.general.dungeonSeparation;
	}

	public int getDungeonSpawnDistance() {
		return CQRConfig.general.dungeonSpawnDistance;
	}

	public HashMap<BlockPos, List<DungeonBase>> getCoordinateSpecificsMap() {
		return this.coordinateSpecificDungeons;
	}

	private void addDungeonToAllBiomes(DungeonBase dungeon) {
		for (ResourceLocation biome : this.biomeDungeonMap.keySet()) {
			this.addDungeonToBiome(dungeon, biome);
		}
	}

	private void addDungeonToBiome(DungeonBase dungeon, Biome biome) {
		addDungeonToBiome(dungeon, biome.getRegistryName());
	}
	
	private void addDungeonToBiome(DungeonBase dungeon, ResourceLocation biome) {
		List<DungeonBase> dungeonList = this.biomeDungeonMap.getOrDefault(biome, new ArrayList<DungeonBase>());
		if (!dungeonList.contains(dungeon)) {
			dungeonList.add(dungeon);
			this.biomeDungeonMap.replace(biome, dungeonList);
		}
	}

	private BiomeDictionary.Type getBiomeTypeByName(String biomeName) {
		for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
			if (biomeName.equalsIgnoreCase(biomeType.getName()) || biomeName.equalsIgnoreCase(biomeType.toString())) {
				return biomeType;
			}
		}
		return null;
	}

	private boolean isBiomeType(String biomeName) {
		return this.getBiomeTypeByName(biomeName) != null;
	}

	public static void loadDungeons() {
		for (Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
			CQRMain.dungeonRegistry.addBiomeEntryToMap(biome);
		}

		CQRMain.dungeonRegistry.loadDungeonFiles();
	}

	public DungeonBase getDungeon(String name) {
		for (DungeonBase dungeon : this.dungeonList) {
			if (dungeon.getDungeonName().equals(name)) {
				return dungeon;
			}
		}
		return null;
	}

	public List<DungeonBase> getLoadedDungeons() {
		return this.dungeonList;
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
