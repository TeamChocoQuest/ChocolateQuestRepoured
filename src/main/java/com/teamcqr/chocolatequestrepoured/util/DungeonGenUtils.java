package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonGenUtils {

	public static boolean isInsideCircle(int ix, int iz, int radius, BlockPos center) {
		BlockPos newPos = new BlockPos(center.getX() + ix, center.getY(), center.getZ() + iz);

		return isInsideSphere(newPos, center, radius);
	}

	public static boolean isInsideSphere(BlockPos pos, BlockPos center, int radius) {
		if (Math.abs(center.getDistance(pos.getX(), pos.getY(), pos.getZ())) < radius) {
			return true;
		}
		return false;
	}

	public static int getHighestYAt(Chunk chunk, int x, int z, boolean countWaterAsAir) {
		int y = 255;
		Block block = chunk.getBlockState(x, y, z).getBlock();
		while (Block.isEqualTo(block, Blocks.AIR) || (countWaterAsAir && (Block.isEqualTo(block, Blocks.WATER) || Block.isEqualTo(block, Blocks.FLOWING_WATER)))) {
			y--;
			block = chunk.getBlockState(x, y, z).getBlock();
		}

		return y;
	}

	public static boolean PercentageRandom(int number, Random rdm) {
		if(number >= 100) {
			return true;
		}
		int rdmNmbr = rdm.nextInt(100) + 1;
		if (number >= rdmNmbr) {
			return true;
		}
		return false;
	}

	public static boolean PercentageRandom(double number, long seed) {
		Random rdm = new Random();
		rdm.setSeed(seed);
		number *= 100;
		return PercentageRandom(((Double) number).intValue(), rdm);
	}

	public static int getIntBetweenBorders(int min, int max, Random rdm) {
		if (min != max && rdm != null) {
			max += 1;
			int ret = min + rdm.nextInt(max - min);
			return ret;
		}
		return min;
	}

	public static int getIntBetweenBorders(int min, int max) {
		Random rdm = new Random();
		max += 1;
		try {
			return min + rdm.nextInt(max - min);
		} catch (IllegalArgumentException ex) {
			return min;
		}
	}

	public static boolean isLootChest(Block b) {
		return b instanceof BlockExporterChest;
	}

	public static boolean isCQBanner(TileEntityBanner banner) {
		return BannerHelper.isCQBanner(banner);
	}

	public static boolean isFarAwayEnoughFromSpawn(World world, int chunkX, int chunkZ) {
		/*if (Math.abs(chunkX) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance()) && Math.abs(chunkZ) >= Math.abs(CQRMain.dungeonRegistry.getDungeonSpawnDistance())) {
			return true;
		}*/
		Chunk spawnChunk = world.getChunkFromBlockCoords(world.getSpawnPoint());
		chunkX -= spawnChunk.x;
		chunkZ -= spawnChunk.z;
		double dist = Math.sqrt((chunkX * chunkX) + (chunkZ * chunkZ));
		return dist >= CQRMain.dungeonRegistry.getDungeonSpawnDistance();
	}

	// IMPORTANT: x and z are the CHUNK's x and z!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(int x, int z, World world, int dungeonSeparation) {
		return isFarAwayEnoughFromLocationSpecifics(new BlockPos(x, 0, z), world, dungeonSeparation);
	}

	// IMPORTANT: pos is a CHUNKPOS!!!
	public static boolean isFarAwayEnoughFromLocationSpecifics(BlockPos pos, World world, int dungeonSeparation) {
		if (CQRMain.dungeonRegistry.getCoordinateSpecificsMap() != null && CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet().size() > 0) {
			for (BlockPos dunPos : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet()) {
				// Chunk chunk = world.getChunkFromBlockCoords(dunPos);
				BlockPos chunkPos = new BlockPos(Math.abs(dunPos.getX() / 16), 0, Math.abs(dunPos.getZ() / 16));
				if (Math.abs(chunkPos.getDistance(Math.abs(pos.getX()), 0, Math.abs(pos.getZ()))) < dungeonSeparation) {
					return false;
				}
			}
		}

		return true;
	}

	public static List<DungeonBase> getLocSpecDungeonsForChunk(int chunkX, int chunkZ, World world) {
		List<DungeonBase> ret = new ArrayList<DungeonBase>();

		for (BlockPos dunPos : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().keySet()) {
			Chunk dunChun = world.getChunkFromBlockCoords(dunPos);
			if (dunChun.x == chunkX && dunChun.z == chunkZ) {
				for (DungeonBase db : CQRMain.dungeonRegistry.getCoordinateSpecificsMap().get(dunPos)) {
					ret.add(db);
				}
			}
		}

		if (ret.isEmpty()) {
			return null;
		}

		return ret;
	}

	public static List<DungeonBase> getLocSpecDungeonsForChunk(Chunk chunk, World world) {
		return getLocSpecDungeonsForChunk(chunk.x, chunk.z, world);
	}

	/*
	 * Rotate a vec3i to align with the given side.
	 * Assumes that the vec3i is default +x right, +z down coordinate system
	 */
	public static Vec3i rotateVec3i(Vec3i vec, EnumFacing side) {
		if (side == EnumFacing.SOUTH) {
			return new Vec3i(-vec.getX(), vec.getY(), -vec.getZ());
		} else if (side == EnumFacing.WEST) {
			return new Vec3i(vec.getZ(), vec.getY(), -vec.getX());
		} else if (side == EnumFacing.EAST) {
			return new Vec3i(-vec.getZ(), vec.getY(), vec.getX());
		} else {
			// North side, or some other invalid side
			return vec;
		}
	}

	public static int randomBetween(Random random, int low, int high) {
		if (high <= low) {
			return low;
		} else {
			return low + random.nextInt(high - low + 1);
		}
	}

	public static int randomBetweenGaussian(Random random, int low, int high) {
		if (high <= low) {
			return low;
		} else {
			double avg = low + ((high - low) / 2d);
			double stdDev = (high - avg) / 3d; // guarantees that MOST (99.7%) results will be between low & high
			double gaussian = random.nextGaussian();
			int result = (int) (avg + (gaussian * stdDev) + 0.5); // 0.5 is added for rounding to nearest whole number
			if (result < low) {
				return low;
			} else if (result > high) {
				return high;
			} else {
				return result;
			}
		}
	}

	public static Vec3i rotateMatrixOffsetCW(Vec3i offset, int sizeX, int sizeZ, int numRotations) {
		final int maxXIndex = sizeX - 1;
		final int maxZIndex = sizeZ - 1;

		if (numRotations % 4 == 0) {
			return new Vec3i(offset.getX(), offset.getY(), offset.getZ());
		} else if (numRotations % 4 == 1) {
			return new Vec3i(maxZIndex - offset.getZ(), offset.getY(), offset.getX());
		} else if (numRotations % 4 == 2) {
			return new Vec3i(maxXIndex - offset.getX(), offset.getY(), maxZIndex - offset.getZ());
		} else {
			return new Vec3i(offset.getZ(), offset.getY(), maxXIndex - offset.getX());
		}
	}
}
