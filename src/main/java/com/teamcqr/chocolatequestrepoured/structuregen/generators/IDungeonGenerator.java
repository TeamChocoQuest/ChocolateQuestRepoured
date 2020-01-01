package com.teamcqr.chocolatequestrepoured.structuregen.generators;

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
		/*int median = 0;
		int cant = 0;
		for(int iX = 0; iX < x; iX++) {
			for(int iZ = 0; iZ < z; iZ++) {
				int height = world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY();
				median += height;
				cant++;
			}
		}
		y = median /cant;*/
		if(world.isRemote) {
			return;
		}
		
		preProcess(world, chunk, x, y, z);
		buildStructure(world, chunk, x, y, z);
		postProcess(world, chunk, x, y, z);
		fillChests(world, chunk, x, y, z);
		placeSpawners(world, chunk, x, y, z);
		placeCoverBlocks(world, chunk, x, y, z);
	}

}
