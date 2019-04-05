package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CavernDungeon;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CavernGenerator implements IDungeonGenerator {
	
	public CavernGenerator() {};
	
	public CavernGenerator(CavernDungeon dungeon) {
		
	}
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

}
