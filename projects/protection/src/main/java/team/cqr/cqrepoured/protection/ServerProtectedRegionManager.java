package team.cqr.cqrepoured.protection;

import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.io.FileIOUtil;
import team.cqr.cqrepoured.common.io.IOConsumer;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.protection.capability.ProtectionReferences;
import team.cqr.cqrepoured.protection.capability.ProtectionReferencesProvider;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketUpdateProtectedRegion;

public class ServerProtectedRegionManager implements IProtectedRegionManager {
	
    private static final String FOLDER_PATH = "protected_regions";
    private static final String ENTITY_REFERENCES_FILE_NAME = "entityReferences.data";

	private final Map<UUID, ProtectedRegionContainer> protectedRegions = new HashMap<>();
	private final Multimap<UUID, UUID> entity2protectedRegion = MultimapBuilder.hashKeys()
			.hashSetValues()
			.build();
	private final ServerLevel level;
	private final File entityReferenceFile;
	private int time;

	public static class ProtectedRegionContainer {

		private final ProtectedRegion protectedRegion;
		private final Collection<LevelChunk> loadedChunks;
		private int lastTickForceLoaded;

		public ProtectedRegionContainer(ProtectedRegion protectedRegion, Collection<LevelChunk> loadedChunks, int time) {
			this.protectedRegion = protectedRegion;
			this.loadedChunks = loadedChunks;
			this.lastTickForceLoaded = time;
		}

		public ProtectedRegion getProtectedRegion() {
			return this.protectedRegion;
		}

		public boolean hasLoadedChunk() {
			return !this.loadedChunks.isEmpty();
		}

		public void addLoadedChunk(LevelChunk chunk) {
			this.loadedChunks.add(chunk);
		}

		public void removeLoadedChunk(LevelChunk chunk) {
			this.loadedChunks.remove(chunk);
		}

		public int getLastTickForceLoaded() {
			return this.lastTickForceLoaded;
		}

		public void setLastTickForceLoaded(int lastTickForceLoaded) {
			this.lastTickForceLoaded = lastTickForceLoaded;
		}

	}

	public ServerProtectedRegionManager(ServerLevel level) {
		this.level = level;
		this.entityReferenceFile = FileIOUtil.getCQRDataFile(level, FOLDER_PATH + "/" + ENTITY_REFERENCES_FILE_NAME);
		this.loadEntityReferences();
	}

	public void handleLevelSave() {
		this.saveEntityReferences();

		this.protectedRegions.values()
				.stream()
				.map(ProtectedRegionContainer::getProtectedRegion)
				.filter(((Predicate<ProtectedRegion>) ProtectedRegion::isValid).negate())
				.collect(Collectors.toList())
				.forEach(this::removeProtectedRegion);

		this.protectedRegions.values()
				.stream()
				.map(ProtectedRegionContainer::getProtectedRegion)
				.filter(ProtectedRegion::isValid)
				.filter(ProtectedRegion::needsSaving)
				.forEach(protectedRegion -> {
					this.saveProtectedRegionToFile(protectedRegion);
					protectedRegion.clearNeedsSaving();
				});
	}

	public void handleChunkLoad(LevelChunk chunk) {
		ProtectionReferences protectedRegionData = ProtectionReferencesProvider.get(chunk);

		protectedRegionData.removeIf(uuid -> this.getProtectedRegion(uuid) == null);

		protectedRegionData.getProtectedRegionUuids()
				.map(this.protectedRegions::get)
				.filter(Objects::nonNull)
				.forEach(container -> container.addLoadedChunk(chunk));
	}

	public void handleChunkUnload(LevelChunk chunk) {
		ProtectionReferences protectedRegionData = ProtectionReferencesProvider.get(chunk);

		protectedRegionData.getProtectedRegionUuids()
				.map(this.protectedRegions::get)
				.filter(Objects::nonNull)
				.forEach(container -> container.removeLoadedChunk(chunk));
	}

	public void handleLevelTick() {
		this.time++;

		Collection<UUID> toRemove = new ArrayList<>();
		Collection<UUID> toUnload = new ArrayList<>();

		for (ProtectedRegionContainer container : this.protectedRegions.values()) {
			ProtectedRegion protectedRegion = container.getProtectedRegion();
			if (!protectedRegion.isValid()) {
				toRemove.add(protectedRegion.uuid());
				return;
			}

			if (!container.hasLoadedChunk() && this.time - container.getLastTickForceLoaded() > 600) {
				toUnload.add(protectedRegion.uuid());
				return;
			}

			if (container.hasLoadedChunk()) {
				container.setLastTickForceLoaded(this.time);
			}

			if (protectedRegion.needsSyncing()) {
				CQRServices.NETWORK.send(PacketDistributor.DIMENSION.with(this.level::dimension), new SPacketUpdateProtectedRegion(protectedRegion));
				protectedRegion.clearNeedsSyncing();
			}
		}

		toRemove.forEach(this::removeProtectedRegion);
		toUnload.forEach(this::unloadProtectedRegion);
	}

	public void handleEntityKilled(Entity entity) {
		Collection<UUID> protectedRegions = this.entity2protectedRegion.removeAll(entity.getUUID());
		if (protectedRegions == null) {
			return;
		}

		protectedRegions.stream()
				.map(this::getProtectedRegion)
				.filter(Objects::nonNull)
				.forEach(protectedRegion -> protectedRegion.removeEntityDependency(entity.getUUID()));
	}

	public void handleBlockDestroyed(BlockPos pos) {
		this.getProtectedRegionsAt(pos)
				.forEach(protectedRegion -> protectedRegion.removeBlockDependency(pos));
	}

	@Override
	@Nullable
	public ProtectedRegion getProtectedRegion(UUID uuid) {
		ProtectedRegion protectedRegion = this.loadedProtectedRegion(uuid);
		if (protectedRegion == null) {
			protectedRegion = this.loadProtectedRegion(uuid);
		}

		return protectedRegion;
	}

	@Override
	public void addProtectedRegion(ProtectedRegion protectedRegion) {
		if (!protectedRegion.isValid()) {
			return;
		}

		if (this.protectedRegions.containsKey(protectedRegion.uuid())) {
			CQRepoured.LOGGER.warn("Protected region with uuid {} already exists.", protectedRegion.uuid());
			return;
		}

		ProtectedRegionContainer container = this.createContainer(protectedRegion);
		this.protectedRegions.put(protectedRegion.uuid(), container);

		protectedRegion.chunkArea()
				.map(chunkPos -> this.level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false))
				.filter(Objects::nonNull)
				.map(LevelChunk.class::cast)
				.map(ProtectionReferencesProvider::get)
				.forEach(protectedRegionData -> protectedRegionData.addProtectedRegionUuid(protectedRegion.uuid()));

		protectedRegion.getEntityDependencies()
				.forEach(entity -> this.entity2protectedRegion.put(entity, protectedRegion.uuid()));

		CQRServices.NETWORK.send(PacketDistributor.DIMENSION.with(this.level::dimension), new SPacketUpdateProtectedRegion(protectedRegion));
		protectedRegion.clearNeedsSyncing();
	}

	@Override
	public void removeProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.remove(uuid);

		this.deleteProtectedRegionFile(uuid);
		if (container != null) {
			ProtectedRegion protectedRegion = container.getProtectedRegion();

			protectedRegion.chunkArea()
					.map(chunkPos -> this.level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false))
					.filter(Objects::nonNull)
					.map(LevelChunk.class::cast)
					.map(ProtectionReferencesProvider::get)
					.forEach(protectedRegionData -> protectedRegionData.removeProtectedRegionUuid(protectedRegion.uuid()));

			protectedRegion.getEntityDependencies()
					.forEach(entity -> this.entity2protectedRegion.remove(entity, protectedRegion.uuid()));

			CQRServices.NETWORK.send(PacketDistributor.DIMENSION.with(this.level::dimension), new SPacketUnloadProtectedRegion(protectedRegion.uuid()));
			protectedRegion.clearNeedsSyncing();
		}
	}

	@Override
	public Stream<ProtectedRegion> getProtectedRegions() {
		return this.protectedRegions.values()
				.stream()
				.map(ProtectedRegionContainer::getProtectedRegion)
				.filter(ProtectedRegion::isValid);
	}

	@Override
	public Stream<ProtectedRegion> getProtectedRegionsAt(BlockPos pos) {
		return ProtectionReferencesProvider.get((LevelChunk) this.level.getChunk(pos))
				.getProtectedRegionUuids()
				.map(this::getProtectedRegion)
				.filter(Objects::nonNull)
				.filter(protectedRegion -> protectedRegion.isInsideProtectedRegion(pos));
	}

	@Override
	public void clearProtectedRegions() {
		this.protectedRegions.keySet()
				.stream()
				.collect(Collectors.toList())
				.forEach(this::removeProtectedRegion);
	}

	@Nullable
	private ProtectedRegion loadedProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.get(uuid);
		if (container == null) {
			return null;
		}

		ProtectedRegion protectedRegion = container.getProtectedRegion();
		if (!protectedRegion.isValid()) {
			return null;
		}

		return protectedRegion;
	}

	@Nullable
	private ProtectedRegion loadProtectedRegion(UUID uuid) {
		ProtectedRegion protectedRegion = this.loadProtectedRegionFromFile(uuid);
		if (protectedRegion == null) {
			return null;
		}

		if (!protectedRegion.isValid()) {
			this.deleteProtectedRegionFile(uuid);
			return null;
		}

		ProtectedRegionContainer container = this.createContainer(protectedRegion);
		this.protectedRegions.put(protectedRegion.uuid(), container);

		CQRServices.NETWORK.send(PacketDistributor.DIMENSION.with(this.level::dimension), new SPacketUpdateProtectedRegion(protectedRegion));
		protectedRegion.clearNeedsSyncing();

		return protectedRegion;
	}

	private void unloadProtectedRegion(UUID uuid) {
		ProtectedRegionContainer container = this.protectedRegions.remove(uuid);

		if (container != null) {
			ProtectedRegion protectedRegion = container.getProtectedRegion();
			if (protectedRegion.needsSaving()) {
				this.saveProtectedRegionToFile(protectedRegion);
				protectedRegion.clearNeedsSaving();
			}

			CQRServices.NETWORK.send(PacketDistributor.DIMENSION.with(this.level::dimension), new SPacketUnloadProtectedRegion(protectedRegion.uuid()));
			protectedRegion.clearNeedsSyncing();
		}
	}

	private ProtectedRegionContainer createContainer(ProtectedRegion protectedRegion) {
		Collection<LevelChunk> loadedChunks = protectedRegion.chunkArea()
				.map(chunkPos -> this.level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false))
				.filter(Objects::nonNull)
				.map(LevelChunk.class::cast)
				.collect(Collectors.toSet());

		return new ProtectedRegionContainer(protectedRegion, loadedChunks, this.time);
	}

	private void saveEntityReferences() {
		FileIOUtil.writeBufferedData(this.entityReferenceFile, out -> {
			out.writeInt(this.entity2protectedRegion.size());
			FileIOUtil.forEach(this.entity2protectedRegion.asMap(), (entity, regions) -> {
				out.writeLong(entity.getMostSignificantBits());
				out.writeLong(entity.getLeastSignificantBits());
				out.writeInt(regions.size());
				FileIOUtil.forEach(regions, region -> {
					out.writeLong(region.getMostSignificantBits());
					out.writeLong(region.getLeastSignificantBits());
				});
			});
		});
	}

	private void loadEntityReferences() {
		this.entity2protectedRegion.clear();

		if (!this.entityReferenceFile.exists()) {
			return;
		}

		FileIOUtil.readBufferedData(this.entityReferenceFile, (IOConsumer<DataInputStream>) in -> {
			for (int i = in.readInt(); i > 0; i--) {
				UUID entity = new UUID(in.readLong(), in.readLong());
				for (int j = in.readInt(); j > 0; j--) {
					this.entity2protectedRegion.put(entity, new UUID(in.readLong(), in.readLong()));
				}
			}
		});
	}

	private void saveProtectedRegionToFile(ProtectedRegion protectedRegion) {
		FileIOUtil.writeNBT(this.getFile(protectedRegion), (CompoundTag) protectedRegion.writeToNBT());
	}

	@Nullable
	private ProtectedRegion loadProtectedRegionFromFile(UUID uuid) {
		File file = this.getFile(uuid);
		if (!file.exists()) {
			return null;
		}

		CompoundTag compound = FileIOUtil.readNBT(file);
		ProtectedRegion protectedRegion = ProtectedRegion.readFromNBT(compound);

		/*if (!ProtectedRegion.PROTECTED_REGION_VERSION.equals(compound.getString("version"))) {
			this.saveProtectedRegionToFile(protectedRegion);
		}*/

		return protectedRegion;
	}
	
    private void deleteProtectedRegionFile(UUID uuid) {
        File file = this.getFile(uuid);
        if (file.exists()) {
            file.delete();
        }
    }

    private File getFile(ProtectedRegion protectedRegion) {
        return this.getFile(protectedRegion.uuid());
    }

    private File getFile(UUID uuid) {
        return FileIOUtil.getCQRDataFile(this.level, FOLDER_PATH + "/" + uuid + ".nbt");
    }


}
