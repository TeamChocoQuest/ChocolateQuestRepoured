package team.cqr.cqrepoured.util.tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.DimensionManager;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class DungeonMapTool {

	private static final BufferedImage[] icons = IntStream.range(0, 20).mapToObj(i -> {
		try {
			String path = new File("").getAbsolutePath();
			path = new File(path).getParent();
			return ImageIO.read(new File(path + "/dungeonMapTool/d" + i + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			return new BufferedImage(18, 18, BufferedImage.TYPE_INT_RGB);
		}
	}).toArray(BufferedImage[]::new);

	public static void run(int radiusC, long seedIn, int distanceIn, int spreadIn, double rarityFactorIn) {
		try {
			hardResetIntCache();

			try {
				Thread.sleep(500);
				System.gc();
				Thread.sleep(500);
				System.gc();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			long start = System.currentTimeMillis();
			long t = start;

			int sizeC = radiusC * 2 + 1;
			int radiusB = radiusC << 4;
			int sizeB = sizeC << 4;
			BufferedImage imageOldGen = new BufferedImage(sizeB, sizeB, BufferedImage.TYPE_INT_RGB);
			BufferedImage imageNewGen = new BufferedImage(sizeB, sizeB, BufferedImage.TYPE_INT_RGB);
			DataBuffer dataOldGen = imageOldGen.getRaster().getDataBuffer();
			DataBuffer dataNewGen = imageNewGen.getRaster().getDataBuffer();

			System.out.println(String.format("0: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			WorldSettings worldSettings = new WorldSettings(seedIn, GameType.CREATIVE, true, false, WorldType.DEFAULT);
			DummyWorld world = new DummyWorld(worldSettings, 0, -radiusB, -radiusB, sizeB, sizeB);
			world.getSpawnPoint();

			System.out.println(String.format("1: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			int spawnX = DungeonGenUtils.getSpawnX(world) >> 4 << 4;
			int spawnZ = DungeonGenUtils.getSpawnZ(world) >> 4 << 4;
			int gridSizeOldGen = 20 << 4;
			int gridSizeNewGen = distanceIn << 4;
			for (int x = 0; x < sizeB; x++) {
				boolean flagOldGen = Math.floorMod(x - radiusB - spawnX - 8 + 1, gridSizeOldGen) <= 1;
				boolean flagNewGen = Math.floorMod(x - radiusB - spawnX - 8 + 1 - (spreadIn >> 1 << 4), gridSizeNewGen) <= 1;
				for (int z = 0; z < sizeB; z++) {
					int i = z * sizeB + x;
					int biomeColor = color(world, world.getBiome(x - radiusB, z - radiusB));
					if (flagOldGen || Math.floorMod(z - radiusB - spawnZ - 8 + 1, gridSizeOldGen) <= 1) {
						dataOldGen.setElem(i, 0x0F0F0F);
					} else {
						dataOldGen.setElem(i, biomeColor);
					}
					if (flagNewGen || Math.floorMod(z - radiusB - spawnZ - 8 + 1 - (spreadIn >> 1 << 4), gridSizeNewGen) <= 1) {
						dataNewGen.setElem(i, 0x0F0F0F);
					} else {
						dataNewGen.setElem(i, biomeColor);
					}
				}
			}

			for (int x = 0; x < 16; x++) {
				int ix = x + radiusB + spawnX;
				for (int z = 0; z < 16; z++) {
					int iz = z + radiusB + spawnZ;
					int i = iz * sizeB + ix;
					if (i >= 0 && i < sizeB * sizeB) {
						dataOldGen.setElem(i, 0xFF0000);
						dataNewGen.setElem(i, 0xFF0000);
					}
				}
			}

			System.out.println(String.format("2: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			Object2IntMap<DungeonBase> dungeonCountMapOldGen = new Object2IntArrayMap<>();
			Object2IntMap<DungeonBase> dungeonCountMapNewGen = new Object2IntArrayMap<>();

			int scale = 4;
			for (int x = -radiusC; x <= radiusC; x++) {
				for (int z = -radiusC; z <= radiusC; z++) {
					WorldDungeonGenerator.setup(20, 0, 0.0D, false);
					DungeonBase dungeonAtPosOldGen = WorldDungeonGenerator.getDungeonAt(world, x, z);
					WorldDungeonGenerator.setup(distanceIn, spreadIn, rarityFactorIn, false);
					DungeonBase dungeonAtPosNewGen = WorldDungeonGenerator.getDungeonAt(world, x, z);

					if (dungeonAtPosOldGen != null) {
						dungeonCountMapOldGen.put(dungeonAtPosOldGen, dungeonCountMapOldGen.getInt(dungeonAtPosOldGen) + 1);
						BufferedImage icon = icons[dungeonAtPosOldGen.getIconID()];
						int width = icon.getWidth();
						int height = icon.getHeight();
						for (int ix = -width / 2; ix < width - width / 2; ix++) {
							for (int iy = -height / 2; iy < height - height / 2; iy++) {
								int newColor = icon.getRGB(ix + width / 2, iy + height / 2);
								for (int i = 0; i < scale; i++) {
									int k = (x + radiusC << 4) + 8 + ix * scale + i;
									if (k < 0 || k >= sizeB) {
										continue;
									}
									for (int j = 0; j < scale; j++) {
										int l = (z + radiusC << 4) + 8 + iy * scale + j;
										if (l < 0 || l >= sizeB) {
											continue;
										}
										dataOldGen.setElem(l * sizeB + k, newColor);
									}
								}
							}
						}
						Graphics2D graphics = imageOldGen.createGraphics();
						graphics.setColor(Color.BLACK);
						graphics.setFont(new Font("Arial", Font.BOLD, 24));
						graphics.drawString(dungeonAtPosOldGen.getDungeonName(), (x + radiusC << 4) + 8 - 9 * scale, (z + radiusC << 4) + 8 - 10 * scale);
					}
					if (dungeonAtPosNewGen != null) {
						dungeonCountMapNewGen.put(dungeonAtPosNewGen, dungeonCountMapNewGen.getInt(dungeonAtPosNewGen) + 1);
						BufferedImage icon = icons[dungeonAtPosNewGen.getIconID()];
						int width = icon.getWidth();
						int height = icon.getHeight();
						for (int ix = -width / 2; ix < width - width / 2; ix++) {
							for (int iy = -height / 2; iy < height - height / 2; iy++) {
								int newColor = icon.getRGB(ix + width / 2, iy + height / 2);
								for (int i = 0; i < scale; i++) {
									int k = (x + radiusC << 4) + 8 + ix * scale + i;
									if (k < 0 || k >= sizeB) {
										continue;
									}
									for (int j = 0; j < scale; j++) {
										int l = (z + radiusC << 4) + 8 + iy * scale + j;
										if (l < 0 || l >= sizeB) {
											continue;
										}
										dataNewGen.setElem(l * sizeB + k, newColor);
									}
								}
							}
						}
						Graphics2D graphics = imageNewGen.createGraphics();
						graphics.setColor(Color.BLACK);
						graphics.setFont(new Font("Arial", Font.BOLD, 24));
						graphics.drawString(dungeonAtPosNewGen.getDungeonName(), (x + radiusC << 4) + 8 - 9 * scale, (z + radiusC << 4) + 8 - 10 * scale);
					}
				}
			}

			System.out.println(String.format("3: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			ImageIO.write(imageOldGen, "png", new File("old.png"));
			ImageIO.write(imageNewGen, "png", new File("new.png"));

			System.out.println(String.format("4: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			System.out.println(String.format("Total: %.1fs", (System.currentTimeMillis() - start) / 1000.0F));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hardResetIntCache();

			try {
				Thread.sleep(500);
				System.gc();
				Thread.sleep(500);
				System.gc();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private static void hardResetIntCache() {
		new ReflectionField<Integer>(IntCache.class, "", "intCacheSize").set(null, 256);
		new ReflectionField<List<int[]>>(IntCache.class, "", "freeSmallArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "inUseSmallArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "freeLargeArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "inUseLargeArrays").get(null).clear();
	}

	private static int color(World world, Biome biome) {
		Set<Type> types = BiomeDictionary.getTypes(biome);
		IntList colors = new IntArrayList();
		if (types.contains(Type.VOID)) {
			colors.add(0x0F0F0F);
		}
		if (types.contains(Type.END)) {
			colors.add(color(world, Blocks.END_STONE));
		}
		if (types.contains(Type.NETHER)) {
			colors.add(color(world, Blocks.NETHERRACK));
		}
		if (types.contains(Type.MUSHROOM)) {
			colors.add(color(world, Blocks.BROWN_MUSHROOM_BLOCK));
		}
		if (types.contains(Type.WATER)) {
			colors.add(color(world, Blocks.WATER));
		}
		if (types.contains(Type.BEACH)) {
			colors.add(color(world, Blocks.SAND));
		}
		if (types.contains(Type.SNOWY)) {
			colors.add(color(world, Blocks.SNOW));
		}
		if (types.contains(Type.MESA)) {
			colors.add(color(world, Blocks.RED_SANDSTONE));
		}
		if (types.contains(Type.SANDY)) {
			colors.add(color(world, Blocks.SAND));
		}
		if (types.contains(Type.SWAMP)) {
			colors.add(0x5F7F00);
		}
		if (types.contains(Type.SAVANNA)) {
			colors.add(0xBF9F00);
		}
		if (types.contains(Type.CONIFEROUS)) {
			colors.add(0x004C00);
		}
		if (types.contains(Type.JUNGLE)) {
			colors.add(0x00CC00);
		}
		if (types.contains(Type.FOREST)) {
			colors.add(0x007C00);
		}
		if (types.contains(Type.MOUNTAIN)) {
			colors.add(color(world, Blocks.STONE));
		}
		if (types.contains(Type.PLAINS)) {
			colors.add(color(world, Blocks.GRASS));
		}
		int r = 0;
		int g = 0;
		int b = 0;
		for (int i = 0; i < colors.size(); i++) {
			int c = colors.getInt(i);
			r += (c >> 16) & 0xFF;
			g += (c >> 8) & 0xFF;
			b += c & 0xFF;
		}
		r /= colors.size();
		g /= colors.size();
		b /= colors.size();
		return (r << 16) | (g << 8) | b;
	}

	private static int color(World world, Block block) {
		return color(world, block.getDefaultState());
	}

	private static int color(World world, IBlockState state) {
		return state.getMapColor(world, BlockPos.ORIGIN).colorValue;
	}

	public static class DummyWorld extends World {

		private final Biome[] biomes;
		public final int x;
		public final int z;
		public final int w;
		public final int l;

		protected DummyWorld(WorldSettings settings, int dimension, int x, int z, int w, int l) {
			super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), DimensionManager.getProviderType(dimension).createDimension(), new Profiler(),
					true);
			this.provider.setWorld(this);
			this.biomes = this.provider.getBiomeProvider().getBiomes(null, x, z, w, l, false);
			this.x = x;
			this.z = z;
			this.w = w;
			this.l = l;
			this.createSpawnPosition(settings);
		}

		@Override
		protected IChunkProvider createChunkProvider() {
			return null;
		}

		@Override
		protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
			return false;
		}

		private void createSpawnPosition(WorldSettings settings) {
			if (!this.provider.canRespawnHere()) {
				this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
			} else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
				this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
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

		public boolean canCoordinateBeSpawn(int x, int z) {
			BlockPos blockpos = new BlockPos(x, 0, z);

			if (this.getBiome(blockpos).ignorePlayerSpawnSuitability()) {
				return true;
			} else {
				return true;
			}
		}

		@Override
		public Biome getBiome(BlockPos pos) {
			int i = (pos.getZ() - this.z) * this.l + (pos.getX() - this.x);
			return i >= 0 && i < this.biomes.length ? this.biomes[i] : Biomes.PLAINS;
		}

		public Biome getBiome(int x, int z) {
			int i = (z - this.z) * this.l + (x - this.x);
			return i >= 0 && i < this.biomes.length ? this.biomes[i] : Biomes.PLAINS;
		}

	}

}
