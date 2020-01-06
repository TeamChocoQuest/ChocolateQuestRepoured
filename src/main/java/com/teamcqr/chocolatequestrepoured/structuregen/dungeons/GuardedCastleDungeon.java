package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GuardedCastleGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class GuardedCastleDungeon extends DungeonBase {

	private File structureFolder;
	private File centerStructureFolder;

	private int minBuildings = 7;
	private int maxBuilding = 14;

	private int minDistance = 15;
	private int maxDistance = 30;

	private boolean buildPaths = true;
	private boolean placeInCircle = false;
	private Block pathBlock = Blocks.GRASS_PATH;

	@Override
	public IDungeonGenerator getGenerator() {
		return new GuardedCastleGenerator(this);
	}

	// DONE: Rewrite this whole file handling as it is unefficient and uses lots of memory which is unnecessary
	public GuardedCastleDungeon(File configFile) {
		super(configFile);
		Properties prop = this.loadConfig(configFile);
		if (prop != null) {
			this.structureFolder = PropertyFileHelper.getFileProperty(prop, "structurefolder", "village_buildings");

			this.centerStructureFolder = PropertyFileHelper.getFileProperty(prop, "centerstructurefolder", "village_centers");
			this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
			this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);

			this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
			// System.out.println("Min Distance: " + minDistance);
			this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);

			this.placeInCircle = PropertyFileHelper.getBooleanProperty(prop, "circle", false);

			this.buildPaths = PropertyFileHelper.getBooleanProperty(prop, "buildroads", true);

			this.pathBlock = PropertyFileHelper.getBlockProperty(prop, "pathblock", Blocks.GRASS_PATH);

			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}

	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		this.dunID = MathHelper.getRandomUUID();

		this.generator = new GuardedCastleGenerator(this);

		int buildings = DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuilding, random);
		for (int i = 0; i < buildings; i++) {
			File building = null;
			int counter = this.structureFolder.listFiles(FileIOUtil.getNBTFileFilter()).length;
			while(building == null) {
				counter--;
				building = this.getStructureFileFromDirectory(this.structureFolder);/* getRandomBuilding(random); */
			}
			((GuardedCastleGenerator) this.generator).addStructure(building);
			building = this.centerStructureFolder;
			building = null;
			counter = this.centerStructureFolder.listFiles(FileIOUtil.getNBTFileFilter()).length;
			while(building == null && counter >= 0) {
				counter--;
				this.getStructureFileFromDirectory(this.centerStructureFolder);
			}
			((GuardedCastleGenerator) this.generator).setCenterStructure(building);
		}
		// Generating it...
		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false) + this.getYOffset();
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		this.generator.generate(world, chunk, x, y, z);
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

	public Block getPathMaterial() {
		return this.pathBlock;
	}

}
