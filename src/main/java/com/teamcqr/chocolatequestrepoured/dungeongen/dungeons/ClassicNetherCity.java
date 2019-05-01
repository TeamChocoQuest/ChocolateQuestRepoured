package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class ClassicNetherCity extends DungeonBase {
	
	private int rowsX = 5;
	private int rowsZ = 5;
	private int heightY = 31;
	private Block bridgeBlock = Blocks.NETHER_BRICK;
	private Block floorBlock = Blocks.LAVA;
	protected File buildingFolder;

	public ClassicNetherCity(File configFile) {
		super(configFile);
	}
	
	public int getY() {
		return this.heightY;
	}
	
	public Block getBridgeBlock() {
		return this.bridgeBlock;
	}
	
	public Block getFloorBlock() {
		return this.floorBlock;
	}
	
	public int getXRows() {
		return this.rowsX;
	}
	public int getZRows() {
		return this.rowsZ;
	}

}
