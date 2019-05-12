package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonGenUtils {
	
	public static int getHighestYAt(Chunk chunk, int x, int z, boolean ignoreWater) {
		int y = 255;
		Block block = chunk.getBlockState(x, y, z).getBlock();
		if(ignoreWater) {
			while(Block.isEqualTo(block, Blocks.AIR) || Block.isEqualTo(block, Blocks.WATER) || Block.isEqualTo(block, Blocks.FLOWING_WATER)) {
				y--;
				block = chunk.getBlockState(x, y, z).getBlock();
			}
		} else {
			while(Block.isEqualTo(block, Blocks.AIR)) {
				y--;
				block = chunk.getBlockState(x, y, z).getBlock();
			}
		}
		
		return y;
	}
	
	public static boolean PercentageRandom(int number, Random rdm) {
		//Random rdm = new Random();
		//rdm.setSeed(seed);
		int rdmNmbr = rdm.nextInt(100) +1;
		if(number >= rdmNmbr) {
			return true;
		}
		return false;
	}
	
	public static boolean PercentageRandom(double number, long seed) {
		Random rdm = new Random();
		rdm.setSeed(seed);
		number *= 100;
		return PercentageRandom(((Double)number).intValue(), rdm);
	}
	
	public static int getIntBetweenBorders(int min, int max, Random rdm) {
		if(min != max && rdm != null) {
			//Random rdm = new Random();
			//rdm.setSeed(seed);
			max += 1;
			int ret = min + rdm.nextInt(max - min);
			return ret;
		}
		return min;
	}
	
	public static int getIntBetweenBorders(int min, int max) {
		Random rdm = new Random();
		max += 1;
		int ret = min + rdm.nextInt(max - min);
		return ret;
	}
	
	public static boolean isLootChest(Block b) {
		for(Block lc : ModBlocks.LOOT_CHEST_BLOCKS) {
			if(Block.isEqualTo(b, lc)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCQBanner() {
		
		return false;
	}
	
	public static boolean isFarAwayEnoughFromSpawn(int chunkX, int chunkZ) {
		if(Math.abs(chunkX) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance()) && Math.abs(chunkZ) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance())) {
			return true;
		}
		return false;
	}
	
	//IMPORTANT: x and z are the CHUNK's x and z!!!!!!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(int x, int z, World world) {
		return isFarAwayEnoughFromLocationSpecifics(new BlockPos(x, 0, z), world);
	}
	//IMPORTANT: pos is a CHUNKPOS!!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(BlockPos pos, World world) {
		if(CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet().size() > 0) {
			for(BlockPos dunPos : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet()) {
				Chunk chunk = world.getChunkFromBlockCoords(dunPos);
				BlockPos chunkPos = new BlockPos(chunk.x, pos.getY(), chunk.z);
				if(!(chunkPos.getDistance(pos.getX(), chunkPos.getY(), pos.getZ()) >= CQRMain.dungeonRegistry.getDungeonDistance())) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static List<DungeonBase> getLocSpecDungeonsForChunk(int chunkX, int chunkZ, World world) {
		List<DungeonBase> ret = new ArrayList<DungeonBase>();
		
		for(BlockPos dunPos : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet()) {
			Chunk dunChun = world.getChunkFromBlockCoords(dunPos);
			if(dunChun.x == chunkX && dunChun.z == chunkZ) {
				for(DungeonBase db : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().get(dunPos)) {
					ret.add(db);
				}
			}
		}
		
		return ret;
	}
	public static List<DungeonBase> getLocSpecDungeonsForChunk(Chunk chunk, World world) {
		return getLocSpecDungeonsForChunk(chunk.x, chunk.z, world);
	}
}
