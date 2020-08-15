package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorGuardedStructure;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonGuardedCastle extends DungeonBase {

	private File structureFolder;
	private File centerStructureFolder;

	private int minBuildings = 7;
	private int maxBuilding = 14;

	private int minDistance = 15;
	private int maxDistance = 30;

	private boolean buildPaths = true;
	private boolean placeInCircle = false;
	private IBlockState pathBlock = Blocks.GRASS_PATH.getDefaultState();

	public DungeonGuardedCastle(String name, Properties prop) {
		super(name, prop);

		this.structureFolder = PropertyFileHelper.getFileProperty(prop, "structurefolder", "village_buildings");

		this.centerStructureFolder = PropertyFileHelper.getFileProperty(prop, "centerstructurefolder", "village_centers");
		this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
		this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);

		this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
		this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);

		this.placeInCircle = PropertyFileHelper.getBooleanProperty(prop, "circle", false);

		this.buildPaths = PropertyFileHelper.getBooleanProperty(prop, "buildroads", true);

		this.pathBlock = PropertyFileHelper.getBlockStateProperty(prop, "pathblock", Blocks.GRASS_PATH.getDefaultState());
	}

	@Override
	public AbstractDungeonGenerator<DungeonGuardedCastle> createDungeonGenerator(World world, int x, int y, int z) {
		return new GeneratorGuardedStructure(world, new BlockPos(x, y, z), this);
	}

	public int getMinDistance() {
		return this.minDistance;
	}

	public int getMaxDistance() {
		return this.maxDistance;
	}

	public boolean buildPaths() {
		return this.buildPaths;
	}

	public boolean placeInCircle() {
		return this.placeInCircle;
	}

	public IBlockState getPathMaterial() {
		return this.pathBlock;
	}

	public int getMinBuildings() {
		return this.minBuildings;
	}

	public int getMaxBuilding() {
		return this.maxBuilding;
	}

	public File getStructureFolder() {
		return this.structureFolder;
	}

	public File getCenterStructureFolder() {
		return this.centerStructureFolder;
	}

}
