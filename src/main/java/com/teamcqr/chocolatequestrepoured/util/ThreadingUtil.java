package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThreadingUtil {

	
	public static void passHashMapToThreads(List<BlockPos> positionsToIterate, Map<BlockPos, Block> mapContainingBlockInformation, int entriesPerMap, World world, boolean async) {
		if(async) {
			if(entriesPerMap > 0) {
				int counter = 0;
				Map<BlockPos, Block> tmpMap = new HashMap<>();
				for(BlockPos b : positionsToIterate) {
					if(entriesPerMap > 0 && counter >= entriesPerMap) {
						counter = 0;
						//System.out.println("New map full! Passing to placement....");
						Map<BlockPos, Block> map = new HashMap<>(tmpMap);
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
					if(mapContainingBlockInformation.containsKey(b)) {
						tmpMap.put(b, mapContainingBlockInformation.getOrDefault(b, Blocks.BEDROCK));
					}
					counter++;
				}
				Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
					
					@Override
					public void run() {
						for(BlockPos b : tmpMap.keySet()) {
							world.setBlockState(b, tmpMap.get(b).getDefaultState());
						}
					}
				});
			} else {
				Reference.BLOCK_PLACING_THREADS_INSTANCE.addTask(new Runnable() {
					
					@Override
					public void run() {
						for(BlockPos p : positionsToIterate) {
							if(mapContainingBlockInformation.get(p) == Blocks.AIR) {
								world.setBlockToAir(p);
							} else {
								world.setBlockState(p, mapContainingBlockInformation.getOrDefault(p, Blocks.BEDROCK).getDefaultState());
							}
						}
					}
				});
			}
		} else {
			for(BlockPos p : positionsToIterate) {
				if(mapContainingBlockInformation.get(p) == Blocks.AIR) {
					world.setBlockToAir(p);
				} else {
					world.setBlockState(p, mapContainingBlockInformation.getOrDefault(p, Blocks.BEDROCK).getDefaultState());
				}
			}
		}
	}

	public static void passListWithBlocksToThreads(List<BlockPos> blocksToPlace, Block blockToPlace, World world, int entriesPerPartList, boolean async) {
		if(async) {
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
		} else {
			for(BlockPos bp : blocksToPlace) {
				if(Block.isEqualTo(blockToPlace, Blocks.AIR)) {
					world.setBlockToAir(bp);
				} else {
					world.setBlockState(bp, blockToPlace.getDefaultState());
				}
			}
		}
	}

}
