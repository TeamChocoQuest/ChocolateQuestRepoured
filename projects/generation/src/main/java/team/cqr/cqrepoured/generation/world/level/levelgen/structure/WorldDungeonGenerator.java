package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.Random;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator {

	public static Random getRandomForCoords(long seed, int x, int z) {
		return new Random(getSeed(seed, x, z));
	}

	// This is needed to calculate the seed, cause we need a new seed for every
	// generation OR we'll have the same dungeon generating every time
	public static long getSeed(long seed, int chunkX, int chunkZ) {
		long mix = xorShift64(chunkX) + Long.rotateLeft(xorShift64(chunkZ), 32) + -1_094_792_450L;
		long result = xorShift64(mix);

		return seed + result;
	}

	// Needed for seed calculation and randomization
	private static long xorShift64(long x) {
		x ^= x << 21;
		x ^= x >>> 35;
		x ^= x << 4;
		return x;
	}

	public static ServerLevel getLevel(ChunkGenerator chunkGenerator) {
		for (ServerLevel level : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
			ServerChunkCache chunkSource = level.getChunkSource();
			if (chunkSource.getGenerator() == chunkGenerator) {
				return level;
			}
		}
		throw new IllegalStateException();
	}

}
