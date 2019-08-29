package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.util.Reference;

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
		return Reference.CONFIG_HELPER_INSTANCE.getWallTopY();
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		
		int startX = chunkX *16;
		int startZ = chunkZ *16;
		
		List<BlockPos> innerBlocks = new ArrayList<BlockPos>();
		List<BlockPos> outerWallBlocks = new ArrayList<BlockPos>();
		List<BlockPos> outerTowerBlocks = new ArrayList<BlockPos>();
		
		for(int y = getLowerY(world, chunk); y <= getTopY(); y++) {
			for(int x = 0; x < 16; x++) {
				for(int z = 0; z < 16; z++) {
					//Wall Rest, not the tower
					if((z >= 4 && z < 12) && (x <= 4 || x >= 12)) {
						if(y <= getTopY() -7) {
							BlockPos wallPos = new BlockPos(startX +x, y, startZ +z);
							if((z >= 6 && z <= 9) && y < (getTopY() -7)) {
								innerBlocks.add(wallPos);
							} else {
								outerWallBlocks.add(wallPos);
							}
						}
					} 
					//Tower itself
					//Obsidian core
					if(((z >= 6 && z <= 9) && (y <= getTopY() -8)) || (((x >=6 && x <= 9) && (z >=2 && z <= 13)) && y < getTopY() -7)) {
						innerBlocks.add(new BlockPos(startX +x, y, startZ +z));
					} else {
						//Wall outer blocks
						if((x >= 4 && x <= 11)) {
							outerTowerBlocks.add(new BlockPos(startX +x, y, startZ +z));
						}
					}
					
					
				}
			}
		}
		
		//Places the blocks at the calculated positions
		if(!outerWallBlocks.isEmpty() && !outerTowerBlocks.isEmpty() && !innerBlocks.isEmpty()) {
			//Inner Obsidian core
			/*for(BlockPos pos : innerBlocks) {
				world.setBlockState(pos, Reference.CONFIG_HELPER.wallHasObsiCore() ? Blocks.OBSIDIAN.getDefaultState() : Blocks.STONEBRICK.getDefaultState());
			}*/
			final List<BlockPos> posL = new ArrayList<BlockPos>(innerBlocks);
			innerBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
				
				@Override
				public void run() {
					
					for(BlockPos p : posL) {
						world.setBlockState(p, Reference.CONFIG_HELPER_INSTANCE.wallHasObsiCore() ? Blocks.OBSIDIAN.getDefaultState() : Blocks.STONEBRICK.getDefaultState());
					}
					
				}
			});
			//Outer Stoneblock cover
			/*for(BlockPos pos : outerWallBlocks) {
				world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState());
			}*/
			final List<BlockPos> posL2 = new ArrayList<BlockPos>(outerWallBlocks);
			outerWallBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
				
				@Override
				public void run() {
					
					for(BlockPos p : posL2) {
						world.setBlockState(p, Blocks.STONEBRICK.getDefaultState());
					}
					
				}
			});
			//Outer andesite blocks
			/*for(BlockPos pos : outerTowerBlocks) {
				world.setBlockState(pos, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH));
			}*/
			final List<BlockPos> posL3 = new ArrayList<BlockPos>(outerTowerBlocks);
			outerTowerBlocks.clear();
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
				
				@Override
				public void run() {
					
					for(BlockPos p : posL3) {
						world.setBlockState(p, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH));
					}
					
				}
			});
		}
	}

}
