package team.cqr.cqrepoured.world.structure.generation;

import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator {

	/**
	 * @return the dungeon that will be generated in this chunk
	 */
	@Nullable
	public static DungeonBase getDungeonAt(ServerLevel world, ChunkPos chunkPos) {
		return getDungeonAt(world, chunkPos, Predicates.alwaysTrue());
	}

	/**
	 * @return the dungeon that will be generated in this chunk
	 */
	@Nullable
	public static DungeonBase getDungeonAt(ServerLevel world, ChunkPos chunkPos, Predicate<DungeonGrid> gridPredicate) {
		DungeonBase locationSpecificDungeon = DungeonRegistry.getInstance().getLocationSpecificDungeon(world, chunkPos);
		if (locationSpecificDungeon != null) {
			return locationSpecificDungeon;
		}

		return GridRegistry.getInstance().getGrids().stream().filter(gridPredicate).map(grid -> grid.getDungeonAt(world, chunkPos)).filter(Objects::nonNull).findFirst().orElse(null);
	}

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
			ServerChunkProvider chunkSource = level.getChunkSource();
			if (chunkSource.getGenerator() == chunkGenerator) {
				return level;
			}
		}
		throw new IllegalStateException();
	}

}
