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

	public void generate(Random random, World world, int i, int j, int k, int sizeX, int sizeZ) {
		System.out.println("Trying to construct support platform...");
		long seed = WorldDungeonGenerator.getSeed(world, i, k);
		Perlin3D p = new Perlin3D(seed, 8, random);
		Perlin3D p2 = new Perlin3D(seed, 32, random);

		int wallSize = 8;
		sizeX += wallSize * 2;
		sizeZ += wallSize * 2;

		i -= wallSize;
		k -= wallSize;

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
		
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				int maxHeight = j - 1 -  world.getTopSolidOrLiquidBlock(new BlockPos(x+i,0,z+k)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)), x + i, z + k, true);
				int posY = world.getTopSolidOrLiquidBlock(new BlockPos(x+i,0,z+k)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);
				int topY = posY +1;
				for (int y = 0; y <= maxHeight; y++) {
					if ((x > wallSize) && (z > wallSize) && (x < sizeX - wallSize) && (z < sizeZ - wallSize)) {
						world.setBlockState(new BlockPos(i + x, posY, k + z), this.structureBlock.getDefaultState());
						topY = posY;
					} else {
						float noiseVar = (y - maxHeight) / (Math.max(1, maxHeight) * 1.5F);

						int tWallSize = wallSize;
						noiseVar += Math.max(0.0F, (tWallSize - x) / 8.0F);
						noiseVar += Math.max(0.0F, (tWallSize - (sizeX - x)) / 8.0F);

						noiseVar += Math.max(0.0F, (tWallSize - z) / 8.0F);
						noiseVar += Math.max(0.0F, (tWallSize - (sizeZ - z)) / 8.0F);
						double value = (p.getNoiseAt(x + i, y, z + k) + p2.getNoiseAt(x + i, y, z + k) + noiseVar)/ 3.0D;
						if (value < 0.5D)
							world.setBlockState(new BlockPos(i + x, posY + y, k + z),this.structureBlock.getDefaultState());
						topY = posY +y;
					}
				}
				maxHeight = world.getTopSolidOrLiquidBlock(new BlockPos(x+i,0,z+k)).getY();//DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),x + i, z + k, true);// world.getTopSolidOrLiquidBlock(new BlockPos(x + i, 0, z + k)).getY();
				if (maxHeight <= j) {
					world.setBlockState(new BlockPos(i + x, topY +1, k + z),
							this.structureTopBlock.getDefaultState());
				}
			}
		}
	}

}
