package team.cqr.cqrepoured.world.structure.protection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.chunk.Chunk;

public class ProtectedRegionManager {

	private static final ClientProtectedRegionManager CLIENT_INSTANCE = new ClientProtectedRegionManager();
	private static final Map<ResourceKey<Level>, ServerProtectedRegionManager> SERVER_INSTANCES = new ConcurrentHashMap<>();

	@Nullable
	public static IProtectedRegionManager getInstance(Level level) {
		if (level == null) {
			return null;
		}
		if (level.isClientSide()) {
			return CLIENT_INSTANCE;
		}
		if (!(level instanceof ServerLevel)) {
			return null;
		}
		return SERVER_INSTANCES.get(((Level) level).dimension());
	}

	public static void handleLevelLoad(Level level) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		SERVER_INSTANCES.computeIfAbsent(((Level) level).dimension(), key -> new ServerProtectedRegionManager((ServerLevel) level));
	}

	public static void handleLevelSave(Level level) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((Level) level).dimension());
		if (manager != null) {
			manager.handleLevelSave();
		}
	}

	public static void handleLevelUnload(Level level) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		SERVER_INSTANCES.remove(((Level) level).dimension());
	}

	public static void handleChunkLoad(Level level, Chunk chunk) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((Level) level).dimension());
		if (manager != null) {
			manager.handleChunkLoad(chunk);
		}
	}

	public static void handleChunkUnload(Level level, Chunk chunk) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((Level) level).dimension());
		if (manager != null) {
			manager.handleChunkUnload(chunk);
		}
	}

	public static void handleLevelTick(Level level) {
		if (!(level instanceof ServerLevel)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((Level) level).dimension());
		if (manager != null) {
			manager.handleLevelTick();
		}
	}

}
