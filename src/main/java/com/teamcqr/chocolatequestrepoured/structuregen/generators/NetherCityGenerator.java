package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class NetherCityGenerator implements IDungeonGenerator {

	private ClassicNetherCity dungeon;
	
	private Set<BlockPos> gridPositions = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsX = new HashSet<>();
	private Set<BlockPos> bridgeBuilderStartPositionsZ = new HashSet<>();
	
	private Set<BlockPos> bridgeBlocks = new HashSet<>();
	private Set<BlockPos> platformBlocks = new HashSet<>();
	private Set<BlockPos> lavaBlocks = new HashSet<>();
	
	private int minX;
	private int maxX;
	private int minZ;
	private int maxZ;
	
	public NetherCityGenerator(ClassicNetherCity dungeon) {
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Calculate the grid for the buildings
		int rowsX = dungeon.getXRows();
		int rowsZ = dungeon.getZRows();
		
		rowsX /= 2;
		rowsZ /= 2;
		
		minX = x -(rowsX * dungeon.getDistanceBetweenBuildingCenters());
		minZ = z -(rowsZ * dungeon.getDistanceBetweenBuildingCenters());
		maxX = x +(rowsX * dungeon.getDistanceBetweenBuildingCenters());
		maxZ = z +(rowsZ * dungeon.getDistanceBetweenBuildingCenters());
		
		//This constructs the grid around the center
		for(int iX = -rowsX; iX <= rowsX; iX++) {
			for(int iZ = -rowsZ; iZ <= rowsZ; iZ++) {
				
				if(iX == 0 && iZ == 0) {
					//TODO: Place for nether dragon to spawn with a certain chance
				}
				
				BlockPos p = new BlockPos(x + (iX * dungeon.getDistanceBetweenBuildingCenters()), y, z +(iZ * dungeon.getDistanceBetweenBuildingCenters()));
				gridPositions.add(p);
				//Bridge starter positions, in total there will be rowsX +rowsZ -1 bridges
				if(iX == 0) {
					bridgeBuilderStartPositionsZ.add(p);
				}
				if(iZ == 0) {
					bridgeBuilderStartPositionsX.add(p);
				}
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Dig out the big air pocket or the small ones
		//Build the roads / bridges and the floors
		
		//Bridges from south to north
		for(BlockPos pX : bridgeBuilderStartPositionsX) {
			for(int iZ = minZ; iZ <= maxZ; iZ++) {
				BlockPos pC = new BlockPos(pX.getX(), pX.getY(), iZ);
				bridgeBlocks.add(pC);
				bridgeBlocks.add(pC.east());
				bridgeBlocks.add(pC.west());
			}
		}
		//Bridges from west to east
		for(BlockPos pZ : bridgeBuilderStartPositionsZ) {
			for(int iX = minX; iX <= maxX; iX++) {
				BlockPos pC = new BlockPos(iX, pZ.getY(), pZ.getZ());
				bridgeBlocks.add(pC);
				bridgeBlocks.add(pC.north());
				bridgeBlocks.add(pC.south());
			}
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//Place the buildings
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Maybe place some ghast spawners over the city and a nether dragon? -> Complete factory first
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE
	}

}
