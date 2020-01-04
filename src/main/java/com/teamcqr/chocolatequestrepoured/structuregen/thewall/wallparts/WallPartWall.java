package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WallPartWall implements IWallPart {

	public WallPartWall() {
		// I dont think this needs a constructor
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER_INSTANCE.getWallTopY() - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {

		int startX = chunkX * 16;
		int startZ = chunkZ * 16;

		// All the calculated block positions are stored within these lists
		List<BlockPos> outerBlocks = new ArrayList<BlockPos>();
		List<BlockPos> innerBlocks = new ArrayList<BlockPos>();

		// Calculates all the block positions
		for (int y = this.getLowerY(world, chunk); y <= this.getTopY(); y++) {
			for (int z = 4; z < 12; z++) {
				for (int x = 0; x < 16; x++) {
					BlockPos pos = new BlockPos(startX + x, y, startZ + z);
					if (y == this.getTopY()) {
						outerBlocks.add(pos);
					} else if ((z >= 6 && z <= 9)) {
						innerBlocks.add(pos);
					} else {
						outerBlocks.add(pos);
					}
				}
			}
		}

		// Places the blocks at the calculated positions
		if (!outerBlocks.isEmpty() && !innerBlocks.isEmpty()) {
			// Inner Obsidian core
			/*
			 * for(BlockPos pos : innerBlocks) {
			 * world.setBlockState(pos, Reference.CONFIG_HELPER.wallHasObsiCore() ? Blocks.OBSIDIAN.getDefaultState() : Blocks.STONEBRICK.getDefaultState());
			 * }
			 */
			final List<BlockPos> posL = new ArrayList<BlockPos>(innerBlocks);
			innerBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {

				@Override
				public void run() {

					for (BlockPos p : posL) {
						world.setBlockState(p, Reference.CONFIG_HELPER_INSTANCE.wallHasObsiCore() ? Blocks.OBSIDIAN.getDefaultState() : Blocks.STONEBRICK.getDefaultState());
					}

				}
			});
			// Outer Stoneblock cover
			/*
			 * for(BlockPos pos : outerBlocks) {
			 * world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState());
			 * }
			 */
			final List<BlockPos> posL2 = new ArrayList<BlockPos>(outerBlocks);
			outerBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {

				@Override
				public void run() {

					for (BlockPos p : posL2) {
						world.setBlockState(p, Blocks.STONEBRICK.getDefaultState());
					}

				}
			});
		}

	}

}
