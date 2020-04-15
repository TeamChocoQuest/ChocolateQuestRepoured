package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.NetherCityGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

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
	// private boolean singleAirPocketsForHouses = false;
	private boolean specialUseForCentralBuilding = false;
	private boolean makeSpaceForBuildings = true;
	private Block bridgeBlock = Blocks.NETHER_BRICK;
	private Block floorBlock = Blocks.LAVA;
	private Block airBlockForPocket = Blocks.AIR;

	protected File buildingFolder;
	protected File centralBuildingsFolder;

	// Values for generator
	private int longestSide = -1;
	private int structCount = 0;

	public ClassicNetherCity(String name, Properties prop) {
		super(name, prop);

		this.minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsX", 5);
		this.maxRowsX = PropertyFileHelper.getIntProperty(prop, "maxRowsX", 7);
		this.minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsZ", 5);
		this.maxRowsZ = PropertyFileHelper.getIntProperty(prop, "maxRowsZ", 7);
		this.posY = PropertyFileHelper.getIntProperty(prop, "posY", 31);
		this.heightY = PropertyFileHelper.getIntProperty(prop, "height", 40);

		// singleAirPocketsForHouses = PropertyFileHelper.getBooleanProperty(prop, "singleAirPocketsForHouses", false);
		this.makeSpaceForBuildings = PropertyFileHelper.getBooleanProperty(prop, "createAirPocket", true);
		this.specialUseForCentralBuilding = PropertyFileHelper.getBooleanProperty(prop, "centralBuildingIsSpecial", true);

		this.bridgeSizeMultiplier = PropertyFileHelper.getDoubleProperty(prop, "bridgelengthmultiplier", 1.2D);

		this.bridgeBlock = PropertyFileHelper.getBlockProperty(prop, "streetblock", Blocks.NETHER_BRICK);
		this.floorBlock = PropertyFileHelper.getBlockProperty(prop, "floorblock", Blocks.LAVA);
		this.airBlockForPocket = PropertyFileHelper.getBlockProperty(prop, "airPocketBlock", Blocks.AIR);

		this.buildingFolder = PropertyFileHelper.getFileProperty(prop, "structureFolder", "nether_city_buildings");
		this.centralBuildingsFolder = PropertyFileHelper.getFileProperty(prop, "centralStructureFolder", "nether_city_buildings");
	}

	@Override
	public void generate(World world, int x, int z) {
		this.generate(world, x, this.posY, z);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		if (this.structCount != this.buildingFolder.listFiles().length) {
			for (File f : this.buildingFolder.listFiles()) {
				CQStructure cqs = new CQStructure(f);

				if (cqs.getSize().getX() > this.longestSide) {
					this.longestSide = cqs.getSize().getX();
				}

				if (cqs.getSize().getZ() > this.longestSide) {
					this.longestSide = cqs.getSize().getZ();
				}
			}
			this.structCount = this.buildingFolder.listFiles().length;
		}

		IDungeonGenerator generator = new NetherCityGenerator(this);
		generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
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
		return DungeonGenUtils.getIntBetweenBorders(this.minRowsX, this.maxRowsX);
	}

	public int getZRows() {
		return DungeonGenUtils.getIntBetweenBorders(this.minRowsZ, this.maxRowsZ);
	}

	public int getLongestSide() {
		return this.longestSide;
	}

	public int getDistanceBetweenBuildingCenters() {
		return (new Double(this.longestSide * this.bridgeSizeMultiplier)).intValue();
	}

	/*
	 * public boolean useSingleAirPocketsForHouses() {
	 * return this.singleAirPocketsForHouses;
	 * }
	 */


	public boolean centralBuildingIsSpecial() {
		return this.specialUseForCentralBuilding;
	}

	public boolean makeSpaceForBuildings() {
		return this.makeSpaceForBuildings;
	}

	private File getBuildingFolder() {
		return this.buildingFolder;
	}

	public File getRandomBuilding() {
		return this.getStructureFileFromDirectory(this.getBuildingFolder());
	}

	private File getCentralBuildingFolder() {
		return this.centralBuildingsFolder;
	}

	public File getRandomCentralBuilding() {
		return this.getStructureFileFromDirectory(this.getCentralBuildingFolder());
	}

}
