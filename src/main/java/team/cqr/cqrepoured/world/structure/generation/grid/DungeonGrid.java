package team.cqr.cqrepoured.world.structure.generation.grid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class DungeonGrid implements IFeatureConfig {
	
	// TODO: Write codec that uses the old prop file to re-create the object
	// => Store the prop file somewhere in the object as string
	public static final Codec<DungeonGrid> CODEC = RecordCodecBuilder.create((object) -> {
		return object.group(
				Codec.STRING.fieldOf("name").forGetter((obj) -> {
					return obj.getName();
				}),
				Codec.STRING.fieldOf("propertyfile").forGetter((obj) -> {
					return obj.getPropFileAsString();
				})
			).apply(object, (name, propString) -> {
				//If a dungeon with this name has already been registered, use the registered object
				DungeonGrid grid = GridRegistry.getInstance().getGrid(name);
				if(grid != null) {
					return grid;
				}
				//Otherwise restore the dungeon object
				Properties prop = DungeonBase.getFromString(propString);
				if(prop != null) {
					DungeonGrid grid2 = GridRegistry.createGridFromFile(prop, name);
					return grid2;
				}
				return null;
			});
	});
	
	String getPropFileAsString() {
		return this.SCANNED_PROPERTIES_FILE;
	}
			
	protected final String SCANNED_PROPERTIES_FILE;

	private static final Random RANDOM = new Random();
	private final String name;
	private final List<DungeonBase> dungeons;
	private int distance;
	private int spread;
	private double rarityFactor;
	private int checkRadiusInChunks;
	private int chance;
	private int priority;
	private int offset;
	private int id;
	private int seed;

	public DungeonGrid(final String name, Properties properties) {
		this.SCANNED_PROPERTIES_FILE = DungeonBase.writePropertiesToString(properties);
		
		this.name = name;
		this.distance = PropertyFileHelper.getIntProperty(properties, "distance", 20);
		this.spread = PropertyFileHelper.getIntProperty(properties, "spread", 10);
		this.rarityFactor = PropertyFileHelper.getDoubleProperty(properties, "rarityFactor", 0.0D);
		this.checkRadiusInChunks = PropertyFileHelper.getIntProperty(properties, "checkRadius", 4);
		this.seed = PropertyFileHelper.getIntProperty(properties, "seed", 1234567890);
		this.chance = PropertyFileHelper.getIntProperty(properties, "chance", 100);
		this.priority = PropertyFileHelper.getIntProperty(properties, "priority", 10);
		this.offset = PropertyFileHelper.getIntProperty(properties, "offset", 0);
		this.dungeons = Arrays.stream(PropertyFileHelper.getStringArrayProperty(properties, "dungeons", new String[0], true)).map(s -> DungeonRegistry.getInstance().getDungeon(s)).filter(Objects::nonNull).collect(Collectors.toList());
		this.dungeons.forEach(dun -> {
			dun.assignGrid(this);
		});
	}

	@Nullable
	public DungeonBase getDungeonAt(ServerWorld world, int chunkX, int chunkZ) {
		return this.getDungeonAt(world, chunkX, chunkZ, false);
	}
	
	@Nullable
	public DungeonBase getDungeonAt(ServerWorld world, int chunkX, int chunkZ, boolean ignoreGridCheck) {
		BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
		
		Biome biome = world.getBiome(pos);
		return this.getDungeonAt(world, chunkX, chunkZ, ignoreGridCheck, biome);
	}
	
	@Nullable
	public DungeonBase getDungeonAt(ServerWorld world, int chunkX, int chunkZ, boolean ignoreGridCheck, Biome biome) {
		if(biome == null) {
			return null;
		}
		Random random = WorldDungeonGenerator.getRandomForCoords(world.getSeed(), chunkX, chunkZ);
		if(ignoreGridCheck) {
			if (!this.canSpawnDungeonAtCoordsIgnoreGridCheck(world, chunkX, chunkZ, random)) {
				return null;
			}
		} else {
			if (!this.canSpawnDungeonAtCoords(world, chunkX, chunkZ, random)) {
				return null;
			}
		}

		BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
		
		CQRWeightedRandom<DungeonBase> possibleDungeons = this.getDungeonsForPos(world, biome, pos);
		DungeonBase dungeon = possibleDungeons.next(random);
		if (dungeon == null) {
			RegistryKey<Biome> rk = RegistryKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());
			log(world, chunkX, chunkZ, "Could not find any dungeon for biome: %s (%s)", biome, BiomeDictionary.getTypes(rk));
			return null;
		}

		int weight = dungeon.getWeight();
		int totalWeight = possibleDungeons.getTotalWeight();
		double chanceModifier = 1.0D / Math.pow((double) weight / (double) totalWeight, this.rarityFactor);
		if (!DungeonGenUtils.percentageRandom(dungeon.getChance() / 100.0D * chanceModifier, random)) {
			log(world, chunkX, chunkZ, "Specific dungeon generation chance check failed for dungeon: %s", dungeon);
			return null;
		}

		return dungeon;
	}

	/**
	 * Checks if<br>
	 * - the chunk coords are on the dungeon grid<br>
	 * - this chunk is far away enough from the spawn<br>
	 * - grid chance is fulfilled<br>
	 * - other structures are far away enough
	 * 
	 * @return true when dungeon can be spawned in this chunk
	 */
	public boolean canSpawnDungeonAtCoords(World world, int chunkX, int chunkZ, Random random) {
		// Check if the chunk is on the grid
		if (!this.isChunkOnGrid(world, chunkX, chunkZ)) {
			return false;
		}

		return this.canSpawnDungeonAtCoordsIgnoreGridCheck(world, chunkX, chunkZ, random);
	}
	
	public boolean canSpawnDungeonAtCoordsIgnoreGridCheck(World world, int chunkX, int chunkZ, Random random) {
		if (!DungeonGenUtils.isFarAwayEnoughFromSpawn(world, chunkX, chunkZ)) {
			log(world, chunkX, chunkZ, "Too near to spawn point");
			return false;
		}

		if (!DungeonGenUtils.percentageRandom(this.chance, random)) {
			log(world, chunkX, chunkZ, "Grid dungeon generation chance check failed");
			return false;
		}

		return !this.isOtherStructureNearby(world, chunkX, chunkZ);
	}

	/**
	 * @return true when the passed chunk coords are on the dungeon grid
	 */
	public boolean isChunkOnGrid(World world, int chunkX, int chunkZ) {
		int dungeonSeparation = this.getDistance();
		// Check whether this chunk is farther north than the wall
		if (CQRConfig.SERVER_CONFIG.wall.enabled.get() && chunkZ < -CQRConfig.SERVER_CONFIG.wall.distance.get() && CQRConfig.SERVER_CONFIG.general.moreDungeonsBehindWall.get()) {
			dungeonSeparation = MathHelper.ceil(dungeonSeparation / CQRConfig.SERVER_CONFIG.general.densityBehindWallFactor.get());
		}
		int dungeonSpread = Math.min(this.getSpread() + 1, dungeonSeparation);

		int cx = chunkX + this.offset - (DungeonGenUtils.getSpawnX(world) >> 4);
		int cz = chunkZ + this.offset - (DungeonGenUtils.getSpawnZ(world) >> 4);
		if (dungeonSpread <= 1) {
			return cx % dungeonSeparation == 0 && cz % dungeonSeparation == 0;
		}

		int x = Math.floorDiv(cx, dungeonSeparation);
		int z = Math.floorDiv(cz, dungeonSeparation);
		Random random = world.getRandom();//OLD: world.setRandomSeed(x, z, this.seed);
		//New cause 1.16 removed that method:
		long seed = 0;
		if(world instanceof ServerWorld) {
			seed = ((ServerWorld) world).getSeed();
		}
		long lTmp = (long)x * 341873128712L + (long)z * 132897987541L + seed + (long)this.seed;
		random.setSeed(lTmp);
		
		x *= dungeonSeparation;
		z *= dungeonSeparation;
		x += random.nextInt(dungeonSpread);
		z += random.nextInt(dungeonSpread);
		return x == cx && z == cz;
	}
	
	public ChunkPos getPotentialChunkPosAtOrNear(World world, int chunkX, int chunkZ) {
		int dungeonSeparation = this.getDistance();
		// Check whether this chunk is farther north than the wall
		if (CQRConfig.SERVER_CONFIG.wall.enabled.get() && chunkZ < -CQRConfig.SERVER_CONFIG.wall.distance.get() && CQRConfig.SERVER_CONFIG.general.moreDungeonsBehindWall.get()) {
			dungeonSeparation = MathHelper.ceil(dungeonSeparation / CQRConfig.SERVER_CONFIG.general.densityBehindWallFactor.get());
		}
		int dungeonSpread = Math.min(this.getSpread() + 1, dungeonSeparation);

		int cx = chunkX + this.offset - (DungeonGenUtils.getSpawnX(world) >> 4);
		int cz = chunkZ + this.offset - (DungeonGenUtils.getSpawnZ(world) >> 4);
		if (dungeonSpread <= 1) {
			if(cx % dungeonSeparation == 0 && cz % dungeonSeparation == 0) {
				return new ChunkPos(cx, cz);
			}
		}

		int x = Math.floorDiv(cx, dungeonSeparation);
		int z = Math.floorDiv(cz, dungeonSeparation);
		Random random = world.getRandom();//OLD: world.setRandomSeed(x, z, this.seed);
		//New cause 1.16 removed that method:
		long seed = 0;
		if(world instanceof ServerWorld) {
			seed = ((ServerWorld) world).getSeed();
		}
		long lTmp = (long)x * 341873128712L + (long)z * 132897987541L + seed + (long)this.seed;
		random.setSeed(lTmp);
		
		x *= dungeonSeparation;
		z *= dungeonSeparation;
		x += random.nextInt(dungeonSpread);
		z += random.nextInt(dungeonSpread);
		
		return new ChunkPos(x, z);
	}

	/**
	 * @return true when a location specific dungeon, a vanilla structure or a aw2 structure is nearby
	 */
	public boolean isOtherStructureNearby(World world, int chunkX, int chunkZ) {
		if(world.isClientSide) {
			return false;
		}
		// Checks if this chunk is in the "wall zone", if yes, abort
		if (DungeonGenUtils.isInWallRange(world, chunkX, chunkZ)) {
			log(world, chunkX, chunkZ, "Nearby wall in the north structure was found");
			return true;
		}

		if (!DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(world, chunkX, chunkZ, 4)) {
			log(world, chunkX, chunkZ, "Nearby location specific structure was found");
			return true;
		}

		// check for nearby CQR dungeon generated by grid with higher priority
		/*for (int x = -this.checkRadiusInChunks; x <= this.checkRadiusInChunks; x++) {
			for (int z = -this.checkRadiusInChunks; z <= this.checkRadiusInChunks; z++) {
				if (x * x + z * z > this.checkRadiusInChunks * this.checkRadiusInChunks) {
					continue;
				}
				if (WorldDungeonGenerator.getDungeonAt((ServerWorld)world, chunkX + x, chunkZ + z, grid -> grid.priority < this.priority) != null) {
					log(world, chunkX, chunkZ, "Nearby cqrepoured structure was found");
					return true;
				}
			}
		}*/

		return false;
	}

	private static void log(World world, int chunkX, int chunkZ, String message, Object... params) {
		if (!CQRConfig.SERVER_CONFIG.advanced.debugDungeonGen.get()) {
			//return;
		}
		CQRMain.logger.info("Failed to generate structure at x={} z={} dim={}: {}", (chunkX << 4) + 8, (chunkZ << 4) + 8, world.dimension().location().toString(), String.format(message, params));
	}

	private CQRWeightedRandom<DungeonBase> getDungeonsForPos(World world, Biome biome, BlockPos pos) {
		CQRWeightedRandom<DungeonBase> dungeonsForChunk = new CQRWeightedRandom<>();

		for (DungeonBase dungeon : this.dungeons) {
			if (!dungeon.canSpawnAt(world, biome, pos)) {
				continue;
			}
			dungeonsForChunk.add(dungeon, dungeon.getWeight());
		}

		return dungeonsForChunk;
	}
	
	public Set<ResourceLocation> collectBiomes() {
		Set<ResourceLocation> result = new HashSet<>();
		for(DungeonBase db : this.dungeons) {
			result.addAll(Arrays.asList(db.getAllowedBiomes()));
		}
		return result;
	}

	public String getName() {
		return this.name;
	}

	public List<DungeonBase> getDungeons() {
		return Collections.unmodifiableList(this.dungeons);
	}

	public int getDistance() {
		return this.distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSpread() {
		return this.spread;
	}

	public void setSpread(int spread) {
		this.spread = spread;
	}

	public double getRarityFactor() {
		return this.rarityFactor;
	}

	public void setRarityFactor(double rarityFactor) {
		this.rarityFactor = rarityFactor;
	}

	public int getCheckRadiusInChunks() {
		return this.checkRadiusInChunks;
	}

	public void setCheckRadiusInChunks(int checkRadiusInChunks) {
		this.checkRadiusInChunks = checkRadiusInChunks;
	}

	public int getChance() {
		return this.chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
		RANDOM.setSeed(id);
		this.seed = RANDOM.nextInt();
	}

	public int getSeed() {
		return this.seed;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Set<ResourceLocation> collectAllowedDims() {
		Set<ResourceLocation> result = new HashSet<>();
		
		for(DungeonBase db : this.dungeons) {
			if(!db.isAllowedDimsAsBlacklist()) {
				result.addAll(Arrays.asList(db.getAllowedDims()));
			}
		}
		
		return result;
	}

	public boolean isValidBiome(ResourceLocation biomeID) {
		for(DungeonBase db : this.dungeons) {
			if(db.isValidBiome(biomeID)) {
				return true;
			}
		}
		return false;
	}

}
