package team.cqr.cqrepoured.structuregen.generation.util;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.structuregen.generation.ChunkInfo;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.util.IntUtil;

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
				IBlockState state = blockStorage.get(x, y, z);

				if (state.getMaterial().isLiquid()) {
					state.getBlock().onBlockAdded(world, MUTABLE, state);
				} else {
					IBlockState stateDown = y == 0 ? get(blockStorageDown, x, 15, z) : get(blockStorage, x, y - 1, z);
					if (stateDown.getBlock() == Blocks.GRASS && state.getLightOpacity(world, MUTABLE) >= 12) {
						MUTABLE.setY(MUTABLE.getY() - 1);
						BlockPlacingHelper.setBlockState(world, chunk, y == 0 ? blockStorageDown : blockStorage, MUTABLE, Blocks.DIRT.getDefaultState(), null, 2);
					}
				}
			});
		});
	}

	private static IBlockState get(@Nullable ExtendedBlockStorage blockStorage, int x, int y, int z) {
		return blockStorage != null ? blockStorage.get(x, y, z) : Blocks.AIR.getDefaultState();
	}

}
