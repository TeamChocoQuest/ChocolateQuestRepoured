package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

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
		return CQRConfig.wall.topY - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, List<List<? extends IStructure>> lists) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;

		// All the calculated block positions are stored within this map
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		ExtendedBlockStatePart.ExtendedBlockState stateObsidian = new ExtendedBlockState(Blocks.OBSIDIAN.getDefaultState(), null);
		ExtendedBlockStatePart.ExtendedBlockState stateBrick = new ExtendedBlockState(Blocks.STONEBRICK.getDefaultState(), null);
		if (!CQRConfig.wall.obsidianCore) {
			stateObsidian = stateBrick;
		}

		// Calculates all the block positions
		int lowerY = this.getLowerY(world, startX, startZ);
		for (int y = lowerY; y <= this.getTopY(); y++) {
			for (int z = 4; z < 12; z++) {
				for (int x = 0; x < 16; x++) {
					BlockPos pos = new BlockPos(startX + x, y, startZ + z);
					if (y == this.getTopY()) {
						stateMap.put(pos, stateBrick);
					} else if ((z >= 6 && z <= 9)) {
						stateMap.put(pos, stateObsidian);
					} else {
						stateMap.put(pos, stateBrick);
					}
				}
			}
		}

		// Places the blocks at the calculated positions
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));

	}

}
