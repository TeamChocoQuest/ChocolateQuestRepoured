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

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionData;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateProtectedRegion;

public class ServerProtectedRegionManager implements IProtectedRegionManager {

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

	public ServerProtectedRegionManager(World world) {
		this.world = world;
		int dim = world.provider.getDimension();
		if (dim == 0) {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "data/CQR/protected_regions");
		} else {
			this.folder = new File(world.getSaveHandler().getWorldDirectory(), "DIM" + dim + "/data/CQR/protected_regions");
		}
	}

	public void handleChunkLoad(Chunk chunk) {
		CapabilityProtectedRegionData capabilityProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
		capabilityProtectedRegionData.removeIf(uuid -> this.provideProtectedRegion(uuid) == null);
	}

	public void handleChunkUnload(Chunk chunk) {
		for (ProtectedRegionContainer container : this.protectedRegions.values()) {
			container.chunkSet.remove(chunk);
		}
	}

	public void handleWorldTick() {
		long time = this.world.getTotalWorldTime();
		for (Iterator<ProtectedRegionContainer> iterator = this.protectedRegions.values().iterator(); iterator.hasNext();) {
			ProtectedRegionContainer container = iterator.next();
			if (!container.chunkSet.isEmpty()) {
				container.lastTickForceLoaded = time;
			} else if (time - container.lastTickForceLoaded > 600) {
				if (container.protectedRegion.needsSaving()) {
					this.saveProtectedRegionToFile(container.protectedRegion);
				}
				iterator.remove();
				CQRMain.NETWORK.sendToDimension(new SPacketUnloadProtectedRegion(container.protectedRegion.getUuid()), this.world.provider.getDimension());
				continue;
			}
			if (container.protectedRegion.needsSyncing()) {
				CQRMain.NETWORK.sendToDimension(new SPacketUpdateProtectedRegion(container.protectedRegion), this.world.provider.getDimension());
				container.protectedRegion.clearNeedsSyncing();
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
			CQRMain.NETWORK.sendToDimension(new SPacketUpdateProtectedRegion(protectedRegion), this.world.provider.getDimension());
			protectedRegion.clearNeedsSyncing();
		}
		return protectedRegion;
	}

	@Override
	@Nullable
	public ProtectedRegion getProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.get(uuid);
		return container != null ? container.protectedRegion : null;
	}

	@Override
	public void addProtectedRegion(ProtectedRegion protectedRegion) {
		if (!protectedRegion.isValid()) {
			return;
		}

		if (this.protectedRegions.containsKey(protectedRegion.getUuid())) {
			CQRMain.logger.warn("Protected region with uuid {} already exists.", protectedRegion.getUuid());
			return;
		}

		((WorldServer) this.world).addScheduledTask(() -> {
			BlockPos p1 = protectedRegion.getStartPos();
			BlockPos p2 = protectedRegion.getEndPos();
			for (int x = p1.getX() >> 4; x <= p2.getX() >> 4; x++) {
				for (int z = p1.getZ() >> 4; z <= p2.getZ() >> 4; z++) {
					Chunk chunk = this.world.getChunk(x, z);
					CapabilityProtectedRegionData capProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
					capProtectedRegionData.addProtectedRegionUuid(protectedRegion.getUuid());
				}
			}
		});

		this.protectedRegions.put(protectedRegion.getUuid(), new ProtectedRegionContainer(protectedRegion));
		CQRMain.NETWORK.sendToDimension(new SPacketUpdateProtectedRegion(protectedRegion), this.world.provider.getDimension());
		protectedRegion.clearNeedsSyncing();
	}

	@Override
	public void removeProtectedRegion(ProtectedRegion protectedRegion) {
		this.removeProtectedRegion(protectedRegion.getUuid());
	}

	@Override
	public void removeProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.remove(uuid);

		File file = new File(this.folder, uuid.toString() + ".nbt");
		if (file.exists()) {
			file.delete();
		}

		if (container != null) {
			for (Chunk chunk : container.chunkSet) {
				CapabilityProtectedRegionData capProtectedRegionData = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
				capProtectedRegionData.removeProtectedRegionUuid(uuid);
			}
			CQRMain.NETWORK.sendToDimension(new SPacketUnloadProtectedRegion(uuid), this.world.provider.getDimension());
		}
	}

	@Override
	public Iterable<ProtectedRegion> getProtectedRegions() {
		return () -> new Iterator<ProtectedRegion>() {
			private final Iterator<ProtectedRegionContainer> iterator = Collections.unmodifiableCollection(ServerProtectedRegionManager.this.protectedRegions.values()).iterator();

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

	@Override
	public List<ProtectedRegion> getProtectedRegionsAt(BlockPos pos) {
		// load chunk which also loads all associated protected regions
		Chunk chunk = this.world.getChunk(pos);
		CapabilityProtectedRegionData cap = chunk.getCapability(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, null);
		if (cap == null) {
			return Collections.emptyList();
		}
		List<ProtectedRegion> list = new ArrayList<>();
		for (UUID uuid : cap.getProtectedRegionUuids()) {
			ProtectedRegion protectedRegion = this.protectedRegions.get(uuid).protectedRegion;
			if (protectedRegion.isInsideProtectedRegion(pos)) {
				list.add(protectedRegion);
			}
		}
		return list;
	}

	@Override
	public void clearProtectedRegions() {
		for (UUID uuid : new ArrayList<>(this.protectedRegions.keySet())) {
			this.removeProtectedRegion(uuid);
		}
	}

	public void saveProtectedRegions() {
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
			} else if (container.protectedRegion.needsSaving()) {
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
