package com.teamcqr.chocolatequestrepoured.structuregen.thewall;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts.IWallPart;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts.WallPartRailingTower;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts.WallPartRailingWall;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts.WallPartTower;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts.WallPartWall;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub:
 * https://github.com/DerToaster98
 */
public class WorldWallGenerator implements IWorldGenerator {

	public WorldWallGenerator() {

	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.isRemote) {
			return;
		}
		if(world.provider.getDimension() != 0) {
			return;
		}
		// Check if it is the wall region
		if (this.isWallRegion(chunkX, chunkZ, world)) {
			// TODO: Spawn some camps or outposts here or place random enemies.... Maybe even add a dungeon type that can spawn here?
		}
		// Z is the z value where the wall is -> generates the wall
		if (chunkZ < 0 && Math.abs(chunkZ) == Math.abs(CQRConfig.wall.distance)) {
			Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 1, 100, chunkZ * 16 + 1));
			if (biome instanceof BiomePlains || biome instanceof BiomeSnow) {
				// Flag for the gate
			}
			IWallPart wallPart = null;
			IWallPart railingPart = null;
			// GENERATE THE WALL
			// Check wether it should construct a wall part or a tower
			if (chunkX % CQRConfig.wall.towerDistance == 0) {
				// Build tower
				wallPart = new WallPartTower();
				railingPart = new WallPartRailingTower();
			} else {
				// Build wall
				wallPart = new WallPartWall();
				railingPart = new WallPartRailingWall();
			}
			if (wallPart != null) {
				wallPart.generateWall(chunkX, chunkZ, world, world.getChunkFromChunkCoords(chunkX, chunkZ));
			}
			if (railingPart != null) {
				railingPart.generateWall(chunkX, chunkZ, world, world.getChunkFromChunkCoords(chunkX, chunkZ));
			}
		}

	}

	private boolean isWallRegion(int chunkX, int chunkZ, World world) {
		// If the wall is even enabled -> continue
		if (!CQRConfig.wall.enabled) {
			return false;
		}
		// Wall is enabled -> check farther
		// Now check if the world is the overworld...
		if (world.provider.getDimension() != 0) {
			return false;
		}
		// The world is the overworld....
		// Now check the coordinates...
		if (chunkZ >= 0) {
			return false;
		}
		// z is < 0 --> north
		// Check if the coords are farther south than the wall
		if (Math.abs(chunkZ) < Math.abs((CQRConfig.wall.distance - 8))) {
			return false;
		}
		// Check if the coords are farther north than the wall
		if (Math.abs(chunkZ) > Math.abs((CQRConfig.wall.distance + 8))) {
			return false;
		}
		// It is in the region of the wall
		return true;
	}

}
