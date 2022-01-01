package team.cqr.cqrepoured.world.structure.generation.generation.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.world.structure.generation.generation.ChunkInfo;

public class SkyLightUtil {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();

	public static void checkSkyLight(World world, ChunkInfo chunkInfo) {
		if (!world.provider.hasSkyLight()) {
			return;
		}
		if (!world.isAreaLoaded(MUTABLE.setPos(chunkInfo.getChunkX() << 4, 0, chunkInfo.getChunkZ() << 4), 16)) {
			return;
		}
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		for (int chunkY = chunkInfo.topMarked(); chunkY >= 0; chunkY--) {
			ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int heightmap = chunk.getHeightMap()[(z << 4) | x];
					for (int y = 15; y >= 0; y--) {
						if ((chunkY << 4) + y >= heightmap) {
							continue;
						}
						MUTABLE.setPos((chunk.x << 4) + x, (chunkY << 4) + y, (chunk.z << 4) + z);
						if (blockStorage != Chunk.NULL_BLOCK_STORAGE) {
							IBlockState state = blockStorage.get(x, y, z);
							if (state.getLightOpacity(world, MUTABLE) >= 15) {
								// blockStorage.setSkyLight(x, y, z, 0);
								continue;
							}
						}
						world.checkLightFor(EnumLightType.SKY, MUTABLE);
					}
				}
			}
		}
	}

}
