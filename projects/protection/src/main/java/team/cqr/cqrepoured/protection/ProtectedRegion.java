package team.cqr.cqrepoured.protection;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.common.serialization.CodecUtil;

public record ProtectedRegion(UUID uuid, BoundingBox boundingBox, SimpleBitStorage protectionData, ProtectionSettings protectionSettings, Set<UUID> entityDependencies, Set<BlockPos> blockDependencies, UpdateInfo updateInfo) {

	private static class UpdateInfo {

		private boolean needsSaving;
		private boolean needsSyncing;

		public UpdateInfo() {
			this(false, false);
		}

		public UpdateInfo(boolean needsSaving, boolean needsSyncing) {
			this.needsSaving = needsSaving;
			this.needsSyncing = needsSyncing;
		}

	}

	public static final Codec<ProtectedRegion> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
        		UUIDUtil.CODEC.fieldOf("uuid").forGetter(ProtectedRegion::uuid),
        		BoundingBox.CODEC.fieldOf("bounding_box").forGetter(ProtectedRegion::boundingBox),
        		CodecUtil.SIMPLE_BIT_STORAGE.fieldOf("protection_data").forGetter(ProtectedRegion::protectionData),
        		ProtectionSettings.CODEC.fieldOf("protection_settings").forGetter(ProtectedRegion::protectionSettings),
        		CodecUtil.set(UUIDUtil.CODEC).fieldOf("entity_dependencies").forGetter(ProtectedRegion::entityDependencies),
        		CodecUtil.set(BlockPos.CODEC).fieldOf("block_dependencies").forGetter(ProtectedRegion::blockDependencies))
        		.apply(instance, ProtectedRegion::new);
	});

	public ProtectedRegion(BoundingBox boundingBox, SimpleBitStorage protectionData, ProtectionSettings protectionSettings, Set<UUID> entityDependencies, Set<BlockPos> blockDependencies) {
		this(Mth.createInsecureUUID(), boundingBox, protectionData, protectionSettings, entityDependencies, blockDependencies);
	}

	private ProtectedRegion(UUID uuid, BoundingBox boundingBox, SimpleBitStorage protectionData, ProtectionSettings protectionSettings, Set<UUID> entityDependencies, Set<BlockPos> blockDependencies) {
		this(uuid, boundingBox, protectionData, protectionSettings, entityDependencies, blockDependencies, new UpdateInfo());
	}

	public void markDirty() {
		this.updateInfo.needsSaving = true;
		this.updateInfo.needsSyncing = true;
	}

	public void clearNeedsSaving() {
		this.updateInfo.needsSaving = false;
	}

	public void clearNeedsSyncing() {
		this.updateInfo.needsSyncing = false;
	}

	public boolean needsSaving() {
		return this.updateInfo.needsSaving;
	}

	public boolean needsSyncing() {
		return this.updateInfo.needsSyncing;
	}

	public boolean isInsideProtectedRegion(BlockPos pos) {
		return this.boundingBox.isInside(pos);
	}

	private int index(BlockPos pos) {
		return index(this.boundingBox, pos);
	}

	private static int index(BoundingBox boundingBox, BlockPos pos) {
		if (!boundingBox.isInside(pos)) {
			return -1;
		}
		int x = pos.getX() - boundingBox.minX();
		int y = pos.getY() - boundingBox.minY();
		int z = pos.getZ() - boundingBox.minZ();
		return (x * boundingBox.getYSpan() + y) * boundingBox.getZSpan() + z;
	}

	public boolean isBreakable(BlockPos pos) {
		return this.getProtectionState(pos) != ProtectionState.PROTECTED;
	}

	public ProtectionState getProtectionState(BlockPos pos) {
		int index = this.index(pos);
		if (index == -1) {
			return ProtectionState.UNPROTECTED;
		}
		return ProtectionState.byId(this.protectionData.get(index));
	}

	public void setProtectionState(BlockPos pos, ProtectionState state) {
		int index = this.index(pos);
		if (index == -1) {
			return;
		}
		if (this.protectionData.get(index) != state.getId()) {
			this.protectionData.set(index, state.getId());
			this.markDirty();
		}
	}

	public boolean isValid() {
		return this.protectionSettings.persistent() || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty();
	}

	public Stream<ChunkPos> chunkArea() {
		return ChunkPos.rangeClosed(new ChunkPos(this.boundingBox.minX() >> 4, this.boundingBox.minZ() >> 4), new ChunkPos(this.boundingBox.maxX() >> 4, this.boundingBox.maxZ() >> 4));
	}

	public boolean preventBlockBreaking() {
		return this.protectionSettings.preventBlockBreaking();
	}

	public boolean preventBlockPlacing() {
		return this.protectionSettings.preventBlockPlacing();
	}

	public boolean preventExplosionsTNT() {
		return this.protectionSettings.preventExplosionsTNT();
	}

	public boolean preventExplosionsOther() {
		return this.protectionSettings.preventExplosionsOther();
	}

	public boolean preventFireSpreading() {
		return this.protectionSettings.preventFireSpreading();
	}

	public boolean preventEntitySpawning() {
		return this.protectionSettings.preventEntitySpawning();
	}

	public boolean persistent() {
		return this.protectionSettings.persistent();
	}

	public void addEntityDependency(UUID uuid) {
		if (this.entityDependencies.add(uuid)) {
			this.markDirty();
		}
	}

	public void removeEntityDependency(UUID uuid) {
		if (this.entityDependencies.remove(uuid)) {
			this.markDirty();
		}
	}

	public boolean isEntityDependency(UUID uuid) {
		return this.entityDependencies.contains(uuid);
	}

	public Stream<UUID> getEntityDependencies() {
		return this.entityDependencies.stream();
	}

	public void addBlockDependency(BlockPos pos) {
		if (this.blockDependencies.add(pos.immutable())) {
			this.markDirty();
		}
	}

	public void removeBlockDependency(BlockPos pos) {
		if (this.blockDependencies.remove(pos)) {
			this.markDirty();
		}
	}

	public boolean isBlockDependency(BlockPos pos) {
		return this.blockDependencies.contains(pos);
	}

	public Stream<BlockPos> getBlockDependencies() {
		return this.blockDependencies.stream();
	}

	public static class Builder {

		private BoundingBox boundingBox;
		private final ProtectionSettings protectionSettings;
		private final Set<UUID> entityDependencies;
		private final Set<BlockPos> blockDependencies;
		private final Set<BlockPos> unprotectedBlocks;

		public Builder(BlockPos dungeonPos, ProtectionSettings protectionSettings) {
			this.boundingBox = BoundingBox.fromCorners(dungeonPos, dungeonPos);
			this.protectionSettings = protectionSettings;
			this.entityDependencies = new HashSet<>();
			this.blockDependencies = new HashSet<>();
			this.unprotectedBlocks = new HashSet<>();
		}

		@SuppressWarnings("deprecation")
		public void updateBoundingBox(BlockPos pos) {
			this.boundingBox = this.boundingBox.encapsulate(pos);
		}

		public void addEntity(Entity entity) {
			this.entityDependencies.add(entity.getUUID());
		}

		public void addBlock(BlockPos pos) {
			this.blockDependencies.add(pos.immutable());
		}

		public void excludePos(BlockPos pos) {
			this.unprotectedBlocks.add(pos.immutable());
		}

		@Nullable
		public ProtectedRegion build() {
			BoundingBox boundingBox = new BoundingBox(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX(),
					this.boundingBox.maxY(), this.boundingBox.maxZ());
			int bits = Mth.ceillog2(ProtectionState.values().length);
			int size = this.boundingBox.getXSpan() * this.boundingBox.getYSpan() * this.boundingBox.getZSpan();
			SimpleBitStorage protectionData = new SimpleBitStorage(bits, size);
			this.unprotectedBlocks.forEach(pos -> protectionData.set(ProtectedRegion.index(boundingBox, pos), ProtectionState.UNPROTECTED.getId()));
			return new ProtectedRegion(boundingBox, protectionData, this.protectionSettings, Set.copyOf(this.entityDependencies),
					Set.copyOf(this.blockDependencies));
		}

	}

	public Tag writeToNBT() {
		return CODEC.encode(this, NbtOps.INSTANCE, new CompoundTag())
				.getOrThrow(false, CQRMain.logger::error);
	}

	public static ProtectedRegion readFromNBT(CompoundTag nbt) {
		return CODEC.decode(NbtOps.INSTANCE, nbt)
				.getOrThrow(false, CQRMain.logger::error)
				.getFirst();
	}

}
