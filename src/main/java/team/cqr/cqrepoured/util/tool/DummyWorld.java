package team.cqr.cqrepoured.util.tool;

import java.io.File;
import java.util.List;
import java.util.Random;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

public class DummyWorld extends WorldServer {

	private final Long2ObjectMap<Biome[]> chunkBiomeArrays = new Long2ObjectOpenHashMap<>();

	public DummyWorld(WorldSettings settings, int dimension) {
		super(new IntegratedServer(Minecraft.getMinecraft(), "dummy-world", "dummy-world", settings, null, null, null, null), new SaveHandlerMP(),
				new WorldInfo(settings, "dummy-world"), dimension, DummyProfiler.INSTANCE);
		this.provider.setWorld(this);
		this.provider.setDimension(dimension);
		this.createSpawnPositionCQR(settings);
		this.worldInfo.setServerInitialized(true);
	}

	public static DummyWorld create(long seed, String type, int dimension) {
		return new DummyWorld(new WorldSettings(seed, GameType.CREATIVE, true, false, WorldType.byName(type)), dimension);
	}

	@Override
	public File getChunkSaveLocation() {
		return new File("CQR-MapTool");
	}

	private void createSpawnPositionCQR(WorldSettings settings) {
		if (!this.provider.canRespawnHere()) {
			this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
		} else {
			this.findingSpawnPoint = true;
			BiomeProvider biomeprovider = this.provider.getBiomeProvider();
			List<Biome> list = biomeprovider.getBiomesToSpawnIn();
			Random random = new Random(this.getSeed());
			BlockPos blockpos = biomeprovider.findBiomePosition(0, 0, 256, list, random);
			int i = 8;
			int j = this.provider.getAverageGroundLevel();
			int k = 8;

			if (blockpos != null) {
				i = blockpos.getX();
				k = blockpos.getZ();
			}

			int l = 0;

			while (!this.canCoordinateBeSpawn(i, k)) {
				i += random.nextInt(64) - random.nextInt(64);
				k += random.nextInt(64) - random.nextInt(64);
				++l;

				if (l == 1000) {
					break;
				}
			}

			this.worldInfo.setSpawn(new BlockPos(i, j, k));
			this.findingSpawnPoint = false;
		}
	}

	private boolean canCoordinateBeSpawn(int x, int z) {
		Biome biome = this.getBiome(x, z);
		if (biome.ignorePlayerSpawnSuitability()) {
			return true;
		}
		return biome.topBlock.getBlock() == Blocks.GRASS;
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		return this.getBiome(pos.getX(), pos.getZ());
	}

	public Biome getBiome(int x, int z) {
		int size = 8;
		long key = ChunkPos.asLong(x >> size, z >> size);
		Biome[] chunkBiomeArray = this.chunkBiomeArrays.computeIfAbsent(key,
				k -> this.provider.getBiomeProvider().getBiomes(null, x >> size << size, z >> size << size, 1 << size, 1 << size, false));
		int x1 = x & ((1 << size) - 1);
		int z1 = z & ((1 << size) - 1);
		return chunkBiomeArray[z1 << size | x1];
	}

}
