package team.cqr.cqrepoured.gentest.util;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import scala.actors.threadpool.Arrays;
import team.cqr.cqrepoured.gentest.ChunkInfo;

public class BlockLightUtil {

	/**
	 * 4 bit for light level<br>
	 * 6 bit for x (relative to position that gets update +14)<br>
	 * 6 bit for y (relative to position that gets update +14)<br>
	 * 6 bit for z (relative to position that gets update +14)<br>
	 * LLLLXXXXXXYYYYYYZZZZZZ
	 */
	private static final IntPriorityQueue QUEUE = new IntArrayFIFOQueue();
	private static final boolean[] USED = new boolean[29 * 29 * 29];
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
						IBlockState state = blockStorage.get(x, y, z);
						if (state.getLightValue(world, MUTABLE) <= 0) {
							continue;
						}
						BlockLightUtil.relightBlock(world, MUTABLE.toImmutable());
					}
				}
			}
		});
	}

	/**
	 * Only works for light emitting blocks!
	 */
	public static void relightBlock(World world, BlockPos pos) {
		int initialLight = world.getBlockState(pos).getLightValue(world, pos);
		if (world.getLightFor(EnumSkyBlock.BLOCK, pos) < initialLight) {
			world.setLightFor(EnumSkyBlock.BLOCK, pos, initialLight);
		}
		QUEUE.enqueue((initialLight << 18) | (14 << 12) | (14 << 6) | 14);

		while (!QUEUE.isEmpty()) {
			int lxyz = QUEUE.dequeueInt();
			int light = lxyz >> 18;
			int x = (lxyz >> 12) & 31;
			int y = (lxyz >> 6) & 31;
			int z = lxyz & 31;
			MUTABLE1.setPos(pos.getX() + x - 14, pos.getY() + y - 14, pos.getZ() + z - 14);

			for (EnumFacing facing : EnumFacing.VALUES) {
				try {
					MUTABLE1.move(facing);
					int index = ((x + facing.getXOffset()) * 29 + (y + facing.getYOffset())) * 29 + (z + facing.getZOffset());
					if (USED[index]) {
						continue;
					}
					USED[index] = true;
					if (world.isOutsideBuildHeight(MUTABLE1)) {
						continue;
					}
					Chunk chunk = world.getChunk(MUTABLE1.getX() >> 4, MUTABLE1.getZ() >> 4);
					ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[MUTABLE1.getY() >> 4];
					if (blockStorage == Chunk.NULL_BLOCK_STORAGE) {
						blockStorage = new ExtendedBlockStorage(MUTABLE1.getY() >> 4 << 4, world.provider.hasSkyLight());
						chunk.getBlockStorageArray()[MUTABLE1.getY() >> 4] = blockStorage;
					}
					int opacity = Math.max(blockStorage.get(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15).getLightOpacity(world, MUTABLE1),
							1);
					int newLight = light - opacity;
					if (blockStorage.getBlockLight(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15) >= newLight) {
						continue;
					}
					blockStorage.setBlockLight(MUTABLE1.getX() & 15, MUTABLE1.getY() & 15, MUTABLE1.getZ() & 15, newLight);
					chunk.markDirty();
					if (newLight <= 1) {
						continue;
					}
					int newPos = ((x + facing.getXOffset()) << 12) | ((y + facing.getYOffset()) << 6) | (z + facing.getZOffset());
					QUEUE.enqueue((newLight << 18) | newPos);
				} finally {
					MUTABLE1.move(facing.getOpposite());
				}
			}
		}

		Arrays.fill(USED, false);
	}

}
