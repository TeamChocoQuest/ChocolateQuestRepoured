package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

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
		return CQRConfig.wall.topY - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, List<List<? extends IStructure>> lists) {
		int startX = chunkX * 16;
		int startZ = chunkZ * 16;

		int[] xValues = new int[] { 0, 1, 6, 7 };
		int[] zValues = new int[] { 2, 3, 12, 13 };
		
		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		ExtendedBlockStatePart.ExtendedBlockState stateBlock = new ExtendedBlockState(Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockStoneSlab.SEAMLESS, true), null);
		ExtendedBlockStatePart.ExtendedBlockState stateAir = new ExtendedBlockState(Blocks.AIR.getDefaultState(), null);

		// Normal railing
		for (int y = 0; y <= 7; y++) {
			for (int z : zValues) {
				for (int x : xValues) {
					if (this.isBiggerPart(x)) {
						// the check is needed to specify wether its the north or south side of the wall (greater than 3 is north side)
						if (y < 3 && z == (z > 3 ? 12 : 3)) {
							stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
							stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
						} else if (y >= 3) {
							// This is for the "thick" part of the bigger railing
							stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
							stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
						}
						// This builds the lower pillars, again the check is needed to identify the side
					} else if (y >= 4 && z == (z > 3 ? 12 : 3) && y <= 6) {
						stateMap.put(new BlockPos(startX + x * 2, this.getTopY() + y, startZ + z), stateBlock);
						stateMap.put(new BlockPos(startX + x * 2 + 1, this.getTopY() + y, startZ + z), stateBlock);
					}
				}
			}
		}

		// Builds the "doorway"
		for (int y = 6; y <= 9; y++) {
			for (int z = 6; z <= 9; z++) {
				for (int x = 4; x <= 11; x++) {
					if (y < 9) {
						stateMap.put(new BlockPos(startX + x, this.getTopY() + y, startZ + z), stateAir);
					} else if (z == 7 || z == 8) {
						// This is for the thinner gap at the top
						stateMap.put(new BlockPos(startX + x, this.getTopY() + y, startZ + z), stateAir);
					}
				}
			}
		}
		
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		if (xAsChunkRelativeCoord % 2 == 0 || xAsChunkRelativeCoord == 0) {
			return true;
		}
		return false;
	}
}
