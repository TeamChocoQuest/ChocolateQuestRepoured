package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WallPartRailingTower implements IWallPart {

	public WallPartRailingTower() {
		// Doesnt really need a constructor
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER_INSTANCE.getWallTopY() - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		int startX = chunkX * 16;
		int startZ = chunkZ * 16;

		int[] xValues = new int[] { 0, 1, 6, 7 };
		int[] zValues = new int[] { 2, 3, 12, 13 };

		// Normal railing
		List<BlockPos> railingBlocks = new ArrayList<BlockPos>();
		for (int y = 0; y <= 7; y++) {
			for (int z : zValues) {
				for (int x : xValues) {
					if (this.isBiggerPart(x)) {
						// the check is needed to specify wether its the north or south side of the wall (greater than 3 is north side)
						if (y < 3 && z == (z > 3 ? 12 : 3)) {
							railingBlocks.add(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z));
							railingBlocks.add(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z));
						} else if (y >= 3) {
							// This is for the "thick" part of the bigger railing
							railingBlocks.add(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z));
							railingBlocks.add(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z));
						}
						// This builds the lower pillars, again the check is needed to identify the side
					} else if (y >= 4 && z == (z > 3 ? 12 : 3) && y <= 6) {
						railingBlocks.add(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z));
						railingBlocks.add(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z));
					}
				}
			}
		}

		List<BlockPos> doorwayBlocks = new ArrayList<BlockPos>();
		// Builds the "doorway"
		for (int y = 6; y <= 9; y++) {
			for (int z = 6; z <= 9; z++) {
				for (int x = 4; x <= 11; x++) {
					if (y < 9) {
						doorwayBlocks.add(new BlockPos(startX + x, this.getTopY() + y, startZ + z));
					} else if (z == 7 || z == 8) {
						// This is for the thinner gap at the top
						doorwayBlocks.add(new BlockPos(startX + x, this.getTopY() + y, startZ + z));
					}
				}
			}
		}
		if (!doorwayBlocks.isEmpty()) {
			/*
			 * for(BlockPos pos : doorwayBlocks) {
			 * world.setBlockToAir(pos);
			 * }
			 */
			final List<BlockPos> posL = new ArrayList<BlockPos>(doorwayBlocks);
			doorwayBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {

				@Override
				public void run() {

					for (BlockPos p : posL) {
						world.setBlockToAir(p);
					}

				}
			});
		}

		if (!railingBlocks.isEmpty()) {
			/*
			 * for(BlockPos pos : railingBlocks) {
			 * world.setBlockState(pos, Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE));
			 * }
			 */
			final List<BlockPos> posL = new ArrayList<BlockPos>(railingBlocks);
			railingBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {

				@Override
				public void run() {

					for (BlockPos p : posL) {
						world.setBlockState(p, Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockStoneSlab.SEAMLESS, true));
					}

				}
			});
		}
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		if (xAsChunkRelativeCoord % 2 == 0 || xAsChunkRelativeCoord == 0) {
			return true;
		}
		return false;
	}
}
