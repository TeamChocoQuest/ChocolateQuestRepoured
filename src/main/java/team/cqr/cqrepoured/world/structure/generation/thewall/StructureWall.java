package team.cqr.cqrepoured.world.structure.generation.thewall;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.event.world.structure.generation.DungeonGenerationHelper;
import team.cqr.cqrepoured.event.world.structure.generation.DungeonPreparationExecutor;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonGenerationManager;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.IWallPart;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPartRailingTower;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPartRailingWall;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPartTower;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPartWall;

public class StructureWall extends Feature<NoFeatureConfig> {

	public StructureWall(Codec<NoFeatureConfig> pCodec) {
		super(pCodec);
	}

	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random rng, BlockPos pos, NoFeatureConfig config) {
		IServerWorld isw = (IServerWorld) seedReader;
		ServerWorld world = isw.getLevel();
		
		if (DungeonGenerationHelper.shouldDelayDungeonGeneration(world)) {
		return false;
		}
	
		if (!CQRConfig.wall.enabled && world.dimension() != World.OVERWORLD) {
			return false;
		}
		ChunkPos cp = new ChunkPos(pos);
		int chunkX = cp.x;
		int chunkZ = cp.z;
		// Check if it is the wall region
		if (this.isWallRegion(chunkX, chunkZ/*, world*/)) {
			// TODO: Spawn some camps or outposts here or place random enemies.... Maybe even add a dungeon type that can spawn
			// here?
		}
		// Z is the z value where the wall is -> generates the wall
		if (chunkZ < 0 && Math.abs(chunkZ) == Math.abs(CQRConfig.wall.distance)) {
			
			GeneratableDungeon.Builder dungeonBuilder = new GeneratableDungeon.Builder(world, pos, "Wall in the North", CQRConfig.wall.mob);

			Biome biome = world.getBiome(pos);
			if(biome.getRegistryName().equals(Biomes.PLAINS.getRegistryName()) || biome.getRegistryName().equals(Biomes.SNOWY_TUNDRA.getRegistryName())) {
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
			wallPart.generateWall(chunkX, chunkZ, chunkGenerator, dungeonBuilder, null);
			railingPart.generateWall(chunkX, chunkZ, chunkGenerator, dungeonBuilder, null);

			if (DungeonGenerationHelper.shouldGenerateDungeonImmediately(world)) {
				DungeonGenerationManager.generateNow(world, dungeonBuilder.build(world), null, DungeonSpawnType.DUNGEON_GENERATION);
			} else if (!CQRConfig.advanced.multithreadedDungeonPreparation) {
				DungeonGenerationManager.generate(world, dungeonBuilder.build(world), null, DungeonSpawnType.DUNGEON_GENERATION);
			} else {
				CompletableFuture<GeneratableDungeon> future = DungeonPreparationExecutor.supplyAsync(world, dungeonBuilder::build);
				DungeonPreparationExecutor.thenAcceptAsync(world, future, generatable -> DungeonGenerationManager.generate(world, generatable, null, DungeonSpawnType.DUNGEON_GENERATION));
			}
			
			
			return true;
		}
		return false;
		
	}
	
	private boolean isWallRegion(int chunkX, int chunkZ/*, World world*/) {
		// If the wall is even enabled -> continue
		if (!CQRConfig.wall.enabled) {
			return false;
		}
		// Wall is enabled -> check farther
		// Now check if the world is the overworld...
		/*if (world.dimension() != World.OVERWORLD) {
			return false;
		}*/
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
