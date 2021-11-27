package team.cqr.cqrepoured.util.tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.DimensionManager;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class DungeonMapTask {

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

	private final int radiusChunks;
	private final int sizeChunks;
	private final int radiusBlocks;
	private final int sizeBlocks;
	private final long seed;
	private final boolean generateBiomes;
	private DummyWorld world;
	private BufferedImage image;
	private final Progress progress = new Progress(4);
	private volatile boolean cancelled;

	public DungeonMapTask(int radiusChunks, long seed, boolean generateBiomes) {
		this.radiusChunks = radiusChunks;
		this.sizeChunks = this.radiusChunks * 2 + 1;
		this.radiusBlocks = this.radiusChunks << 4;
		this.sizeBlocks = this.sizeChunks << 4;
		this.seed = seed;
		this.generateBiomes = generateBiomes;
	}

	public CompletableFuture<Void> run() {
		this.world = DummyWorld.create(this.seed, WorldType.DEFAULT.getName(), 0);
		this.image = new BufferedImage(this.sizeBlocks, this.sizeBlocks, BufferedImage.TYPE_INT_RGB);
		return CompletableFuture.runAsync(this::exportBiomes).thenRunAsync(this::exportGrid).thenRunAsync(this::exportDungeons).thenRunAsync(this::exportImage)
				.handleAsync((v, t) -> {
					if (t != null) {
						progress.setErrored();
					}
					WorldServer world = DimensionManager.getWorld(0);
					if (world != null) {
						DimensionManager.setWorld(0, null, world.getMinecraftServer());
					}
					hardResetIntCache();
					return null;
				});
	}

	private void exportBiomes() {
		if (this.cancelled) {
			return;
		}

		if (this.generateBiomes) {
			DataBuffer dataBuffer = image.getRaster().getDataBuffer();

			for (int x = 0; x < this.sizeBlocks; x++) {
				if (this.cancelled) {
					return;
				}
				for (int z = 0; z < this.sizeBlocks; z++) {
					int i = z * this.sizeBlocks + x;
					Biome biome = world.getBiome(x - this.radiusBlocks, z - this.radiusBlocks);
					int color = color(world, biome);
					dataBuffer.setElem(i, color);
				}
				progress.setProgress((double) x / (this.sizeBlocks - 1));
			}
		}

		progress.finishStage();
	}

	private void exportGrid() {
		if (this.cancelled) {
			return;
		}

		DataBuffer dataBuffer = image.getRaster().getDataBuffer();
		int spawnX = DungeonGenUtils.getSpawnX(world) >> 4 << 4;
		int spawnZ = DungeonGenUtils.getSpawnZ(world) >> 4 << 4;
		int gridSize = 20 << 4;

		for (int x = 0; x < this.sizeBlocks; x++) {
			if (this.cancelled) {
				return;
			}
			boolean gridX = Math.floorMod(x - radiusBlocks - spawnX - 8 + 1 - (40 >> 1 << 4), gridSize) <= 1;
			for (int z = 0; z < this.sizeBlocks; z++) {
				int i = z * this.sizeBlocks + x;
				if (gridX || Math.floorMod(z - radiusBlocks - spawnZ - 8 + 1 - (40 >> 1 << 4), gridSize) <= 1) {
					dataBuffer.setElem(i, 0x0F0F0F);
				}
			}
			progress.setProgress((double) x / (sizeBlocks - 1));
		}

		for (int x = 0; x < 16; x++) {
			int ix = x + sizeBlocks + spawnX;
			if (ix < 0 || ix > sizeBlocks) {
				continue;
			}
			for (int z = 0; z < 16; z++) {
				int iz = z + sizeBlocks + spawnZ;
				if (iz < 0 || iz > sizeBlocks) {
					continue;
				}
				dataBuffer.setElem(iz * sizeBlocks + ix, 0xFF0000);
			}
		}

		progress.finishStage();
	}

	private void exportDungeons() {
		if (this.cancelled) {
			return;
		}

		DataBuffer dataBuffer = image.getRaster().getDataBuffer();
		int scale = 4;

		for (int x = -radiusChunks; x <= radiusChunks; x++) {
			if (this.cancelled) {
				return;
			}
			for (int z = -radiusChunks; z <= radiusChunks; z++) {
				// TODO adjust gui to allow modification of all grids
				DungeonBase dungeon = WorldDungeonGenerator.getDungeonAt(world, x, z);

				if (dungeon == null) {
					continue;
				}

				BufferedImage icon = icons[dungeon.getIconID()];
				int width = icon.getWidth();
				int height = icon.getHeight();
				for (int ix = -width / 2; ix < width - width / 2; ix++) {
					for (int iy = -height / 2; iy < height - height / 2; iy++) {
						int newColor = icon.getRGB(ix + width / 2, iy + height / 2);
						for (int i = 0; i < scale; i++) {
							int k = (x + radiusChunks << 4) + 8 + ix * scale + i;
							if (k < 0 || k >= sizeBlocks) {
								continue;
							}
							for (int j = 0; j < scale; j++) {
								int l = (z + radiusChunks << 4) + 8 + iy * scale + j;
								if (l < 0 || l >= sizeBlocks) {
									continue;
								}
								dataBuffer.setElem(l * sizeBlocks + k, newColor);
							}
						}
					}

					Graphics2D graphics = image.createGraphics();
					graphics.setColor(Color.BLACK);
					graphics.setFont(new Font("Arial", Font.BOLD, 24));
					graphics.drawString(dungeon.getDungeonName(), (x + radiusChunks << 4) + 8 - 9 * scale, (z + radiusChunks << 4) + 8 - 10 * scale);
				}
			}
			progress.setProgress((double) (x + radiusChunks) / sizeChunks);
		}

		progress.finishStage();
	}

	private void exportImage() {
		if (this.cancelled) {
			return;
		}

		double d = image.getWidth() * image.getHeight() * 0.045D;

		try (ImageOutputStream out = new FileImageOutputStream(new File("dungeon_map.png")) {
			@Override
			public void write(int b) throws IOException {
				super.write(b);
				progress.setProgress(streamPos / d);
			}

			@Override
			public void write(byte b[], int off, int len) throws IOException {
				super.write(b, off, len);
				progress.setProgress(streamPos / d);
			}
		}) {
			ImageIO.write(image, "png", out);
		} catch (IOException e) {
			throw new CancellationException("Failed exporting dungeon map!");
		}

		progress.finishStage();
	}

	public Progress getProgress() {
		return progress;
	}

	public void cancel() {
		this.progress.setCancelled();
		this.cancelled = true;
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
