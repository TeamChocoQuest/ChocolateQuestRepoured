package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonGenUtils {
	
	public static boolean isInsideCircle(int ix, int iz, int radius, BlockPos center) {
		BlockPos newPos = new BlockPos(center.getX() +ix, center.getY(), center.getZ() +iz);
		
		return isInsideSphere(newPos, center, radius);
	}
	
	public static boolean isInsideSphere(BlockPos pos, BlockPos center, int radius) {
		if(Math.abs(center.getDistance(pos.getX(), pos.getY(), pos.getZ())) < radius) {
			return true;
		}
		return false;
	}
	
	public static void passHashMapToThread(HashMap<BlockPos, Block> blocks, int entriesPerMap, World world, boolean async) {
		if(async) {
			Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {

				@Override
				public void run() {
					HashMap<BlockPos, Block> tmpMap = new HashMap<>();
					int counter = 1;
					for(BlockPos b : blocks.keySet()) {
						if(counter == entriesPerMap) {
							@SuppressWarnings("unchecked")
							HashMap<BlockPos, Block> map = (HashMap<BlockPos, Block>) tmpMap.clone();
							Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
								
								@Override
								public void run() {
									for(BlockPos b : map.keySet()) {
										world.setBlockState(b, map.get(b).getDefaultState());
									}
								}
							});
							tmpMap.clear();
						}
						tmpMap.put(b, blocks.get(b));
						counter++;
					}
				}
				
			});
		} else {
			for(BlockPos b : blocks.keySet()) {
				world.setBlockState(b, blocks.get(b).getDefaultState());
			}
		}
	}

	public static void passListWithBlocksToThreads(List<BlockPos> blocksToPlace, Block blockToPlace, World world, int entriesPerPartList) {
		List<BlockPos> bplistTMP = new ArrayList<BlockPos>();
		int counter = 1;
		for(BlockPos bp : blocksToPlace) {
			bplistTMP.add(bp);
			//One Task contains 50 blocks to place
			if(counter % entriesPerPartList == 0) {
				Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
					
					@Override
					public void run() {
						for(BlockPos b : bplistTMP) {
							if(Block.isEqualTo(blockToPlace, Blocks.AIR)) {
								world.setBlockToAir(b);
							} else {
								world.setBlockState(b, blockToPlace.getDefaultState());
							}
						}
						
					}
				});
				
				bplistTMP.clear();
			}
			counter++;
		}
		Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
			
			@Override
			public void run() {
				for(BlockPos b : bplistTMP) {
					if(Block.isEqualTo(blockToPlace, Blocks.AIR)) {
						world.setBlockToAir(b);
					} else {
						world.setBlockState(b, blockToPlace.getDefaultState());
					}
				}
				
			}
		});
	}
	
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
	
	public static boolean isCQBanner(TileEntityBanner banner) {
		return BannerHelper.isCQBanner(banner); 
	}
	
	public static boolean isFarAwayEnoughFromSpawn(int chunkX, int chunkZ) {
		if(Math.abs(chunkX) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance()) && Math.abs(chunkZ) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance())) {
			return true;
		}
		return false;
	}
	
	//IMPORTANT: x and z are the CHUNK's x and z!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(int x, int z, World world, int dungeonSeparation) {
		return isFarAwayEnoughFromLocationSpecifics(new BlockPos(x, 0, z), world, dungeonSeparation);
	}
	//IMPORTANT: pos is a CHUNKPOS!!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(BlockPos pos, World world, int dungeonSeparation) {
		if(CQRMain.dungeonRegistry.getCoordinateSpecificsMap() != null && CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet().size() > 0) {
			for(BlockPos dunPos : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet()) {
				//Chunk chunk = world.getChunkFromBlockCoords(dunPos);
				BlockPos chunkPos = new BlockPos(Math.abs(dunPos.getX() /16), 0, Math.abs(dunPos.getZ() /16));
				if(Math.abs(chunkPos.getDistance(Math.abs(pos.getX()), 0, Math.abs(pos.getZ()))) < dungeonSeparation) {
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
		
		if(ret.isEmpty()) {
			return null;
		}
		
		return ret;
	}
	public static List<DungeonBase> getLocSpecDungeonsForChunk(Chunk chunk, World world) {
		return getLocSpecDungeonsForChunk(chunk.x, chunk.z, world);
	}
}
