package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FloatingNetherCityGenerator implements IDungeonGenerator {

	private FloatingNetherCity dungeon;
	private int islandCount = 1;
	
	//This needs to calculate async (island blocks, chain blocks, air blocks)
	
	public FloatingNetherCityGenerator(FloatingNetherCity generator) {
		this.dungeon = generator;
		this.islandCount = this.dungeon.getBuildingCount(new Random());
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Calculates the positions and creates the island objects
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Radius = sqrt(((Longer side of building) / 2)^2 *2) +5
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Builds the platforms
		// Builds the chains
		
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
	
	//Constructs an Island in this shape:
	/*						Dec  Rad
	 * ####################  0    10
	 *  ##################   1    9
	 *    ##############     2    7
	 *       ########        3    4
	 *           
	 */
	private void buildPlatform(BlockPos center, int radius, World world) {
		List<BlockPos> blocks = new ArrayList<>();
		int decrementor = 0;
		int rad = radius;
		while(decrementor < (rad /2)) {
			rad -= decrementor;
			
			for(int iX = -rad; iX <= rad; iX++) {
				for(int iZ = -rad; iZ <= rad; iZ++) {
					if(DungeonGenUtils.isInsideCircle(iX, iZ, rad, center)) {
						blocks.add(center.add(iX, -decrementor, iZ));
					}
				}
			}
			
			decrementor++;
		}
		
		DungeonGenUtils.passListWithBlocksToThreads(blocks, dungeon.getIslandBlock(), world, 100);
	}

}
