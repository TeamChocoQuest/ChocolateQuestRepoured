package team.cqr.cqrepoured.world.structure.protection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class ProtectedRegionManager {

	private static final ClientProtectedRegionManager CLIENT_INSTANCE = new ClientProtectedRegionManager();
	private static final Map<RegistryKey<World>, ServerProtectedRegionManager> SERVER_INSTANCES = new ConcurrentHashMap<>();

	@Nullable
	public static IProtectedRegionManager getInstance(IWorld level) {
		if (level == null) {
			return null;
		}
		if (level.isClientSide()) {
			return CLIENT_INSTANCE;
		}
		if (!(level instanceof ServerWorld)) {
			return null;
		}
		return SERVER_INSTANCES.get(((World) level).dimension());
	}

	public static void handleLevelLoad(IWorld level) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		SERVER_INSTANCES.computeIfAbsent(((World) level).dimension(), key -> new ServerProtectedRegionManager((ServerWorld) level));
	}

	public static void handleLevelSave(IWorld level) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((World) level).dimension());
		if (manager != null) {
			manager.handleLevelSave();
		}
	}

	public static void handleLevelUnload(IWorld level) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		SERVER_INSTANCES.remove(((World) level).dimension());
	}

	public static void handleChunkLoad(IWorld level, Chunk chunk) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((World) level).dimension());
		if (manager != null) {
			manager.handleChunkLoad(chunk);
		}
	}

	public static void handleChunkUnload(IWorld level, Chunk chunk) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((World) level).dimension());
		if (manager != null) {
			manager.handleChunkUnload(chunk);
		}
	}

	public static void handleLevelTick(IWorld level) {
		if (!(level instanceof ServerWorld)) {
			return;
		}
		ServerProtectedRegionManager manager = SERVER_INSTANCES.get(((World) level).dimension());
		if (manager != null) {
			manager.handleLevelTick();
		}
	}

}
