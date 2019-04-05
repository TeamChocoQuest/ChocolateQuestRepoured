package com.teamcqr.chocolatequestrepoured.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;

public class DungeonGenUtils {
	
	public static int getHighestYAt(Chunk chunk, int x, int z, boolean ignoreWater) {
		int y = 255;
		Block block = chunk.getBlockState(x, y, z).getBlock();
		if(ignoreWater) {
			while(Block.isEqualTo(block, Blocks.AIR) || Block.isEqualTo(block, Blocks.WATER) || Block.isEqualTo(block, Blocks.FLOWING_WATER)) {
				y--;
			}
		} else {
			while(Block.isEqualTo(block, Blocks.AIR)) {
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
	
	public static int getIntBetweenBorders(int min, int max, long seed) {
		Random rdm = new Random();
		rdm.setSeed(seed);
		max += 1;
		int ret = min + rdm.nextInt(max);
		return ret;
	}
}
