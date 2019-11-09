package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.NetherCityGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class ClassicNetherCity extends DungeonBase {
	
	private int minRowsX = 5;
	private int maxRowsX = 7;
	private int minRowsZ = 5;
	private int maxRowsZ = 7;
	private int heightY = 31;
	private int posY = 31;
	private double bridgeSizeMultiplier = 1.2D;
	//private boolean singleAirPocketsForHouses = false;
	private boolean specialUseForCentralBuilding = false;
	private boolean centralSpawnerIsSingleUse = true;
	private boolean spawnersAboveBuildings = true;
	private boolean spawnersAreSingleUse = true;
	private boolean makeSpaceForBuildings = true;
	private Block bridgeBlock = Blocks.NETHER_BRICK;
	private Block floorBlock = Blocks.LAVA;
	private Block airBlockForPocket = Blocks.AIR;
	
	private String spawnerMobName = "minecraft:ghast";
	private String centralSpawnerMobName = "minecraft:wither_boss";
	
	protected File buildingFolder;
	protected File centralBuildingsFolder;
	
	//Values for generator
	private int longestSide = -1;
	private int structCount = 0;

	public ClassicNetherCity(File configFile) {
		super(configFile);
		Properties prop = loadConfig(configFile);
		if(prop != null) {
			
			minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsX", 5);
			maxRowsX = PropertyFileHelper.getIntProperty(prop, "maxRowsX", 7);
			minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsZ", 5);
			maxRowsZ = PropertyFileHelper.getIntProperty(prop, "maxRowsZ", 7);
			posY = PropertyFileHelper.getIntProperty(prop, "posY", 31);
			heightY = PropertyFileHelper.getIntProperty(prop, "height", 40);
			
			//singleAirPocketsForHouses = PropertyFileHelper.getBooleanProperty(prop, "singleAirPocketsForHouses", false);
			spawnersAboveBuildings = PropertyFileHelper.getBooleanProperty(prop, "spawnersAboveBuildings", true);
			makeSpaceForBuildings = PropertyFileHelper.getBooleanProperty(prop, "createAirPocket", true);
			specialUseForCentralBuilding = PropertyFileHelper.getBooleanProperty(prop, "centralBuildingIsSpecial", true);
			spawnersAreSingleUse = PropertyFileHelper.getBooleanProperty(prop, "spawnersAreSingleUse", false);
			centralSpawnerIsSingleUse = PropertyFileHelper.getBooleanProperty(prop, "centralSpawnerIsSingleUse", true);
			
			spawnerMobName = prop.getProperty("spawnerMob", "minecraft:ghast");
			centralSpawnerMobName = prop.getProperty("centralSpawnerMob", "minecraft:wither_boss");
			
			bridgeSizeMultiplier = PropertyFileHelper.getDoubleProperty(prop, "bridgelengthmultiplier", 1.2D);
			
			bridgeBlock = PropertyFileHelper.getBlockProperty(prop, "streetblock", Blocks.NETHER_BRICK);
			floorBlock = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.LAVA);
			airBlockForPocket = PropertyFileHelper.getBlockProperty(prop, "airPocketBlock", Blocks.AIR);
			
			buildingFolder = PropertyFileHelper.getFileProperty(prop, "structureFolder", "nether_city_buildings");
			centralBuildingsFolder = PropertyFileHelper.getFileProperty(prop, "centralStructureFolder", "nether_city_buildings");
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + posY + "  Z: " + z + "  ...");
		if(structCount != buildingFolder.listFiles().length) {
			for(File f : buildingFolder.listFiles()) {
				CQStructure cqs = new CQStructure(f, null, 0, 0, false);
				
				if(cqs.getSizeX() > longestSide) {
					longestSide = cqs.getSizeX();
				}
				
				if(cqs.getSizeZ() > longestSide) {
					longestSide = cqs.getSizeZ();
				}
			}
			structCount = buildingFolder.listFiles().length;
		}
		
		IDungeonGenerator generator = new NetherCityGenerator(this);
		generator.generate(world, chunk, x, posY, z);
		
		System.out.println("Generated " + this.getDungeonName() + " at X: " + x + "  Y: " + posY + "  Z: " + z);
	}
	
	public int getCaveHeight() {
		return this.heightY;
	}
	
	public Block getBridgeBlock() {
		return this.bridgeBlock;
	}
	
	public Block getFloorBlock() {
		return this.floorBlock;
	}
	
	public Block getAirPocketBlock() {
		return this.airBlockForPocket;
	}
	
	public int getXRows() {
		return DungeonGenUtils.getIntBetweenBorders(minRowsX, maxRowsX);
	}
	public int getZRows() {
		return DungeonGenUtils.getIntBetweenBorders(minRowsZ, maxRowsZ);
	}

	public int getLongestSide() {
		return longestSide;
	}
	
	public int getDistanceBetweenBuildingCenters() {
		return (new Double(longestSide * bridgeSizeMultiplier)).intValue();
	}
	
	/*public boolean useSingleAirPocketsForHouses() {
		return this.singleAirPocketsForHouses;
	}*/
	
	public boolean placeSpawnersAboveBuildings() {
		return spawnersAboveBuildings;
	}

	public boolean centralBuildingIsSpecial() {
		return specialUseForCentralBuilding;
	}
	
	public boolean centralSpawnerIsSingleUse() {
		return centralSpawnerIsSingleUse;
	}
	
	public boolean spawnersAreSingleUse() {
		return spawnersAreSingleUse;
	}
	
	public boolean makeSpaceForBuildings() {
		return makeSpaceForBuildings;
	}
	
	private File getBuildingFolder() {
		return buildingFolder;
	}
	public File getRandomBuilding() {
		return getStructureFileFromDirectory(getBuildingFolder());
	}
	private File getCentralBuildingFolder() {
		return centralBuildingsFolder;
	}
	public File getRandomCentralBuilding() {
		return getStructureFileFromDirectory(getCentralBuildingFolder());
	}
	
	public ResourceLocation getSpawnerMob() {
		String[] bossString = this.spawnerMobName.split(":");
		
		return new ResourceLocation(bossString[0], bossString[1]);
	}
	
	public ResourceLocation getCentralSpawnerMob() {
		String[] bossString = this.centralSpawnerMobName.split(":");
		
		return new ResourceLocation(bossString[0], bossString[1]);
	}
}
