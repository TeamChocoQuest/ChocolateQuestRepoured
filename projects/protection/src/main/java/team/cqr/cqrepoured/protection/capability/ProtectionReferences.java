package team.cqr.cqrepoured.protection.capability;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public class ProtectionReferences implements INBTSerializable<LongArrayTag> {

	private final LevelChunk chunk;
	private final Set<UUID> protectedRegionUuids = new HashSet<>();

	public ProtectionReferences(LevelChunk chunk) {
		this.chunk = chunk;
	}

	public Stream<UUID> getProtectedRegionUuids() {
		return this.protectedRegionUuids.stream();
	}

	public void removeIf(Predicate<UUID> predicate) {
		Iterator<UUID> iterator = this.protectedRegionUuids.iterator();
		while (iterator.hasNext()) {
			UUID uuid = iterator.next();
			if (predicate.test(uuid)) {
				iterator.remove();
				this.chunk.setUnsaved(true);
			}
		}
	}

	public void clearProtectedRegionUuids() {
		if (!this.protectedRegionUuids.isEmpty()) {
			this.protectedRegionUuids.clear();
			this.chunk.setUnsaved(true);
		}
	}

	public boolean addProtectedRegionUuid(UUID uuid) {
		if (this.protectedRegionUuids.add(uuid)) {
			this.chunk.setUnsaved(true);
			return true;
		}
		return false;
	}

	public boolean removeProtectedRegionUuid(UUID uuid) {
		if (this.protectedRegionUuids.remove(uuid)) {
			this.chunk.setUnsaved(true);
			return true;
		}
		return false;
	}

	@Override
	public LongArrayTag serializeNBT() {
		long[] data = new long[this.protectedRegionUuids.size() << 1];
		int i = 0;
		for (UUID uuid : this.protectedRegionUuids) {
			data[i++] = uuid.getMostSignificantBits();
			data[i++] = uuid.getLeastSignificantBits();
		}
		return new LongArrayTag(data);
	}

	@Override
	public void deserializeNBT(LongArrayTag nbt) {
		this.protectedRegionUuids.clear();

		long[] data = nbt.getAsLongArray();
		int i = 0;
		for (int j = 0; j < data.length >> 1; j++) {
			this.protectedRegionUuids.add(new UUID(data[i++], data[i++]));
		}
	}

}
