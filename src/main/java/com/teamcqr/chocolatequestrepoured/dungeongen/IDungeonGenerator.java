package com.teamcqr.chocolatequestrepoured.dungeongen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public interface IDungeonGenerator { 
	
	void preProcess(World world, Chunk chunk, int x, int y, int z);
	void buildStructure(World world, Chunk chunk, int x, int y, int z);
	void postProcess(World world, Chunk chunk, int x, int y, int z);
	void fillChests(World world, Chunk chunk, int x, int y, int z);
	void placeSpawners(World world, Chunk chunk, int x, int y, int z);
	void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z);
	
	default void generate(World world, Chunk chunk, int x, int y, int z) {
		preProcess(world, chunk, x, y, z);
		buildStructure(world, chunk, x, y, z);
		postProcess(world, chunk, x, y, z);
		fillChests(world, chunk, x, y, z);
		placeSpawners(world, chunk, x, y, z);
		placeCoverBlocks(world, chunk, x, y, z);
	}

}
