package team.cqr.cqrepoured.structureprot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionData;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;

public class ProtectedRegionManager {

	private static final Int2ObjectMap<ProtectedRegionManager> INSTANCES = new Int2ObjectOpenHashMap<>();
	private final Map<UUID, ProtectedRegionContainer> protectedRegions = new HashMap<>();
	private final World world;
	private final File folder;

	public static class ProtectedRegionContainer {
		public final ProtectedRegion protectedRegion;
		public long lastTickForceLoaded;
		public final Set<Chunk> chunkSet = new HashSet<>();

		public ProtectedRegionContainer(ProtectedRegion protectedRegion) {
			this.protectedRegion = protectedRegion;
			this.lastTickForceLoaded = protectedRegion.getWorld().getTotalWorldTime();

			BlockPos p1 = protectedRegion.getStartPos();
			BlockPos p2 = protectedRegion.getEndPos();
			for (int x = p1.getX() >> 4; x <= p2.getX() >> 4; x++) {
				for (int z = p1.getZ() >> 4; z <= p2.getZ() >> 4; z++) {
					Chunk chunk = protectedRegion.getWorld().getChunkProvider().getLoadedChunk(x, z);
					if (chunk != null) {
						this.chunkSet.add(chunk);
					}
				}
			}
		}
	}

	public ProtectedRegionManager(World world) {
		this.world = world;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/protected_regions");
		} else {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/protected_regions");
		}
	}

	@Nullable
	public static ProtectedRegionManager getInstance(World world) {
		if (world.isRemote) {
			return null;
		}
		return INSTANCES.get(world.provider.getDimension());
	}

	public static void handleWorldLoad(World world) {
		if (world.isRemote) {
			return;
		}
		INSTANCES.computeIfAbsent(world.provider.getDimension(), key -> new ProtectedRegionManager(world));
	}

	public static void handleWorldSave(World world) {
		if (world.isRemote) {
			return;
		}
		ProtectedRegionManager manager = INSTANCES.get(world.provider.getDimension());
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
		ProtectedRegionManager instance = INSTANCES.get(world.provider.getDimension());
		if (instance == null) {
			return;
		}
		CapabilityProtectedRegionData capabilityProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
		capabilityProtectedRegionData.removeIf(uuid -> instance.provideProtectedRegion(uuid) == null);
	}

	public static void handleChunkUnload(World world, Chunk chunk) {
		if (world.isRemote) {
			return;
		}
		ProtectedRegionManager instance = INSTANCES.get(world.provider.getDimension());
		if (instance == null) {
			return;
		}
		for (ProtectedRegionContainer container : instance.protectedRegions.values()) {
			container.chunkSet.remove(chunk);
		}
	}

	public static void handleWorldTick(World world) {
		if (world.isRemote) {
			return;
		}
		ProtectedRegionManager instance = INSTANCES.get(world.provider.getDimension());
		if (instance == null) {
			return;
		}
		long time = world.getTotalWorldTime();
		for (Iterator<ProtectedRegionContainer> iterator = instance.protectedRegions.values().iterator(); iterator.hasNext();) {
			ProtectedRegionContainer container = iterator.next();
			if (!container.chunkSet.isEmpty()) {
				container.lastTickForceLoaded = time;
			} else if (time - container.lastTickForceLoaded > 1200) {
				if (container.protectedRegion.isDirty()) {
					instance.saveProtectedRegionToFile(container.protectedRegion);
				}
				iterator.remove();
			}
		}
	}

	@Nullable
	public ProtectedRegion provideProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.get(uuid);
		if (container != null) {
			return container.protectedRegion;
		}
		ProtectedRegion protectedRegion = this.createProtectedRegionFromFile(uuid);
		if (protectedRegion != null) {
			this.protectedRegions.put(uuid, new ProtectedRegionContainer(protectedRegion));
		}
		return protectedRegion;
	}

	@Nullable
	public ProtectedRegion getProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.get(uuid);
		return container != null ? container.protectedRegion : null;
	}

	public void addProtectedRegion(ProtectedRegion protectedRegion) {
		if (!protectedRegion.isValid()) {
			return;
		}

		if (this.protectedRegions.containsKey(protectedRegion.getUuid())) {
			CQRMain.logger.warn("Protected region with uuid {} already exists.", protectedRegion.getUuid());
			return;
		}

		BlockPos p1 = protectedRegion.getStartPos();
		BlockPos p2 = protectedRegion.getEndPos();
		for (int x = p1.getX() >> 4; x <= p2.getX() >> 4; x++) {
			for (int z = p1.getZ() >> 4; z <= p2.getZ() >> 4; z++) {
				Chunk chunk = this.world.getChunk(x, z);
				CapabilityProtectedRegionData capProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
				capProtectedRegionData.addProtectedRegionUuid(protectedRegion.getUuid());
			}
		}

		this.protectedRegions.put(protectedRegion.getUuid(), new ProtectedRegionContainer(protectedRegion));
	}

	public void removeProtectedRegion(ProtectedRegion protectedRegion) {
		this.removeProtectedRegion(protectedRegion.getUuid());
	}

	public void removeProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.remove(uuid);

		File file = new File(ProtectedRegionManager.this.folder, uuid.toString() + ".nbt");
		if (file.exists()) {
			file.delete();
		}

		if (container != null) {
			for (Chunk chunk : container.chunkSet) {
				CapabilityProtectedRegionData capProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
				capProtectedRegionData.removeProtectedRegionUuid(uuid);
			}
		}
	}

	public Iterable<ProtectedRegion> getProtectedRegions() {
		return () -> new Iterator<ProtectedRegion>() {
			private final Iterator<ProtectedRegionContainer> iterator = Collections.unmodifiableCollection(ProtectedRegionManager.this.protectedRegions.values()).iterator();

			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public ProtectedRegion next() {
				return this.iterator.next().protectedRegion;
			}
		};
	}

	public List<ProtectedRegion> getProtectedRegionsAt(BlockPos pos) {
		// load chunk which also loads all associated protected regions
		this.world.getChunk(pos);
		List<ProtectedRegion> list = new ArrayList<>();
		for (ProtectedRegionContainer container : this.protectedRegions.values()) {
			if (container.protectedRegion.isInsideProtectedRegion(pos)) {
				list.add(container.protectedRegion);
			}
		}
		return list;
	}

	private void saveProtectedRegions() {
		if (!this.folder.exists()) {
			this.folder.mkdirs();
		}

		for (Iterator<ProtectedRegionContainer> iterator = this.protectedRegions.values().iterator(); iterator.hasNext();) {
			ProtectedRegionContainer container = iterator.next();
			if (!container.protectedRegion.isValid()) {
				File file = new File(this.folder, container.protectedRegion.getUuid() + ".nbt");
				if (file.exists()) {
					file.delete();
				}
				iterator.remove();
			} else if (container.protectedRegion.isDirty()) {
				this.saveProtectedRegionToFile(container.protectedRegion);
			}
		}
	}

	private void saveProtectedRegionToFile(ProtectedRegion protectedRegion) {
		File file = new File(this.folder, protectedRegion.getUuid().toString() + ".nbt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			try (OutputStream out = new FileOutputStream(file)) {
				NBTTagCompound compound = protectedRegion.writeToNBT();
				CompressedStreamTools.writeCompressed(compound, out);
			}
		} catch (IOException e) {
			CQRMain.logger.error(String.format("Failed to write protected region to file: %s", file.getName()), e);
		}
	}

	@Nullable
	private ProtectedRegion createProtectedRegionFromFile(UUID uuid) {
		File file = new File(this.folder, uuid.toString() + ".nbt");
		if (!file.exists()) {
			return null;
		}
		try (InputStream in = new FileInputStream(file)) {
			NBTTagCompound compound = CompressedStreamTools.readCompressed(in);
			ProtectedRegion protectedRegion = new ProtectedRegion(this.world, compound);

			if (!compound.getString("version").equals(ProtectedRegion.PROTECTED_REGION_VERSION)) {
				this.saveProtectedRegionToFile(protectedRegion);
			}

			return protectedRegion;
		} catch (IOException e) {
			CQRMain.logger.error(String.format("Failed to read protected region from file: %s", file.getName()), e);
		}
		return null;
	}

}
