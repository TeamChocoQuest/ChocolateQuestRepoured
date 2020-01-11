package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Random;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		// Check for flat world type and if dungeons may spawn there
		if (world.getWorldType() == WorldType.FLAT && !CQRConfig.general.dungeonsInFlat) {
			return;
		}

		// Spawn all coordinate specific dungeons for this chunk
		Set<DungeonBase> coordinateSpecificDungeons = DungeonGenUtils.getLocSpecDungeonsForChunk(world, chunkX, chunkZ);
		if (!coordinateSpecificDungeons.isEmpty()) {
			for (DungeonBase dungeon : coordinateSpecificDungeons) {
				CQRMain.logger.info("Generating dungeon " + dungeon.getDungeonName() + " at chunkX=" + chunkX + ", chunkZ=" + chunkZ);
				Random rand = new Random(getSeed(world, chunkX, chunkZ));
				dungeon.generate(dungeon.getLockedPos().getX(), dungeon.getLockedPos().getZ(), world, world.getChunkFromChunkCoords(chunkX, chunkZ), rand);
			}
			return;
		}

		// Checks if this chunk is in the "wall zone", if yes, abort
		if (DungeonGenUtils.isInWallRange(world, chunkX, chunkZ)) {
			return;
		}

		boolean behindWall = false;
		int dungeonSeparation = CQRConfig.general.dungeonSeparation;

		// Check whether this chunk is farther north than the wall
		if (CQRConfig.wall.enabled && chunkZ < -CQRConfig.wall.distance) {
			if (CQRConfig.general.moreDungeonsBehindWall) {
				dungeonSeparation /= CQRConfig.general.densityBehindWallFactor;
			}
			behindWall = true;
		}

		Chunk spawnChunk = world.getChunkFromBlockCoords(world.getSpawnPoint());
		if ((chunkX - spawnChunk.x) % dungeonSeparation == 0 && (chunkZ - spawnChunk.z) % dungeonSeparation == 0 && DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)
				&& DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, chunkX, chunkZ, dungeonSeparation)) {
			Random rand = new Random(getSeed(world, chunkX, chunkZ));

			// Overall dungeon spawn chance
			if (DungeonGenUtils.PercentageRandom(CQRConfig.general.overallDungeonChance, rand)) {
				Set<DungeonBase> possibleDungeons = DungeonRegistry.getInstance().getDungeonsForChunk(world, chunkX, chunkZ, behindWall);

				if (!possibleDungeons.isEmpty()) {
					int maxWeight = 0;
					for (DungeonBase t : possibleDungeons) {
						maxWeight += t.getSpawnChance();
					}
					double d = rand.nextDouble() * maxWeight;
					for (DungeonBase dungeon : possibleDungeons) {
						d -= dungeon.getSpawnChance();
						if (d <= 0) {
							CQRMain.logger.info("Generating dungeon " + dungeon.getDungeonName() + " at chunkX=" + chunkX + ", chunkZ=" + chunkZ);
							dungeon.generate(chunkX * 16 + 1, chunkZ * 16 + 1, world, world.getChunkFromChunkCoords(chunkX, chunkZ), rand);
							return;
						}
					}
				}
			}
		}
	}

	// This is needed to calculate the seed, cause we need a new seed for every generation OR we'll have the same dungeon generating everytime
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
