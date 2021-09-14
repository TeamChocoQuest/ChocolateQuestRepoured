package team.cqr.cqrepoured.gentest.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.gentest.ChunkInfo;

public class BlockAddedUtil {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();

	public static void onBlockAdded(World world, ChunkInfo chunkInfo) {
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
						if (!state.getMaterial().isLiquid()) {
							continue;
						}
						state.getBlock().onBlockAdded(world, MUTABLE, state);
					}
				}
			}
		});
	}

}
