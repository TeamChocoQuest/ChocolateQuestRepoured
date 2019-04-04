package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SimpleCaveGenerator implements IDungeonGenerator {
	
	BlockPos[] floorBlocks = new BlockPos[]{};
	BlockPos[] airBlocks = new BlockPos[]{};
	
	Block floorMat; 
	Block airMat;
	
	public SimpleCaveGenerator(Block floorBlock, Block airBlock) {
		this.floorMat = floorBlock;
		this.airMat = airBlock;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Calculate the positions of the airblocks !ASYNC to save resources!
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Place the blocks into the calculated positions
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Place loot chest
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Place boss spawner
	}

}
