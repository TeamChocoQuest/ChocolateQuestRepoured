package com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WallPartWall implements IWallPart {

	public WallPartWall() {
		// I dont think this needs a constructor
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER.getWallTopY() - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		// TODO Put all this calculation and block placing into a separate thread...
		
		int startX = chunkX *16;
		int startZ = chunkZ *16;
		
		//All the calculated block positions are stored within these lists
		List<BlockPos> outerBlocks = new ArrayList<BlockPos>();
		List<BlockPos> innerBlocks = new ArrayList<BlockPos>();
		
		//Calculates all the block positions
		for(int y = getLowerY(world, chunk); y <= getTopY(); y++) {
			for(int z = 4; z < 13; z++) {
				for(int x = 0; x < 16; x++) {
					BlockPos pos = new BlockPos(startX +x, y, startZ +z);
					if((z >= 6 && z <= 9) || y == getTopY()) {
						innerBlocks.add(pos);
					} else {
						outerBlocks.add(pos);
					}
				}
			}
		}
		
		//Places the blocks at the calculated positions
		if(!outerBlocks.isEmpty() && !innerBlocks.isEmpty()) {
			//Inner Obsidian core
			for(BlockPos pos : innerBlocks) {
				world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
			}
			//Outer Stoneblock cover
			for(BlockPos pos : outerBlocks) {
				world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState());
			}
		}
		
	}

}
