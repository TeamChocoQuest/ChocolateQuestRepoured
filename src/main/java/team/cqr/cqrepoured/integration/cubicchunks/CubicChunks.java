package team.cqr.cqrepoured.integration.cubicchunks;

import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class CubicChunks {

	public static boolean isCubicWorld(World world) {
		return ((io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld) world).isCubicWorld();
	}

	/**
	 * Only works when {@link CubicChunks#isCubicWorld(World)} returns true.
	 */
	public static boolean isOutsideBuildHeight(World world, int y) {
		return y < ((ICubicWorld) world).getMinHeight() || y > ((ICubicWorld) world).getMaxHeight();
	}

	/**
	 * Only works when {@link CubicChunks#isCubicWorld(World)} returns true.
	 */
	public static ExtendedBlockStorage getBlockStorage(World world, int chunkX, int chunkY, int chunkZ) {
		return ((io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld) world).getCubeFromCubeCoords(chunkX, chunkY, chunkZ).getStorage();
	}

}
