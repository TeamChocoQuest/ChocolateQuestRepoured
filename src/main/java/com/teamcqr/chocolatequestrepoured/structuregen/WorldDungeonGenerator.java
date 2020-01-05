package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WorldDungeonGenerator implements IWorldGenerator {

	private DungeonRegistry dungeonRegistry;

	public WorldDungeonGenerator() {
		this.dungeonRegistry = CQRMain.dungeonRegistry;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.isRemote) {
			return;
		}

		// Check for flat worlds, if dungeons may spawn there
		boolean flatPass = true;
		boolean behindWall = false;
		if (world.getWorldType().equals(WorldType.FLAT) && !CQRConfig.general.dungeonsInFlat) {
			flatPass = false;
		}

		// Checks if this chunk is in the "wall zone", if yes, abort
		if (this.notInWallRange(chunkX, chunkZ, world)) {
			int dungeonSeparation = this.dungeonRegistry.getDungeonDistance();

			// Check wether the generated chunk is farther north than the wall...
			if (CQRConfig.wall.enabled && chunkZ < 0 && Math.abs(chunkZ) > Math.abs(CQRConfig.wall.distance)) {
				dungeonSeparation /= 2;
				behindWall = true;
			}

			boolean canBuildRandomDungeons = true;
			if (DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world) != null && DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world).size() > 0) {
				System.out.println("Found location specific Dungeons for ChunkX=" + chunkX + " ChunkZ=" + chunkZ + "!");
				for (DungeonBase dungeon : DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world)) {
					boolean dimensionIsOK = false;
					// Here we also need to check if the dimension fits
					for (int dimID : dungeon.getAllowedDimensions()) {
						if (world.provider.getDimension() == dimID) {
							dimensionIsOK = true;
							break;
						}
					}

					if (dimensionIsOK && flatPass) {
						Random rdm = new Random(getSeed(world, chunkX, chunkZ));
						dungeon.generate(dungeon.getLockedPos().getX(), dungeon.getLockedPos().getZ(), world, world.getChunkFromChunkCoords(chunkX, chunkZ), rdm);
						canBuildRandomDungeons = false;
					}

				}
			}
			if (canBuildRandomDungeons) {
				// Now check if any dungeons exist for this biome....
				Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16, 100, chunkZ * 16));
				// No Dungeons for this biome -> ragequit
				if (this.dungeonRegistry.getDungeonsForBiome(biome).isEmpty()) {
					return;
				}
				// Now check if the dungeon is far away enough from the last one
				if ((chunkX % dungeonSeparation == 0 && chunkZ % dungeonSeparation == 0) && DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)) {
					Random rdm = new Random(getSeed(world, chunkX, chunkZ));
					
					if (DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(chunkX, chunkZ, world, dungeonSeparation) || this.dungeonRegistry.getCoordinateSpecificsMap().isEmpty()) {
						
						//Overall dungeon spawn chance
						if(!DungeonGenUtils.PercentageRandom(CQRConfig.general.overallDungeonChance, rdm)) {
							return;
						}
						
						// Chooses a dungeon to generate
						// TODO: Add support for unique dungeons, means i need to save the dungeons positions into a file...
						int strctrIndex = rdm.nextInt(this.dungeonRegistry.getDungeonsForBiome(biome).size());
						DungeonBase chosenDungeon = this.dungeonRegistry.getDungeonsForBiome(biome).get(strctrIndex);

						// Checks, if the dungeon generates (calculated by the percentage chance the
						// dungeon has...
						if (DungeonGenUtils.PercentageRandom(chosenDungeon.getSpawnChance(), rdm)) {
							boolean dimensionIsOK = false;
							// This checks the dimension the dungeon can spawn in
							for (int dimID : chosenDungeon.getAllowedDimensions()) {
								if (world.provider.getDimension() == dimID) {
									dimensionIsOK = true;
									break;
								}
							}

							boolean wallPass = true;
							if (!behindWall && chosenDungeon.doesSpawnOnlyBehindWall()) {
								return;
							}

							// If the dimension is fine, if yes, it will generate the dungeon
							if (dimensionIsOK && flatPass && wallPass) {
								System.out.println("Generating dungeon " + chosenDungeon.getDungeonName() + " at chunkX=" + chunkX + "  chunkZ=" + chunkZ);
								// DONE: Choose a structure and build it --> Dungeon handles it self!
								Random rdmGen = new Random(getSeed(world, chunkX, chunkZ));
								chosenDungeon.generate(chunkX * 16 + 1, chunkZ * 16 + 1, world, world.getChunkFromChunkCoords(chunkX, chunkZ), rdmGen);
								// TODO: Check if dungeon is unique or every structure should generate once and
								// then check if dungeon is already present
							}
						}
					}
				}
			}
		}
	}

	// A method to check if a dungeon is in an area where it can spawn to not "clip" into the wall
	private boolean notInWallRange(int chunkX, int chunkZ, World world) {
		// If the wall is even enabled -> continue
		if (!CQRConfig.wall.enabled) {
			return true;
		}
		// Wall is enabled -> check farther
		// Now check if the world is the overworld...
		if (world.provider.getDimension() != 0) {
			return true;
		}
		// The world is the overworld....
		// Now check the coordinates...
		if (chunkZ > 0) {
			return true;
		}
		// z is < 0 --> north
		if (Math.abs(chunkZ) < Math.abs((CQRConfig.wall.distance - 12))) {
			return true;
		}
		if (Math.abs(chunkZ) > Math.abs((CQRConfig.wall.distance + 12))) {
			return true;
		}
		// It is in the region of the wall
		return false;
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
