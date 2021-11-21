package team.cqr.cqrepoured.structuregen.thewall;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.structuregen.DungeonGenerationHelper;
import team.cqr.cqrepoured.structuregen.DungeonPreparationExecutor;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerationManager;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.thewall.wallparts.IWallPart;
import team.cqr.cqrepoured.structuregen.thewall.wallparts.WallPartRailingTower;
import team.cqr.cqrepoured.structuregen.thewall.wallparts.WallPartRailingWall;
import team.cqr.cqrepoured.structuregen.thewall.wallparts.WallPartTower;
import team.cqr.cqrepoured.structuregen.thewall.wallparts.WallPartWall;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WorldWallGenerator implements IWorldGenerator {

	public WorldWallGenerator() {

	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (DungeonGenerationHelper.shouldDelayDungeonGeneration(world)) {
			return;
		}

		if (!CQRConfig.wall.enabled || world.isRemote || world.provider.getDimension() != 0) {
			return;
		}
		// Check if it is the wall region
		if (this.isWallRegion(chunkX, chunkZ, world)) {
			// TODO: Spawn some camps or outposts here or place random enemies.... Maybe even add a dungeon type that can spawn
			// here?
		}
		// Z is the z value where the wall is -> generates the wall
		if (chunkZ < 0 && Math.abs(chunkZ) == Math.abs(CQRConfig.wall.distance)) {
			BlockPos pos = new BlockPos((chunkX << 4) + 8, world.getSeaLevel(), (chunkZ << 4) + 8);
			GeneratableDungeon.Builder dungeonBuilder = new GeneratableDungeon.Builder(world, pos, "Wall in the North", CQRConfig.wall.mob);

			Biome biome = world.getBiomeProvider().getBiome(pos);
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
			wallPart.generateWall(chunkX, chunkZ, world, world.getChunk(chunkX, chunkZ), dungeonBuilder);
			railingPart.generateWall(chunkX, chunkZ, world, world.getChunk(chunkX, chunkZ), dungeonBuilder);

			if (DungeonGenerationHelper.shouldGenerateDungeonImmediately(world)) {
				DungeonGenerationManager.generateNow(world, dungeonBuilder.build(world), null, DungeonSpawnType.DUNGEON_GENERATION);
			} else if (!CQRConfig.advanced.multithreadedDungeonPreparation) {
				DungeonGenerationManager.generate(world, dungeonBuilder.build(world), null, DungeonSpawnType.DUNGEON_GENERATION);
			} else {
				CompletableFuture<GeneratableDungeon> future = DungeonPreparationExecutor.supplyAsync(world, dungeonBuilder::build);
				DungeonPreparationExecutor.thenAcceptAsync(world, future,
						generatable -> DungeonGenerationManager.generate(world, generatable, null, DungeonSpawnType.DUNGEON_GENERATION));
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
