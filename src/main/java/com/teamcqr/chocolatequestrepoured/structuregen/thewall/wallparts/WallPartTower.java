package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WallPartTower implements IWallPart {

	public WallPartTower() {
		// Doesnt really need a constructor
	}

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, List<List<? extends IStructure>> lists) {

		int startX = chunkX * 16;
		int startZ = chunkZ * 16;

		Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
		ExtendedBlockStatePart.ExtendedBlockState stateAndesite = new ExtendedBlockState(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH), null);
		ExtendedBlockStatePart.ExtendedBlockState stateObsidian = new ExtendedBlockState(Blocks.OBSIDIAN.getDefaultState(), null);
		ExtendedBlockStatePart.ExtendedBlockState stateBrick = new ExtendedBlockState(Blocks.STONEBRICK.getDefaultState(), null);
		if(!CQRConfig.wall.obsidianCore) {
			stateObsidian = stateBrick;
		}


		for (int y = this.getLowerY(world, chunk); y <= this.getTopY(); y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					// Wall Rest, not the tower
					if ((z >= 4 && z < 12) && (x <= 4 || x >= 12)) {
						if (y <= this.getTopY() - 7) {
							BlockPos wallPos = new BlockPos(startX + x, y, startZ + z);
							if ((z >= 6 && z <= 9) && y < (this.getTopY() - 7)) {
								stateMap.put(wallPos, stateObsidian);
							} else {
								stateMap.put(wallPos, stateBrick);
							}
						}
					}
					// Tower itself
					// Obsidian core
					if (((z >= 6 && z <= 9) && (y <= this.getTopY() - 8)) || (((x >= 6 && x <= 9) && (z >= 2 && z <= 13)) && y < this.getTopY() - 7)) {
						stateMap.put(new BlockPos(startX + x, y, startZ + z), stateObsidian);
					} else {
						// Wall outer blocks
						if ((x >= 4 && x <= 11)) {
							stateMap.put(new BlockPos(startX + x, y, startZ + z), stateAndesite);
						}
					}

				}
			}
		}

		// Places the blocks at the calculated positions
		lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
	}

}
