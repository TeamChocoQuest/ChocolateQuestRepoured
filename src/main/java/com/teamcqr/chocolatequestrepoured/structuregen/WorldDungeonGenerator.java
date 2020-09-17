package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator implements IWorldGenerator {

	private static final String[] STRUCTURE_NAMES_INTERNAL = { "Stronghold", "Mansion", "Monument", "Village", "Mineshaft", "Temple", "EndCity", "Fortress" };

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (DungeonGenerationHelper.shouldDelayDungeonGeneration(world)) {
			DungeonGenerationHelper.addDelayedChunk(world, chunkX, chunkZ);
			return;
		}

		// Check if structures are enabled for this world
		if (!world.getWorldInfo().isMapFeaturesEnabled()) {
			return;
		}

		// Check for flat world type and if dungeons may spawn there
		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
			return;
		}

		// Spawn all coordinate specific dungeons for this chunk
		List<DungeonBase> locationSpecificDungeons = DungeonRegistry.getInstance().getLocationSpecificDungeonsForChunk(world, chunkX, chunkZ);
		if (!locationSpecificDungeons.isEmpty()) {
			if (locationSpecificDungeons.size() > 1) {
				CQRMain.logger.warn("Found {} location specific dungeons for chunkX={}, chunkZ={}!", locationSpecificDungeons.size(), chunkX, chunkZ);
			}
			for (DungeonBase dungeon : locationSpecificDungeons) {
				for (DungeonSpawnPos dungeonSpawnPos : dungeon.getLockedPositionsInChunk(world, chunkX, chunkZ)) {
					int x = dungeonSpawnPos.getX(world);
					int z = dungeonSpawnPos.getZ(world);
					dungeon.generate(world, x, z, new Random(getSeed(world, x, z)), DungeonDataManager.DungeonSpawnType.LOCKED_COORDINATE,DungeonGenerationHelper.shouldGenerateDungeonImmediately(world));
				}
			}
			return;
		}

		// Checks if this chunk is in the "wall zone", if yes, abort
		if (DungeonGenUtils.isInWallRange(world, chunkX, chunkZ)) {
			return;
		}

		int dungeonSeparation = CQRConfig.general.dungeonSeparation;

		// Check whether this chunk is farther north than the wall
		if (CQRConfig.wall.enabled && chunkZ < -CQRConfig.wall.distance && CQRConfig.general.moreDungeonsBehindWall) {
			dungeonSeparation /= CQRConfig.general.densityBehindWallFactor;
		}

		// Check if the chunk is on the grid
		BlockPos spawnPoint = world.getSpawnPoint();
		if ((chunkX - (spawnPoint.getX() >> 4)) % dungeonSeparation != 0 || (chunkZ - (spawnPoint.getZ() >> 4)) % dungeonSeparation != 0) {
			return;
		}

		if (!DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)) {
			return;
		}

		if (!DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, chunkX, chunkZ, dungeonSeparation)) {
			return;
		}

		// Check if no vanilla structure is near
		if (CQRConfig.advanced.generationRespectOtherStructures) {
			BlockPos p = new BlockPos((chunkX << 4) + 8, world.getSeaLevel(), (chunkZ << 4) + 8);
			for (String sn : STRUCTURE_NAMES_INTERNAL) {
				try {
					BlockPos vanillaStructurePos = world.findNearestStructure(sn, p, true);
					if (vanillaStructurePos != null && p.distanceSq(vanillaStructurePos) <= CQRConfig.advanced.generationMinDistanceToOtherStructure * CQRConfig.advanced.generationMinDistanceToOtherStructure) {
						return;
					}
				} catch (NullPointerException e) {
					// ignore
				}
			}
		}

		Random rand = new Random(getSeed(world, (chunkX << 4) + 8, (chunkZ << 4) + 8));
		if (!DungeonGenUtils.percentageRandom(CQRConfig.general.overallDungeonChance, rand)) {
			return;
		}

		CQRWeightedRandom<DungeonBase> possibleDungeons = DungeonRegistry.getInstance().getDungeonsForPos(world, new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
		DungeonBase dungeon = possibleDungeons.next(rand);
		if (dungeon != null && DungeonGenUtils.percentageRandom(dungeon.getChance(), rand)) {
			dungeon.generate(world, (chunkX << 4) + 8, (chunkZ << 4) + 8, rand, DungeonDataManager.DungeonSpawnType.DUNGEON_GENERATION, DungeonGenerationHelper.shouldGenerateDungeonImmediately(world));
		}
	}

	// This is needed to calculate the seed, cause we need a new seed for every generation OR we'll have the same dungeon generating every time
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
