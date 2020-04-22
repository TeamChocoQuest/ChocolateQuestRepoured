package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;

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

	
	//TODO: Initialize this on world load
	/* 
	 * First key: Dimension
	 * Second key: Dungeon name
	 * Third value: where this dungeon did spawn
	 */

	public static DungeonRegistry getInstance() {
		return DungeonRegistry.instance;
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
		Biome biome = world.getBiomeProvider().getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));

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
			DungeonBase dungeon = this.createDungeonFromFile(file);

			if (dungeon != null) {
				this.dungeons.add(dungeon);

				if (this.isDungeonMissingModDependencies(dungeon)) {
					CQRMain.logger.warn("{}: Dungeon is missing mod dependencies!", file.getName());
					continue;
				}

				if (dungeon.isPosLocked()) {
					this.coordinateSpecificDungeons.add(dungeon);
					continue;
				}

				if (dungeon.getWeight() <= 0) {
					CQRMain.logger.warn("{}: Dungeon weight is set to or below 0!", file.getName());
					continue;
				}

				if (dungeon.getChance() <= 0) {
					CQRMain.logger.warn("{}: Dungeon chance is set to or below 0!", file.getName());
					continue;
				}

				for (String s : dungeon.getBiomes()) {
					if (s.equalsIgnoreCase("*") || s.equalsIgnoreCase("ALL")) {
						// Add to all biome types and biomes
						this.addDungeonToAllBiomes(dungeon);
						this.addDungeonToAllBiomeTypes(dungeon);
						break;
					} else if (this.isBiomeType(s)) {
						// Add to biome type
						this.addDungeonToBiomeType(dungeon, this.getBiomeTypeByName(s));
					} else {
						// Add to biome
						this.addDungeonToBiome(dungeon, new ResourceLocation(s));
					}
				}

				// TODO: Fix biome blacklisting
				for (String s : dungeon.getBlacklistedBiomes()) {
					if (s.equalsIgnoreCase("*") || s.equalsIgnoreCase("ALL")) {
						// Remove from all biome types and biomes
						this.removeDungeonFromAllBiomes(dungeon);
						this.removeDungeonFromAllBiomeTypes(dungeon);
						break;
					} else if (this.isBiomeType(s)) {
						// Remove from biome type
						this.removeDungeonFromBiomeType(dungeon, this.getBiomeTypeByName(s));
					} else {
						// Remove from biome
						this.removeDungeonFromBiome(dungeon, new ResourceLocation(s));
					}
				}
			}
		}
	}

	private DungeonBase createDungeonFromFile(File file) {
		Properties prop = new Properties();
		try (InputStream inputStream = new FileInputStream(file)) {
			prop.load(inputStream);
		} catch (IOException e) {
			CQRMain.logger.error("Failed to load file" + file.getName(), e);
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		String generatorType = prop.getProperty("generator", "");
		EDungeonGenerator dungeonGenerator = EDungeonGenerator.getDungeonGenerator(generatorType);

		return dungeonGenerator != null ? dungeonGenerator.createDungeon(name, prop) : null;
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
		if (dungeon.dependsOnOtherStructures()) {
			Set<String> spawnedTypes = DungeonDataManager.getSpawnedDungeonNames(world);
			if(spawnedTypes.isEmpty()) {
				return true;
			}
			for (String s : dungeon.getDungeonDependencies()) {
				if(!spawnedTypes.contains(s)) {
					return true;
				}
				Set<BlockPos> spawnedLocs = DungeonDataManager.getLocationsOfDungeon(world, s);
				if(spawnedLocs.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean canDungeonSpawnAgain(World world, DungeonBase dungeon) {
		return (dungeon.getSpawnLimit() < 0) || !DungeonDataManager.getInstance(world).isDungeonSpawnLimitMet(dungeon);
	}

}
