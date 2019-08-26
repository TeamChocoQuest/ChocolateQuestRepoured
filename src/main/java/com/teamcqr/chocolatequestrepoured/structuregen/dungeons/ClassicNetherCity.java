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
	private boolean singleAirPocketsForHouses = false;
	private Block bridgeBlock = Blocks.NETHER_BRICK;
	private Block floorBlock = Blocks.LAVA;
	protected File buildingFolder;
	
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
			
			singleAirPocketsForHouses = PropertyFileHelper.getBooleanProperty(prop, "singleAirPocketsForHouses", false);
			
			bridgeSizeMultiplier = PropertyFileHelper.getDoubleProperty(prop, "bridgelengthmultiplier", 1.2D);
			
			bridgeBlock = PropertyFileHelper.getBlockProperty(prop, "streetblock", Blocks.NETHER_BRICK);
			floorBlock = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.LAVA);
			
			buildingFolder = PropertyFileHelper.getFileProperty(prop, "structurefolder", "nether_city_buildings");
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		if(structCount != buildingFolder.listFiles().length) {
			for(File f : buildingFolder.listFiles()) {
				CQStructure cqs = new CQStructure(f, false);
				
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
	
	public boolean useSingleAirPocketsForHouses() {
		return this.singleAirPocketsForHouses;
	}
}
