package team.cqr.cqrepoured.structuregen;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.grid.DungeonGrid;
import team.cqr.cqrepoured.structuregen.grid.GridRegistry;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (DungeonGenerationHelper.shouldDelayDungeonGeneration(world)) {
			DungeonGenerationHelper.addDelayedChunk(world, chunkX, chunkZ);
			return;
		}

		//setup(CQRConfig.general.dungeonSeparation, CQRConfig.general.dungeonSpread, CQRConfig.general.dungeonRarityFactor, true);
		DungeonBase dungeon = getDungeonAt(world, chunkX, chunkZ);
		if (dungeon == null) {
			return;
		}

		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		dungeon.generate(world, x, z, getRandomForCoords(world, x, z), DungeonDataManager.DungeonSpawnType.DUNGEON_GENERATION,
				DungeonGenerationHelper.shouldGenerateDungeonImmediately(world));
	}

	/**
	 * @return the dungeon that will be generated in this chunk
	 */
	@Nullable
	public static DungeonBase getDungeonAt(World world, int chunkX, int chunkZ) {
		return getDungeonAt(world, chunkX, chunkZ, Predicates.alwaysTrue(), Predicates.alwaysTrue());
	}

	/**
	 * @return the dungeon that will be generated in this chunk
	 */
	@Nullable
	public static DungeonBase getDungeonAt(World world, int chunkX, int chunkZ, Predicate<DungeonGrid> gridPredicate, Predicate<DungeonBase> dungeonPredicate) {
		if (!canSpawnDungeonsInWorld(world)) {
			return null;
		}

		DungeonBase locationSpecificDungeon = getLocationSpecificDungeon(world, chunkX, chunkZ);
		if (locationSpecificDungeon != null) {
			return locationSpecificDungeon;
		}

		return GridRegistry.grids().stream()
				.filter(gridPredicate)
				.map(grid -> grid.getDungeonAt(world, chunkX, chunkZ, dungeonPredicate))
				.findFirst().orElse(null);
	}

	/**
	 * @return true when structure genration is enabled for this world and either the world is no flat world or dungeons can
	 *         generate in flat worlds
	 */
	public static boolean canSpawnDungeonsInWorld(World world) {
		// Check if structures are enabled for this world
		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			return false;
		}
		// Check for flat world type and if dungeons may spawn there
		if (world.getWorldType() != WorldType.FLAT) {
			return true;
		}
		return CQRConfig.general.dungeonsInFlat;
	}

	/**
	 * @return the location specific dungeon that spawns in this chunk
	 */
	@Nullable
	public static DungeonBase getLocationSpecificDungeon(World world, int chunkX, int chunkZ) {
		// Spawn all coordinate specific dungeons for this chunk
		List<DungeonBase> locationSpecificDungeons = DungeonRegistry.getInstance().getLocationSpecificDungeonsForChunk(world, chunkX, chunkZ);
		if (locationSpecificDungeons.isEmpty()) {
			return null;
		}
		if (locationSpecificDungeons.size() > 1) {
			CQRMain.logger.warn("Found {} location specific dungeons for chunkX={}, chunkZ={}!", locationSpecificDungeons.size(), chunkX, chunkZ);
		}
		return locationSpecificDungeons.get(0);
	}

	public static Random getRandomForCoords(World world, int x, int z) {
		return new Random(getSeed(world, x, z));
	}

	// This is needed to calculate the seed, cause we need a new seed for every
	// generation OR we'll have the same dungeon generating every time
	public static long getSeed(World world, int chunkX, int chunkZ) {
		long mix = xorShift64(chunkX) + Long.rotateLeft(xorShift64(chunkZ), 32) + -1094792450L;
		long result = xorShift64(mix);

		return world.getSeed() + result;
	}

	// Needed for seed calculation and randomization
	private static long xorShift64(long x) {
		x ^= x << 21;
		x ^= x >>> 35;
		x ^= x << 4;
		return x;
	}

}
