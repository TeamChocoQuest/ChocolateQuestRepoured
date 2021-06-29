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
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.structuregen.DungeonRegistry;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class DungeonMapTool {

	private static final Random RAND = new Random();
	private static final Random RAND1 = new Random();
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

	private static long seed;
	private static int distance;
	private static int spread;
	private static double rarityDivisor;

	public static void run(int radiusC, long seedIn, int distanceIn, int spreadIn, double rarityDivisorIn) {
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

			seed = seedIn;
			distance = distanceIn;
			spread = Math.min(spreadIn + 1, distance);
			rarityDivisor = rarityDivisorIn;
			int sizeC = radiusC * 2 + 1;
			int radiusB = radiusC << 4;
			int sizeB = sizeC << 4;
			BufferedImage imageOldGen = new BufferedImage(sizeB, sizeB, BufferedImage.TYPE_INT_RGB);
			BufferedImage imageNewGen = new BufferedImage(sizeB, sizeB, BufferedImage.TYPE_INT_RGB);
			DataBuffer dataOldGen = imageOldGen.getRaster().getDataBuffer();
			DataBuffer dataNewGen = imageNewGen.getRaster().getDataBuffer();

			System.out.println(String.format("0: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			WorldSettings worldSettings = new WorldSettings(seed, GameType.CREATIVE, false, false, WorldType.DEFAULT);
			WorldInfo worldInfo = new WorldInfo(worldSettings, "test");
			BiomeProvider biomeProvider = new BiomeProvider(worldInfo);
			Biome[] biomes = biomeProvider.getBiomes(null, -radiusB, -radiusB, sizeB, sizeB, false);

			System.out.println(String.format("1: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			int gridSize = distance << 4;
			for (int x = 0; x < sizeB; x++) {
				boolean flag = Math.floorMod(x - 8 + 1, gridSize) <= 1;
				for (int z = 0; z < sizeB; z++) {
					int i = z * sizeB + x;
					if (flag || Math.floorMod(z - 8 + 1, gridSize) <= 1) {
						dataOldGen.setElem(i, 0x0F0F0F);
						dataNewGen.setElem(i, 0x0F0F0F);
					} else {
						int biomeColor = biomes[i].getGrassColorAtPos(BlockPos.ORIGIN);
						dataOldGen.setElem(i, biomeColor);
						dataNewGen.setElem(i, biomeColor);
					}
				}
			}

			System.out.println(String.format("2: %.1fs", (System.currentTimeMillis() - t) / 1000.0F));
			t = System.currentTimeMillis();

			Object2IntMap<DungeonBase> dungeonCountMapOldGen = new Object2IntArrayMap<>();
			Object2IntMap<DungeonBase> dungeonCountMapNewGen = new Object2IntArrayMap<>();

			int scale = 4;
			for (int x = -radiusC; x <= radiusC; x++) {
				int x1 = ((x + radiusC) << 4) + 8;
				for (int z = -radiusC; z <= radiusC; z++) {
					int z1 = ((z + radiusC) << 4) + 8;
					Biome biome = biomes[z1 * sizeB + x1];
					DungeonBase dungeonAtPosOldGen = canGenerateDungeonAtOldGen(biome, x, z);
					DungeonBase dungeonAtPosNewGen = canGenerateDungeonAtNewGen(biome, x, z);

					if (dungeonAtPosOldGen != null) {
						dungeonCountMapOldGen.put(dungeonAtPosOldGen,
								dungeonCountMapOldGen.getInt(dungeonAtPosOldGen) + 1);
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
						dungeonCountMapNewGen.put(dungeonAtPosNewGen,
								dungeonCountMapNewGen.getInt(dungeonAtPosNewGen) + 1);
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

	private static DungeonBase canGenerateDungeonAtOldGen(Biome biome, int chunkX, int chunkZ) {
		if (chunkX % distance != 0 || chunkZ % distance != 0) {
			return null;
		}
		RAND.setSeed(getSeed(seed, chunkX, chunkZ));
		if (!DungeonGenUtils.percentageRandom(CQRConfig.general.overallDungeonChance, RAND)) {
			return null;
		}
		CQRWeightedRandom<DungeonBase> possibleDungeons = DungeonRegistry.getInstance().getDungeonsForDimBiome(0,
				biome);
		DungeonBase dungeon = possibleDungeons.next(RAND);
		if (dungeon == null) {
			return null;
		}
		if (!DungeonGenUtils.percentageRandom((double) dungeon.getChance() / 100.0D, RAND)) {
			return null;
		}
		return dungeon;
	}

	private static DungeonBase canGenerateDungeonAtNewGen(Biome biome, int chunkX, int chunkZ) {
		if (!canSpawnStructureAtCoords(chunkX, chunkZ)) {
			return null;
		}
		RAND.setSeed(getSeed(seed, chunkX, chunkZ));
		if (!DungeonGenUtils.percentageRandom(CQRConfig.general.overallDungeonChance, RAND)) {
			return null;
		}
		CQRWeightedRandom<DungeonBase> possibleDungeons = DungeonRegistry.getInstance().getDungeonsForDimBiome(0,
				biome);
		DungeonBase dungeon = possibleDungeons.next(RAND);
		if (dungeon == null) {
			return null;
		}
		if (!DungeonGenUtils.percentageRandom((double) dungeon.getChance()
				/ Math.min(1.0D,
						rarityDivisor * (double) dungeon.getWeight() / (double) possibleDungeons.getTotalWeight())
				/ 100.0D, RAND)) {
			return null;
		}
		return dungeon;
	}

	private static long getSeed(long seed, int chunkX, int chunkZ) {
		long mix = xorShift64(chunkX) + Long.rotateLeft(xorShift64(chunkZ), 32) + -1094792450L;
		long result = xorShift64(mix);

		return seed + result;
	}

	private static long xorShift64(long x) {
		x ^= x << 21;
		x ^= x >>> 35;
		x ^= x << 4;
		return x;
	}

	public static boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		int cx = chunkX;
		int cz = chunkZ;
		if (spread <= 1) {
			return cx % distance == 0 && cz % distance == 0;
		}

		int x = MathHelper.intFloorDiv(cx, distance);
		int z = MathHelper.intFloorDiv(cz, distance);
		Random random = setRandomSeed(x, z, 10387312);
		x *= distance;
		z *= distance;
		x += random.nextInt(spread);
		z += random.nextInt(spread);
		return x == cx && z == cz;
	}

	public static Random setRandomSeed(int seedX, int seedY, int seedZ) {
		long j2 = (long) seedX * 341873128712L + (long) seedY * 132897987541L + seed + (long) seedZ;
		RAND1.setSeed(j2);
		return RAND1;
	}

	private static void hardResetIntCache() {
		new ReflectionField<Integer>(IntCache.class, "", "intCacheSize").set(null, 256);
		new ReflectionField<List<int[]>>(IntCache.class, "", "freeSmallArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "inUseSmallArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "freeLargeArrays").get(null).clear();
		new ReflectionField<List<int[]>>(IntCache.class, "", "inUseLargeArrays").get(null).clear();
	}

}
