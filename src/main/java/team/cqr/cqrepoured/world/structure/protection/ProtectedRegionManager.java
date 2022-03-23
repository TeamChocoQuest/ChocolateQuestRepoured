package team.cqr.cqrepoured.world.structure.protection;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtectedRegionManager {

	private static final ClientProtectedRegionManager CLIENT_INSTANCE = new ClientProtectedRegionManager();
	private static final Map<DimensionType, ServerProtectedRegionManager> INSTANCES = new ConcurrentHashMap<>();

	private ProtectedRegionManager() {

	}

	@Nullable
	public static IProtectedRegionManager getInstance(World world) {
		if (world == null) {
			return null;
		}
		if (world.isClientSide) {
			return CLIENT_INSTANCE;
		}
		return INSTANCES.get(world.dimensionType());
	}

	public static void handleWorldLoad(World world) {
		if (world.isClientSide) {
			return;
		}
		INSTANCES.computeIfAbsent(world.dimensionType(), key -> new ServerProtectedRegionManager(world));
	}

	public static void handleWorldSave(World world) {
		if (world.isClientSide) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.dimensionType());
		if (manager != null) {
			manager.saveProtectedRegions();
		}
	}

	public static void handleWorldUnload(World world) {
		if (world.isClientSide) {
			return;
		}
		INSTANCES.remove(world.dimensionType());
	}

	public static void handleChunkLoad(World world, Chunk chunk) {
		if (world.isClientSide) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.dimensionType());
		if (manager != null) {
			manager.handleChunkLoad(chunk);
		}
	}

	public static void handleChunkUnload(World world, Chunk chunk) {
		if (world.isClientSide) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.dimensionType());
		if (manager != null) {
			manager.handleChunkUnload(chunk);
		}
	}

	public static void handleWorldTick(World world) {
		if (world.isClientSide) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.dimensionType());
		if (manager != null) {
			manager.handleWorldTick();
		}
	}

}
