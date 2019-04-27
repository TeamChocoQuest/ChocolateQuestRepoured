package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
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
		Perlin3D p = new Perlin3D(world.getSeed(), 8, random);
		Perlin3D p2 = new Perlin3D(world.getSeed(), 32, random);

		int wallSize = 8;
		sizeX += wallSize * 2;
		sizeZ += wallSize * 2;

		i -= wallSize;
		k -= wallSize;

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				int maxHeight = j - 1 - DungeonGenUtils.getHighestYAt(
						world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)), x + i, z + k, true);
				int posY = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),
						x + i, z + k, true);
				for (int y = 0; y <= maxHeight; y++) {
					if ((x > wallSize) && (z > wallSize) && (x < sizeX - wallSize) && (z < sizeZ - wallSize)) {
						world.setBlockState(new BlockPos(i + x, posY, k + z), this.structureBlock.getDefaultState());
					} else {
						float noiseVar = (y - maxHeight) / (Math.max(1, maxHeight) * 1.5F);

						int tWallSize = wallSize;
						noiseVar += Math.max(0.0F, (tWallSize - x) / 8.0F);
						noiseVar += Math.max(0.0F, (tWallSize - (sizeX - x)) / 8.0F);

						noiseVar += Math.max(0.0F, (tWallSize - z) / 8.0F);
						noiseVar += Math.max(0.0F, (tWallSize - (sizeZ - z)) / 8.0F);
						double value = (p.getNoiseAt(x + i, y, z + k) + p2.getNoiseAt(x + i, y, z + k) + noiseVar)
								/ 3.0D;
						if (value < 0.5D)
							world.setBlockState(new BlockPos(i + x, posY + y, k + z),
									this.structureBlock.getDefaultState());
					}
				}
				maxHeight = DungeonGenUtils.getHighestYAt(world.getChunkFromBlockCoords(new BlockPos(x + i, 0, z + k)),
						x + i, z + k, true);// world.getTopSolidOrLiquidBlock(new BlockPos(x + i, 0, z + k)).getY();
				if (maxHeight <= j) {
					world.setBlockState(new BlockPos(i + x, maxHeight - 1, k + z),
							this.structureTopBlock.getDefaultState());
				}
			}
		}
	}

}
