package team.cqr.cqrepoured.world.structure.generation.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.generators.GeneratorGridCity;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonGridCity extends DungeonBase {

	private int minRowsX = 5;
	private int maxRowsX = 7;
	private int minRowsZ = 5;
	private int maxRowsZ = 7;
	private int heightY = 31;
	private double bridgeSizeMultiplier = 1.2D;
	// private boolean singleAirPocketsForHouses = false;
	private boolean specialUseForCentralBuilding = false;
	private boolean makeSpaceForBuildings = true;
	private BlockState bridgeBlock = Blocks.NETHER_BRICKS.defaultBlockState();
	private BlockState floorBlock = Blocks.LAVA.defaultBlockState();
	private BlockState airBlockForPocket = Blocks.AIR.defaultBlockState();

	protected File buildingFolder;
	protected File centralBuildingsFolder;

	public DungeonGridCity(String name, Properties prop) {
		super(name, prop);

		this.minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsX", 5);
		this.maxRowsX = PropertyFileHelper.getIntProperty(prop, "maxRowsX", 7);
		this.minRowsX = PropertyFileHelper.getIntProperty(prop, "minRowsZ", 5);
		this.maxRowsZ = PropertyFileHelper.getIntProperty(prop, "maxRowsZ", 7);
		this.heightY = PropertyFileHelper.getIntProperty(prop, "height", 40);

		// singleAirPocketsForHouses = PropertyFileHelper.getBooleanProperty(prop, "singleAirPocketsForHouses", false);
		this.makeSpaceForBuildings = PropertyFileHelper.getBooleanProperty(prop, "createAirPocket", true);
		this.specialUseForCentralBuilding = PropertyFileHelper.getBooleanProperty(prop, "centralBuildingIsSpecial", true);

		this.bridgeSizeMultiplier = PropertyFileHelper.getDoubleProperty(prop, "bridgelengthmultiplier", 1.2D);

		this.bridgeBlock = PropertyFileHelper.getBlockStateProperty(prop, "streetblock", Blocks.NETHER_BRICKS.defaultBlockState());
		this.floorBlock = PropertyFileHelper.getBlockStateProperty(prop, "floorblock", Blocks.LAVA.defaultBlockState());
		this.airBlockForPocket = PropertyFileHelper.getBlockStateProperty(prop, "airPocketBlock", Blocks.AIR.defaultBlockState());

		this.buildingFolder = PropertyFileHelper.getStructureFolderProperty(prop, "structureFolder", "nether_city_buildings");
		this.centralBuildingsFolder = PropertyFileHelper.getStructureFolderProperty(prop, "centralStructureFolder", "nether_city_buildings");
	}

	@Override
	public Collection<StructurePiece> runGenerator(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random) {
		Collection<StructurePiece> pieces = new ArrayList<>();
		pieces.add(new GeneratorGridCity(chunkGenerator, pos, this, random).prepare());
		return pieces;
	}

	public int getCaveHeight() {
		return this.heightY;
	}

	public BlockState getBridgeBlock() {
		return this.bridgeBlock;
	}

	public BlockState getFloorBlock() {
		return this.floorBlock;
	}

	public BlockState getAirPocketBlock() {
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
