package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorGuardedStructure;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

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

	public DungeonGuardedCastle(String name, Properties prop) {
		super(name, prop);

		this.structureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "structurefolder", "village_buildings");

		this.centerStructureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "centerstructurefolder", "village_centers");
		this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
		this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);

		this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
		this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);
	}

	@Override
	public AbstractDungeonGenerator<DungeonGuardedCastle> createDungeonGenerator(World world, int x, int y, int z, Random rand) {
		return new GeneratorGuardedStructure(world, new BlockPos(x, y, z), this, rand);
	}

	public int getMinDistance() {
		return this.minDistance;
	}

	public int getMaxDistance() {
		return this.maxDistance;
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

	public boolean rotateDungeon() {
		//TODO
		return false;
	}

}
