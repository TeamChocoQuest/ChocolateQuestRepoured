package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.FloatingNetherCity;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FloatingNetherCityGenerator implements IDungeonGenerator {

	private FloatingNetherCity generator;
	private int islandCount = 1;
	
	
	public FloatingNetherCityGenerator(FloatingNetherCity generator) {
		this.generator = generator;
		this.islandCount = this.generator.getBuildingCount(new Random());
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Calculates the positions
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Builds the platforms
		// Builds the bridges and chains
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// Builds the structures

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Unused

	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Unused

	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// Unused  or maybe later implemented

	}

}
