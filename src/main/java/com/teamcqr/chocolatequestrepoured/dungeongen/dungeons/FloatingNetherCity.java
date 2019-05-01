package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class FloatingNetherCity extends ClassicNetherCity {

	private int minBuildings;
	private int maxBuildings;
	private int minIslandDistance;
	private int maxIslandDistance;
	private Block islandMaterial;
	private Block chainBlock;
	private Block bridgeBlock;
	private int bridgeChance;
	private boolean buildChains;
	
	public FloatingNetherCity(File configFile) {
		super(configFile);
	}
	
	public boolean doBuildChains() {
		return this.buildChains;
	}
	public int getBridgeChance() {
		return this.bridgeChance;
	}
	public int getBuildingCount(Random random) {
		return DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuildings, random);
	}
	public int getIslandDistance() {
		return DungeonGenUtils.getIntBetweenBorders(this.minIslandDistance, this.maxIslandDistance, new Random());
	}
	public Block getChainBlock() {
		return this.chainBlock;
	}
	public Block getIslandBlock() {
		return this.islandMaterial;
	}
	public Block getBridgeBlock() {
		return this.bridgeBlock;
	}

}
