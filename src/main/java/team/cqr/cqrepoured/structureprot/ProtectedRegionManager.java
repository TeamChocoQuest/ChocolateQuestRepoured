package team.cqr.cqrepoured.structureprot;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ProtectedRegionManager {

	private static final ClientProtectedRegionManager CLIENT_INSTANCE = new ClientProtectedRegionManager();
	private static final Int2ObjectMap<ServerProtectedRegionManager> INSTANCES = new Int2ObjectOpenHashMap<>();

	private ProtectedRegionManager() {

	}

	@Nullable
	public static IProtectedRegionManager getInstance(World world) {
		if(world == null) {
			return null;
		}
		if (world.isRemote) {
			return CLIENT_INSTANCE;
		}
		return INSTANCES.get(world.provider.getDimension());
	}

	public static void handleWorldLoad(World world) {
		if (world.isRemote) {
			return;
		}
		INSTANCES.computeIfAbsent(world.provider.getDimension(), key -> new ServerProtectedRegionManager(world));
	}

	public static void handleWorldSave(World world) {
		if (world.isRemote) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.provider.getDimension());
		if (manager != null) {
			manager.saveProtectedRegions();
		}
	}

	public static void handleWorldUnload(World world) {
		if (world.isRemote) {
			return;
		}
		INSTANCES.remove(world.provider.getDimension());
	}

	public static void handleChunkLoad(World world, Chunk chunk) {
		if (world.isRemote) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.provider.getDimension());
		if (manager != null) {
			manager.handleChunkLoad(chunk);
		}
	}

	public static void handleChunkUnload(World world, Chunk chunk) {
		if (world.isRemote) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.provider.getDimension());
		if (manager != null) {
			manager.handleChunkUnload(chunk);
		}
	}

	public static void handleWorldTick(World world) {
		if (world.isRemote) {
			return;
		}
		ServerProtectedRegionManager manager = INSTANCES.get(world.provider.getDimension());
		if (manager != null) {
			manager.handleWorldTick();
		}
	}

}
