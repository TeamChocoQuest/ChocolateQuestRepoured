package com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts;

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
		//Doesnt really need a constructor
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER.getWallTopY() - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		int startX = chunkX *16;
		int startZ = chunkZ *16;
		
		int[] xValues = new int[] {0,1,6,7};
		
		List<BlockPos> railingBlocks = new ArrayList<BlockPos>();
		for(int y = 0; y <= 7; y++) {
			//South Side 1
			for(int z = 2; z <= 3; z++) {
				for(int x : xValues) {
					if(isBiggerPart(x)) {
						if(y < 3 && z == 3) {
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						} else if(y >= 3) {							
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						}
					} else if(y >= 4 && z == 3 && y <= 6) {
						railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
						railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
					}
				}
			}
			//North Side
			for(int z = 12; z <= 13; z++) {
				for(int x : xValues) {
					if(isBiggerPart(x)) {
						if(y < 3 && z == 12) {
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						} else if(y >= 3) {							
							railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
							railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
						}
					} else if(y >= 4 && z == 12 && y <= 6) {
						railingBlocks.add(new BlockPos(startX + x*2, getTopY() +y, startZ +z));
						railingBlocks.add(new BlockPos(startX + x*2 +1, getTopY() +y, startZ +z));
					}
				}
			}
		}
		
		List<BlockPos> doorwayBlocks = new ArrayList<BlockPos>();
		//Builds the "doorway"
		for(int y = 6; y <= 9; y++) {
			for(int z = 6; z <= 9; z++) {
				for(int x = 4; x <= 11; x++) {
					if(y < 9) {
						doorwayBlocks.add(new BlockPos(startX +x, getTopY() +y, startZ + z));
					} else if(z == 7 || z == 8) {
						doorwayBlocks.add(new BlockPos(startX +x, getTopY() +y, startZ + z));
					}
				}
			}
		}
		if(!doorwayBlocks.isEmpty()) {
			for(BlockPos pos : doorwayBlocks) {
				world.setBlockToAir(pos);
			}
		}
		
		
		if(!railingBlocks.isEmpty()) {
			for(BlockPos pos : railingBlocks) {
				world.setBlockState(pos, Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE));
			}
		}
	}
	
	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		if(xAsChunkRelativeCoord %2 == 0 || xAsChunkRelativeCoord == 0) {
			return true;
		}
		return false;
	}
}
