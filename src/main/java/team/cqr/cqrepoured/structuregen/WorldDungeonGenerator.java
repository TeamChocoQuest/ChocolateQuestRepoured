package team.cqr.cqrepoured.structuregen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.integration.IntegrationInformation;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VanillaStructureHelper;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class WorldDungeonGenerator implements IWorldGenerator {

	private static boolean logFailReasons = true;
	private static int distance;
	private static int spread;
	private static double rarityFactor;

	public static void setup(int distance, int spread, double rarityFactor, boolean logFailReasons) {
		WorldDungeonGenerator.distance = distance;
		WorldDungeonGenerator.spread = spread;
		WorldDungeonGenerator.rarityFactor = rarityFactor;
		WorldDungeonGenerator.logFailReasons = logFailReasons;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (DungeonGenerationHelper.shouldDelayDungeonGeneration(world)) {
			DungeonGenerationHelper.addDelayedChunk(world, chunkX, chunkZ);
			return;
		}

		setup(CQRConfig.general.dungeonSeparation, CQRConfig.general.dungeonSpread, CQRConfig.general.dungeonRarityFactor, true);
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
		if (!canSpawnDungeonsInWorld(world)) {
			return null;
		}

		DungeonBase locationSpecificDungeon = getLocationSpecificDungeon(world, chunkX, chunkZ);
		if (locationSpecificDungeon != null) {
			return locationSpecificDungeon;
		}

		if (!canSpawnDungeonAtCoords(world, chunkX, chunkZ)) {
			return null;
		}

		Random random = getRandomForCoords(world, chunkX, chunkZ);
		if (!DungeonGenUtils.percentageRandom(CQRConfig.general.overallDungeonChance, random)) {
			log(world, chunkX, chunkZ, "Global dungeon generation chance check failed");
			return null;
		}

		Biome biome = getBiomeForChunk(world, chunkX, chunkZ);
		DungeonRegistry registry = DungeonRegistry.getInstance();
		CQRWeightedRandom<DungeonBase> possibleDungeons = registry.getDungeonsForPos(world, biome, chunkX, chunkZ);
		DungeonBase dungeon = possibleDungeons.next(random);
		if (dungeon == null) {
			log(world, chunkX, chunkZ, "Could not find any dungeon for biome: %s (%s)", biome, BiomeDictionary.getTypes(biome));
			return null;
		}

		int weight = dungeon.getWeight();
		int totalWeight = possibleDungeons.getTotalWeight();
		double chanceModifier = 1.0D / Math.pow((double) weight / (double) totalWeight, rarityFactor);
		if (!DungeonGenUtils.percentageRandom((double) dungeon.getChance() / 100.0D * chanceModifier, random)) {
			log(world, chunkX, chunkZ, "Specific dungeon generation chance check failed for dungeon: %s", dungeon);
			return null;
		}

		return dungeon;
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

	/**
	 * Checks if<br>
	 * - this chunk is far away enough from the spawn<br>
	 * - other structures are far away enough<br>
	 * - the chunk coords are on the dungeon grid
	 * 
	 * @return true when dungeon can be spawned in this chunk
	 */
	public static boolean canSpawnDungeonAtCoords(World world, int chunkX, int chunkZ) {
		if (!DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)) {
			return false;
		}

		if (isOtherStructureNearby(world, chunkX, chunkZ)) {
			return false;
		}

		// Check if the chunk is on the grid
		return canSpawnStructureAtCoords(world, chunkX, chunkZ);
	}

	/**
	 * @return true when a location specific dungeon, a vanilla structure or a aw2 structure is nearby
	 */
	public static boolean isOtherStructureNearby(World world, int chunkX, int chunkZ) {
		// Checks if this chunk is in the "wall zone", if yes, abort
		if (DungeonGenUtils.isInWallRange(world, chunkX, chunkZ)) {
			return true;
		}

		if (!DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, chunkX, chunkZ, 4)) {
			return true;
		}

		BlockPos pos = new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8);
		if (CQRConfig.advanced.generationRespectOtherStructures) {
			// Vanilla Structures
			if (VanillaStructureHelper.isStructureInRange(world, pos, MathHelper.ceil(CQRConfig.advanced.generationMinDistanceToOtherStructure / 16.0D))) {
				log(world, chunkX, chunkZ, "Nearby vanilla structure was found");
				return true;
			}
			// AW2-Structures
			if (IntegrationInformation.isAW2StructureAlreadyThere(world, pos)) {
				log(world, chunkX, chunkZ, "Nearby ancient warfare 2 structure was found");
				return true;
			}
		}

		return false;
	}

	/**
	 * @return true when the passed chunk coords are on the dungeon grid
	 */
	public static boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		int dungeonSeparation = distance;
		// Check whether this chunk is farther north than the wall
		if (CQRConfig.wall.enabled && chunkZ < -CQRConfig.wall.distance && CQRConfig.general.moreDungeonsBehindWall) {
			dungeonSeparation = MathHelper.ceil((double) dungeonSeparation / CQRConfig.general.densityBehindWallFactor);
		}
		int dungeonSpread = Math.min(spread + 1, dungeonSeparation);

		int cx = chunkX - (DungeonGenUtils.getSpawnX(world) >> 4);
		int cz = chunkZ - (DungeonGenUtils.getSpawnZ(world) >> 4);
		if (dungeonSpread <= 1) {
			return cx % dungeonSeparation == 0 && cz % dungeonSeparation == 0;
		}

		int x = MathHelper.intFloorDiv(cx, dungeonSeparation);
		int z = MathHelper.intFloorDiv(cz, dungeonSeparation);
		Random random = world.setRandomSeed(x, z, 10387312);
		x *= dungeonSeparation;
		z *= dungeonSeparation;
		x += random.nextInt(dungeonSpread);
		z += random.nextInt(dungeonSpread);
		return x == cx && z == cz;
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

	public static Biome getBiomeForChunk(World world, int chunkX, int chunkZ) {
		MutableBlockPos mutablePos = new MutableBlockPos();
		Optional<Biome> biome = IntStream.range(0, 256).mapToObj(i -> {
			int x = i >> 4;
			int z = i & 15;
			mutablePos.setPos((chunkX << 4) + x, 0, (chunkZ << 4) + z);
			return world.getBiome(mutablePos);
		}).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey);
		return biome.orElseGet(() -> Biomes.PLAINS);
	}

	private static void log(World world, int chunkX, int chunkZ, String message, Object... params) {
		if (!logFailReasons) {
			return;
		}
		String s = String.format(message, params);
		CQRMain.logger.info("Failed to generate structure at x={} z={} dim={}: {}", (chunkX << 4) + 8, (chunkZ << 4) + 8, world.provider.getDimension(), s);
	}

}
