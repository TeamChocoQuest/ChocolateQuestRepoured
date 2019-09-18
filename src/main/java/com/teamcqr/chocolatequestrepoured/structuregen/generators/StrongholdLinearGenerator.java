package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdLinearGenerator implements IDungeonGenerator {

	public StrongholdLinearGenerator() {
		//Set floor count
		//Set room per floor count
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//calculates the positions for rooms, stairs, bossroom, entrance, entrance stairs
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//places the structures
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//Places the entrance building or staircase
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//Only for entrance
	}

}
