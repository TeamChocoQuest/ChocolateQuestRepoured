package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlateauBuilder {

	// Copied directly from old mod, should be ok, because it is just an
	// implementation of perlin3D

	public PlateauBuilder() {
	}

	Block structureBlock = Blocks.STONE;
	Block structureTopBlock = Blocks.GRASS;

	public int wallSize = 8;

	public void load(Block support, Block top) {
		structureBlock = support;
		structureTopBlock = top;
	}

	public void generate(Random random, World world, int startX, int startY, int startZ, int sizeX, int sizeZ) {
		//generateCubeUnderStructure(startX, startY, startZ, sizeX, sizeZ, world);
		System.out.println("Trying to construct support platform...");
		//Chunk startChunk = world.getChunkFromBlockCoords(new BlockPos(startX, startY, startZ));
		//long seed = WorldDungeonGenerator.getSeed(world, startChunk.x, startChunk.z);
		Perlin3D p = new Perlin3D(world.getSeed(), 8, random);
		Perlin3D p2 = new Perlin3D(world.getSeed(), 32, random);

		int wallSize = 8;
		sizeX += wallSize * 2;
		sizeZ += wallSize * 2;

		startX -= wallSize;
		startZ -= wallSize;

		/*int platFormHeight = 0;
	    for (int x = 0; x < sizeX; x++)
	    {
	      for (int z = 0; z < sizeZ; z++)
	      {
	        int maxHeight = j - world.getTopSolidOrLiquidBlock(new BlockPos(x + i, 0, z + k)).getY();
	        if (maxHeight > platFormHeight) {
	        	platFormHeight = maxHeight;
	        }
	      }
	    }
	    platFormHeight = Math.max(1, platFormHeight);
		
		for(int x = 0; x <sizeX; x++) {
			for(int z = 0; z <sizeZ; z++) {
				int maxHeight = j -world.getTopSolidOrLiquidBlock(new BlockPos(x +i, 0, z +k)).getY();
				int maxY = 0;
				
				for(int y = platFormHeight - maxHeight; y < platFormHeight; y++) {
					if((x > wallSize) && (z > wallSize) && (x < sizeX - wallSize) && (z < sizeZ - wallSize)) {
						world.setBlockState(new BlockPos(x,j -y,z), this.structureBlock.getDefaultState());
						maxY = y;
					} else {
						float noise = (y -maxHeight) / (Math.max(1, maxHeight) * 1.5F);
						
						int wallSizeTMP = wallSize;
						
						noise += Math.max(0.0F, (wallSizeTMP -x) /wallSize);
						noise += Math.max(0.0F, (wallSizeTMP - (sizeX -x)) /wallSize);
						
						noise += Math.max(0.0F, (wallSizeTMP -z) /wallSize);
						noise += Math.max(0.0F, (wallSizeTMP - (sizeZ -z)) /wallSize);
						
						double noiseValue = (p.getNoiseAt(x +i, y, z +k) +p2.getNoiseAt(x +i, y, z +k) +noise) /3.0D +y /platFormHeight *0.25D;
						if(noiseValue < 0.5D) {
							world.setBlockState(new BlockPos(x,j -y,z), this.structureBlock.getDefaultState());
							maxY = y;
						}
					}
				}
				if(maxHeight <= j) {
					world.setBlockState(new BlockPos(x,j -maxY,z), this.structureTopBlock.getDefaultState());
				}
			}
		}*/
		
		for (int x = 0; x < sizeX; ++x) {
			for (int z = 0; z < sizeZ; ++z) {
				int maxHeight = startY - 1 -  world.getTopSolidOrLiquidBlock(new BlockPos(x+startX,0,z+startZ)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)), x + i, z + k, true);
				int posY = world.getTopSolidOrLiquidBlock(new BlockPos(x+startX,0,z+startZ)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);
				for (int y = 0; y <= maxHeight; ++y) {
					//This generates the "cube" that goes under the structure
					if ((x > wallSize) && (z > wallSize) && (x < sizeX - wallSize) && (z < sizeZ - wallSize)) {
						world.setBlockState(new BlockPos(startX + x, posY +y, startZ + z), this.structureBlock.getDefaultState());
					}
					//This generates the fancy "curved" walls around the cube
					else {
						float noiseVar = (float)(y - maxHeight) / ((float)Math.max(1, maxHeight) * 1.5F);

						noiseVar += Math.max(0.0F, (wallSize - x) / 8.0F);
						noiseVar += Math.max(0.0F, (wallSize - (sizeX - x)) / 8.0F);

						noiseVar += Math.max(0.0F, (wallSize - z) / 8.0F);
						noiseVar += Math.max(0.0F, (wallSize - (sizeZ - z)) / 8.0F);
						double value = (p.getNoiseAt(x + startX, y, z + startZ) + p2.getNoiseAt(x + startX, y, z + startZ) + noiseVar) / 3.0D + y / (maxHeight == 0 ? 1:maxHeight) *0.25D;
						if (value < 0.5D)
							world.setBlockState(new BlockPos(startX + x, posY + y, startZ + z),this.structureBlock.getDefaultState());
					}
				}
				maxHeight = world.getTopSolidOrLiquidBlock(new BlockPos(x+startX,0,z+startZ)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);// world.getTopSolidOrLiquidBlock(new BlockPos(x + i, 0, z + k)).getY();
				if (maxHeight <= startY) {
					world.setBlockState(new BlockPos(startX + x, maxHeight -1, startZ + z),
							this.structureTopBlock.getDefaultState());
				}
			}
		}
	}


}
