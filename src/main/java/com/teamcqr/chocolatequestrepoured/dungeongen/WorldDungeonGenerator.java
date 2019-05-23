package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Reference;

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
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		// Check if Dugneon "can" spawn (decided via a chance)
		// System.out.println("Trying to generate dungeon at ChunkX=" + chunkX + "
		// ChunkZ=" + chunkZ + "...");

		boolean flatPass = true;
		if (world.getWorldType().equals(WorldType.FLAT) && !Reference.CONFIG_HELPER.generateDungeonsInFlat()) {
			flatPass = false;
		}

		//DONE: Adjust this check so, that dungeons before and beyond the wall are generated!!!
		if (notInWallRange(chunkX, chunkZ, world)) {
			// BUILD THE FUCKING WALL
			// WALL GENERATION IS IN AN OWN GENERATOR -> WallBuilder
			// I left some space between the wall so that no dungeon can "clip" inside it :D
			boolean canBuildRandomDungeons = true;
			 if (DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world) != null
					&& DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world).size() > 0) {
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
						Random rdm = new Random();
						rdm.setSeed(getSeed(world, chunkX, chunkZ));
						dungeon.generate(dungeon.getLockedPos().getX(), dungeon.getLockedPos().getZ(), world,
								world.getChunkFromChunkCoords(chunkX, chunkZ), rdm);
						canBuildRandomDungeons = false;
					}

				}
			} 
			 if(canBuildRandomDungeons) {
				// Now check if any dungeons exist for this biome....
				Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 1, 100, chunkZ * 16 + 1));
				// System.out.println("Searching dungeons for biome " + biome.getBiomeName() +
				// "...");
				// No Dungeons for this biome -> ragequit
				if (this.dungeonRegistry.getDungeonsForBiome(biome).isEmpty()) {
					// System.out.println("No dungeons for biome " + biome.getBiomeName() + "!");
					return;
				}
				// Now check if the dungeon is far away enough from the last one
				if ((chunkX % this.dungeonRegistry.getDungeonDistance() == 0
						&& chunkZ % this.dungeonRegistry.getDungeonDistance() == 0)
						&& DungeonGenUtils.isFarAwayEnoughFromSpawn(chunkX, chunkZ)) {
					// System.out.println("Chunks are far away enough from last dungeon and from
					// spawn!");
					Random rdm = new Random();
					rdm.setSeed(getSeed(world, chunkX, chunkZ));

					if (DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(chunkX, chunkZ, world)
							|| this.dungeonRegistry.getCoordinateSpecificsMap().isEmpty()) {
						// System.out.println("Location is fine! Choosing dungeon...");
						int strctrIndex = rdm.nextInt(this.dungeonRegistry.getDungeonsForBiome(biome).size());
						DungeonBase chosenDungeon = this.dungeonRegistry.getDungeonsForBiome(biome).get(strctrIndex);
						// System.out.println("Chose dungeon " + chosenStructure.getDungeonName() + "!
						// Calculating chance...");

						if (DungeonGenUtils.PercentageRandom(chosenDungeon.getSpawnChance(), getSeed(world, chunkX, chunkZ))) {
							boolean dimensionIsOK = false;
							for (int dimID : chosenDungeon.getAllowedDimensions()) {
								if (world.provider.getDimension() == dimID) {
									dimensionIsOK = true;
									break;
								}
							}

							if (dimensionIsOK && flatPass) {
								System.out.println("Generating dungeon " + chosenDungeon.getDungeonName()
										+ " at chunkX=" + chunkX + "  chunkZ=" + chunkZ);
								// DONE: Choose a structure and build it --> Dungeon handles it self!
								Random rdmGen = new Random();
								rdm.setSeed(getSeed(world, chunkX, chunkZ));
								chosenDungeon.generate(chunkX * 16 + 1, chunkZ * 16 + 1, world,
										world.getChunkFromChunkCoords(chunkX, chunkZ), rdmGen);
								// TODO: Check if dungeon is unique or every structure should generate once and
								// then check if dungeon is already present
							}
						}
					}
				}
			}
		}
	}

	//A method to check if a dungeon is in an area where it can spawn to not "clip" into the wall
	private boolean notInWallRange(int chunkX, int chunkZ, World world) {
		//If the wall is even enabled -> continue
		if(!Reference.CONFIG_HELPER.buildWall()) {
			return true;
		}
		//Wall is enabled -> check farther
		//Now check if the world is the overworld...
		if(world.provider.getDimension() != 0) {
			return true;
		}
		//The world is the overworld....
		//Now check the coordinates...
		if(chunkZ >= 0) {
			return true;
		}
		//z is < 0 --> north
		if(Math.abs(chunkZ) < Math.abs((Reference.CONFIG_HELPER.getWallSpawnDistance() +6))) {
			return true;
		}
		if(Math.abs(chunkZ) > Math.abs((Reference.CONFIG_HELPER.getWallSpawnDistance() -6))) {
			return true;
		}
		//It is in the region of the wall
		return false;
	}

	public static long getSeed(World world, int chunkX, int chunkZ) {
		long mix = xorShift64(chunkX) + Long.rotateLeft(xorShift64(chunkZ), 32) + -1094792450L;
		long result = xorShift64(mix);

		return world.getSeed() + result;
	}

	private static long xorShift64(long x) {
		x ^= x << 21;
		x ^= x >>> 35;
		x ^= x << 4;
		return x;
	}

}
