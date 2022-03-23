package team.cqr.cqrepoured.world.structure.generation.generation.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.IntUtil;
import team.cqr.cqrepoured.world.structure.generation.generation.ChunkInfo;

import javax.annotation.Nullable;

public class BlockAddedUtil {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();

	public static void onBlockAdded(World world, ChunkInfo chunkInfo) {
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());

		chunkInfo.forEachReversed(chunkY -> {
			ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];

			if (blockStorage == Chunk.NULL_BLOCK_STORAGE || blockStorage.isEmpty()) {
				return;
			}

			ExtendedBlockStorage blockStorageDown = chunkY == 0 ? null : chunk.getBlockStorageArray()[chunkY - 1];

			IntUtil.forEachChunkXYZ((x, y, z) -> {
				MUTABLE.setPos((chunk.x << 4) + x, (chunkY << 4) + y, (chunk.z << 4) + z);
				BlockState state = blockStorage.get(x, y, z);

				if (state.getMaterial().isLiquid()) {
					state.getBlock().onBlockAdded(world, MUTABLE, state);
				} else {
					BlockState stateDown = y == 0 ? get(blockStorageDown, x, 15, z) : get(blockStorage, x, y - 1, z);
					if (stateDown.getBlock() == Blocks.GRASS && state.getLightOpacity(world, MUTABLE) >= 12) {
						MUTABLE.setY(MUTABLE.getY() - 1);
						BlockPlacingHelper.setBlockState(world, chunk, y == 0 ? blockStorageDown : blockStorage, MUTABLE, Blocks.DIRT.getDefaultState(), null, 2);
					}
				}
			});
		});
	}

	private static BlockState get(@Nullable ExtendedBlockStorage blockStorage, int x, int y, int z) {
		return blockStorage != null ? blockStorage.get(x, y, z) : Blocks.AIR.getDefaultState();
	}

}
