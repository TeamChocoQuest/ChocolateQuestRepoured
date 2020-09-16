package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorGridCity;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonGridCity extends DungeonBase {

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
	private IBlockState bridgeBlock = Blocks.NETHER_BRICK.getDefaultState();
	private IBlockState floorBlock = Blocks.LAVA.getDefaultState();
	private IBlockState airBlockForPocket = Blocks.AIR.getDefaultState();

	protected File buildingFolder;
	protected File centralBuildingsFolder;

	public DungeonGridCity(String name, Properties prop) {
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

		this.bridgeBlock = PropertyFileHelper.getBlockStateProperty(prop, "streetblock", Blocks.NETHER_BRICK.getDefaultState());
		this.floorBlock = PropertyFileHelper.getBlockStateProperty(prop, "floorblock", Blocks.LAVA.getDefaultState());
		this.airBlockForPocket = PropertyFileHelper.getBlockStateProperty(prop, "airPocketBlock", Blocks.AIR.getDefaultState());

		this.buildingFolder = PropertyFileHelper.getStructureFolderProperty(prop, "structureFolder", "nether_city_buildings");
		this.centralBuildingsFolder = PropertyFileHelper.getStructureFolderProperty(prop, "centralStructureFolder", "nether_city_buildings");
	}

	@Override
	public void generate(World world, int x, int z, Random rand) {
		this.generate(world, x, this.posY, z, rand);
	}

	@Override
	public AbstractDungeonGenerator<DungeonGridCity> createDungeonGenerator(World world, int x, int y, int z, Random rand) {
		return new GeneratorGridCity(world, new BlockPos(x, y, z), this, rand);
	}

	public int getCaveHeight() {
		return this.heightY;
	}

	public IBlockState getBridgeBlock() {
		return this.bridgeBlock;
	}

	public IBlockState getFloorBlock() {
		return this.floorBlock;
	}

	public IBlockState getAirPocketBlock() {
		return this.airBlockForPocket;
	}

	public int getXRows(Random rand) {
		return DungeonGenUtils.randomBetween(this.minRowsX, this.maxRowsX, rand);
	}

	public int getZRows(Random rand) {
		return DungeonGenUtils.randomBetween(this.minRowsZ, this.maxRowsZ, rand);
	}

	/*
	 * public boolean useSingleAirPocketsForHouses() { return this.singleAirPocketsForHouses; }
	 */

	public boolean centralBuildingIsSpecial() {
		return this.specialUseForCentralBuilding;
	}

	public boolean makeSpaceForBuildings() {
		return this.makeSpaceForBuildings;
	}

	public File getBuildingFolder() {
		return this.buildingFolder;
	}

	public File getRandomBuilding(Random rand) {
		return this.getStructureFileFromDirectory(this.getBuildingFolder(), rand);
	}

	public File getCentralBuildingFolder() {
		return this.centralBuildingsFolder;
	}

	public File getRandomCentralBuilding(Random rand) {
		return this.getStructureFileFromDirectory(this.getCentralBuildingFolder(), rand);
	}

	public double getBridgeSizeMultiplier() {
		return this.bridgeSizeMultiplier;
	}

}
