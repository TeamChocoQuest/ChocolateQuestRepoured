package com.teamcqr.chocolatequestrepoured.util;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;

public class DungeonGenUtils {
	
	public static int getHighestYAt(Chunk chunk, int x, int z, boolean ignoreWater) {
		int y = 255;
		IBlockState block = chunk.getBlockState(x, y, z);
		if(ignoreWater) {
			while(block == Blocks.AIR.getDefaultState() || block == Blocks.WATER.getDefaultState() || block == Blocks.FLOWING_WATER.getDefaultState()) {
				y--;
			}
		} else {
			while(block == Blocks.AIR.getDefaultState()) {
				y--;
			}
		}
		
		return y;
	}
	
	public static boolean PercentageRandom(int number, long seed) {
		Random rdm = new Random();
		rdm.setSeed(seed);
		int rdmNmbr = rdm.nextInt(100) +1;
		if(number >= rdmNmbr) {
			return true;
		}
		return false;
	}
	
	public static boolean PercentageRandom(double number, long seed) {
		return PercentageRandom(number *100, seed);
	}
}
