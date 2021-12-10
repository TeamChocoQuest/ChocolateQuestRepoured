package team.cqr.cqrepoured.integration.cubicchunks;

import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class CubicChunks {

	public static boolean isCubicWorld(World world) {
		return ((io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld) world).isCubicWorld();
	}

	/**
	 * Only works when {@link CubicChunks#isCubicWorld(World)} return true.
	 */
	public static ExtendedBlockStorage getBlockStorage(World world, int chunkX, int chunkY, int chunkZ) {
		return ((io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld) world).getCubeFromCubeCoords(chunkX, chunkY, chunkZ).getStorage();
	}

}
