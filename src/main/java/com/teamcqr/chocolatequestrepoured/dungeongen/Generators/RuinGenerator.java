package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RuinGenerator implements IDungeonGenerator{

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		//Builds support platform
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		//Will generate the structure and will age the blocks and miss some and remove torches
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		//creates random explosions
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

}
