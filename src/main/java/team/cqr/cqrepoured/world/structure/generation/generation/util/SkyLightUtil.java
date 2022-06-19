package team.cqr.cqrepoured.world.structure.generation.generation.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import team.cqr.cqrepoured.world.structure.generation.generation.ChunkInfo;

public class SkyLightUtil {

	private static final BlockPos.Mutable MUTABLE = new BlockPos.Mutable();

	public static void checkSkyLight(World world, ChunkInfo chunkInfo) {
		if (!world.provider.hasSkyLight()) {
			return;
		}
		if (!world.isAreaLoaded(MUTABLE.set(chunkInfo.getChunkX() << 4, 0, chunkInfo.getChunkZ() << 4), 16)) {
			return;
		}
		Chunk chunk = world.getChunk(chunkInfo.getChunkX(), chunkInfo.getChunkZ());
		for (int chunkY = chunkInfo.topMarked(); chunkY >= 0; chunkY--) {
			ChunkSection blockStorage = chunk.getSections()[chunkY];
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int heightmap = chunk.getHeightMap()[(z << 4) | x];
					for (int y = 15; y >= 0; y--) {
						if ((chunkY << 4) + y >= heightmap) {
							continue;
						}
						MUTABLE.set((chunk.getPos().x << 4) + x, (chunkY << 4) + y, (chunk.getPos().z << 4) + z);
						if (blockStorage != Chunk.NULL_BLOCK_STORAGE) {
							BlockState state = blockStorage.get(x, y, z);
							if (state.getLightOpacity(world, MUTABLE) >= 15) {
								// blockStorage.setSkyLight(x, y, z, 0);
								continue;
							}
						}
						world.checkLightFor(LightType.SKY, MUTABLE);
					}
				}
			}
		}
	}

}
