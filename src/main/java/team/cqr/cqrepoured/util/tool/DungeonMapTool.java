package team.cqr.cqrepoured.util.tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;

public class DungeonMapTool {

	private static final Object2IntMap<Biome> biomeColorCache = new Object2IntOpenHashMap<>();
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

	public static void run(int radiusC, long seedIn, int distanceIn, int spreadIn, double rarityFactorIn, boolean generateBiomes) {
		try {
			// TODO move these elsewhere
			boolean exportDungeonCounts = false;
			boolean overrideOldDungeonCounts = false;
			hardResetIntCache();

			try {
				Thread.sleep(10);
				System.gc();
				Thread.sleep(10);
				System.gc();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			long start = System.currentTimeMillis();
			long t = start;

			int sizeC = radiusC * 2 + 1;
			int radiusB = radiusC << 4;
			int sizeB = sizeC << 4;
			BufferedImage bufferedImage = null;
			DataBuffer dataBuffer = null;
			if (!exportDungeonCounts) {
				bufferedImage = new BufferedImage(sizeB, sizeB, BufferedImage.TYPE_INT_RGB);
				dataBuffer = bufferedImage.getRaster().getDataBuffer();
			}

			CQRMain.logger.info("0: {}s", (System.currentTimeMillis() - t) / 1000.0F);
			t = System.currentTimeMillis();

			DummyWorld world = DummyWorld.create(seedIn, WorldType.DEFAULT.getName(), 0);

			CQRMain.logger.info("1: {}s", (System.currentTimeMillis() - t) / 1000.0F);
			t = System.currentTimeMillis();

			if (!exportDungeonCounts) {
				int spawnX = DungeonGenUtils.getSpawnX(world) >> 4 << 4;
				int spawnZ = DungeonGenUtils.getSpawnZ(world) >> 4 << 4;
				int gridSize = distanceIn << 4;
				for (int x = 0; x < sizeB; x++) {
					boolean gridX = Math.floorMod(x - radiusB - spawnX - 8 + 1 - (40 >> 1 << 4), gridSize) <= 1;
					for (int z = 0; z < sizeB; z++) {
						int i = z * sizeB + x;
						if (gridX || Math.floorMod(z - radiusB - spawnZ - 8 + 1 - (40 >> 1 << 4), gridSize) <= 1) {
							dataBuffer.setElem(i, 0x0F0F0F);
						} else if (generateBiomes) {
							dataBuffer.setElem(i, color(world, world.getBiome(x - radiusB, z - radiusB)));
						}
					}
				}

				for (int x = 0; x < 16; x++) {
					int ix = x + radiusB + spawnX;
					if (ix < 0 || ix > sizeB) {
						continue;
					}
					for (int z = 0; z < 16; z++) {
						int iz = z + radiusB + spawnZ;
						if (iz < 0 || iz > sizeB) {
							continue;
						}
						dataBuffer.setElem(iz * sizeB + ix, 0xFF0000);
					}
				}
			}

			CQRMain.logger.info("2: {}s", (System.currentTimeMillis() - t) / 1000.0F);
			t = System.currentTimeMillis();

			Object2IntMap<DungeonBase> dungeonCountMap = new Object2IntArrayMap<>();
			int scale = 4;
			for (int x = -radiusC; x <= radiusC; x++) {
				for (int z = -radiusC; z <= radiusC; z++) {
					// TODO adjust gui to allow modification of all grids
					// WorldDungeonGenerator.setup(distanceIn, spreadIn, rarityFactorIn, false);
					DungeonBase dungeonAtPos = WorldDungeonGenerator.getDungeonAt(world, x, z);

					if (dungeonAtPos != null) {
						dungeonCountMap.put(dungeonAtPos, dungeonCountMap.getInt(dungeonAtPos) + 1);
						if (!exportDungeonCounts) {
							BufferedImage icon = icons[dungeonAtPos.getIconID()];
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
											dataBuffer.setElem(l * sizeB + k, newColor);
										}
									}
								}
							}
							Graphics2D graphics = bufferedImage.createGraphics();
							graphics.setColor(Color.BLACK);
							graphics.setFont(new Font("Arial", Font.BOLD, 24));
							graphics.drawString(dungeonAtPos.getDungeonName(), (x + radiusC << 4) + 8 - 9 * scale, (z + radiusC << 4) + 8 - 10 * scale);
						}
					}
					// Now, reset to default
					// WorldDungeonGenerator.setup(null, null, null, true);
				}
			}

			CQRMain.logger.info("3: {}s", (System.currentTimeMillis() - t) / 1000.0F);
			t = System.currentTimeMillis();

			if (!exportDungeonCounts) {
				ImageIO.write(bufferedImage, "png", new File("dungeon_map.png"));
			} else {
				File file = new File("dungeon_count.prop");
				if (!file.exists()) {
					file.createNewFile();
				}
				Properties prop = new Properties();
				if (overrideOldDungeonCounts) {
					try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
						prop.load(in);
					}
				}
				dungeonCountMap.object2IntEntrySet().forEach(e -> {
					String k = e.getKey().getDungeonName();
					prop.setProperty(k, Integer.toString(PropertyFileHelper.getIntProperty(prop, k, 0) + e.getIntValue()));
				});
				try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
					prop.store(out, null);
				}
			}

			CQRMain.logger.info("4: {}s", (System.currentTimeMillis() - t) / 1000.0F);
			CQRMain.logger.info("Total: {}s", (System.currentTimeMillis() - start) / 1000.0F);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hardResetIntCache();

			try {
				Thread.sleep(10);
				System.gc();
				Thread.sleep(10);
				System.gc();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private static void hardResetIntCache() {
		synchronized (IntCache.class) {
			IntCache.intCacheSize = 256;
			IntCache.freeSmallArrays.clear();
			IntCache.freeLargeArrays.clear();
			IntCache.inUseSmallArrays.clear();
			IntCache.inUseLargeArrays.clear();
		}
	}

	private static int color(World world, Biome biome) {
		return biomeColorCache.computeIfAbsent(biome, k -> {
			Set<Type> types = BiomeDictionary.getTypes(k);
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
			if (colors.isEmpty()) {
				return 0;
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
		});
	}

	private static int color(World world, Block block) {
		return color(world, block.getDefaultState());
	}

	private static int color(World world, IBlockState state) {
		return state.getMapColor(world, BlockPos.ORIGIN).colorValue;
	}

}
