package team.cqr.cqrepoured.world.structure.generation.generation.util;

import java.util.Arrays;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.world.structure.generation.generation.ChunkInfo;

public class BlockLightUtil {

	private static final int MAX_LIGHT_LEVEL = 15;
	/**
	 * 8 bit for light level<br>
	 * 8 bit for x (relative to position that gets update +MAX_LIGHT_LEVEL-1)<br>
	 * 8 bit for y (relative to position that gets update +MAX_LIGHT_LEVEL-1)<br>
	 * 8 bit for z (relative to position that gets update +MAX_LIGHT_LEVEL-1)<br>
	 * LLLLXXXXXXYYYYYYZZZZZZ
	 */
	private static final IntPriorityQueue QUEUE = new IntArrayFIFOQueue();
	private static final boolean[] USED = new boolean[(MAX_LIGHT_LEVEL * 2 - 1) * (MAX_LIGHT_LEVEL * 2 - 1) * (MAX_LIGHT_LEVEL * 2 - 1)];
	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private static final MutableBlockPos MUTABLE1 = new MutableBlockPos();

	public static void checkBlockLight(World world, ChunkInfo chunkInfo) {
		if (!world.isAreaLoaded(MUTABLE.setPos(chunkInfo.getChunkX() << 4, 0, chunkInfo.getChunkZ() << 4), 16)) {
			return;
		}
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		chunkInfo.forEachReversed(chunkY -> {
			ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
			if (blockStorage == Chunk.NULL_BLOCK_STORAGE || blockStorage.isEmpty()) {
				return;
			}
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = 15; y >= 0; y--) {
						MUTABLE.setPos((chunk.x << 4) + x, (chunkY << 4) + y, (chunk.z << 4) + z);
						BlockState state = blockStorage.get(x, y, z);
						if (state.getLightValue(world, MUTABLE) <= 0) {
							continue;
						}
						BlockLightUtil.relightBlock(world, MUTABLE.toImmutable());
					}
				}
			}
		});
	}

	private static int encode(int light, int x, int y, int z) {
		light = MathHelper.clamp(light, 0, MAX_LIGHT_LEVEL);
		x += MAX_LIGHT_LEVEL - 1;
		y += MAX_LIGHT_LEVEL - 1;
		z += MAX_LIGHT_LEVEL - 1;
		return (light << 24) | (x << 16) | (y << 8) | z;
	}

	private static int decodeLight(int lxyz) {
		return MathHelper.clamp(lxyz >>> 24, 0, MAX_LIGHT_LEVEL);
	}

	private static int decodeX(int lxyz) {
		return ((lxyz >>> 16) & 0xFF) - (MAX_LIGHT_LEVEL - 1);
	}

	private static int decodeY(int lxyz) {
		return ((lxyz >>> 8) & 0xFF) - (MAX_LIGHT_LEVEL - 1);
	}

	private static int decodeZ(int lxyz) {
		return (lxyz & 0xFF) - (MAX_LIGHT_LEVEL - 1);
	}

	private static boolean getAndSetUsed(int x, int y, int z) {
		x += MAX_LIGHT_LEVEL - 1;
		y += MAX_LIGHT_LEVEL - 1;
		z += MAX_LIGHT_LEVEL - 1;
		int i = (x * (MAX_LIGHT_LEVEL * 2 - 1) + y) * (MAX_LIGHT_LEVEL * 2 - 1) + z;
		if (USED[i]) {
			return true;
		}
		USED[i] = true;
		return false;
	}

	/**
	 * Only works for light emitting blocks!
	 */
	public static void relightBlock(World world, BlockPos pos) {
		int initialLight = world.getBlockState(pos).getLightValue(world, pos);
		if (world.getLightFor(LightType.BLOCK, pos) < initialLight) {
			world.setLightFor(LightType.BLOCK, pos, initialLight);
		}
		QUEUE.enqueue(encode(initialLight, 0, 0, 0));
		getAndSetUsed(0, 0, 0);

		while (!QUEUE.isEmpty()) {
			int lxyz = QUEUE.dequeueInt();
			int light = decodeLight(lxyz);
			int x = decodeX(lxyz);
			int y = decodeY(lxyz);
			int z = decodeZ(lxyz);
			MUTABLE1.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);

			for (Direction facing : Direction.VALUES) {
				try {
					MUTABLE1.move(facing);
					if (getAndSetUsed(x + facing.getXOffset(), y + facing.getYOffset(), z + facing.getZOffset())) {
						continue;
					}
					if (world.isOutsideBuildHeight(MUTABLE1)) {
						continue;
					}
					Chunk chunk = world.getChunk(MUTABLE1.getX() >> 4, MUTABLE1.getZ() >> 4);
					ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[MUTABLE1.getY() >> 4];
					if (blockStorage == Chunk.NULL_BLOCK_STORAGE) {
						blockStorage = new ExtendedBlockStorage(MUTABLE1.getY() >> 4 << 4, world.provider.hasSkyLight());
						chunk.getBlockStorageArray()[MUTABLE1.getY() >> 4] = blockStorage;
						chunk.generateSkylightMap();
					}
					int opacity = Math.max(blockStorage.get(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15).getLightOpacity(world, MUTABLE1), 1);
					int newLight = light - opacity;
					if (blockStorage.getBlockLight(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15) >= newLight) {
						continue;
					}
					blockStorage.setBlockLight(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15, newLight);
					chunk.markDirty();
					if (newLight <= 1) {
						continue;
					}
					QUEUE.enqueue(encode(newLight, x + facing.getXOffset(), y + facing.getYOffset(), z + facing.getZOffset()));
				} finally {
					MUTABLE1.move(facing.getOpposite());
				}
			}
		}

		Arrays.fill(USED, false);
	}

}
