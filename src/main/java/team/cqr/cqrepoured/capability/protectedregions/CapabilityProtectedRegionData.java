package team.cqr.cqrepoured.capability.protectedregions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.world.chunk.Chunk;

public class CapabilityProtectedRegionData {

	private final Chunk chunk;
	private final Set<UUID> protectedRegionUuids = new HashSet<>();

	public CapabilityProtectedRegionData(Chunk chunk) {
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
				this.chunk.markUnsaved();
			}
		}
	}

	public void clearProtectedRegionUuids() {
		if (!this.protectedRegionUuids.isEmpty()) {
			this.protectedRegionUuids.clear();
			this.chunk.markUnsaved();
		}
	}

	public boolean addProtectedRegionUuid(UUID uuid) {
		if (this.protectedRegionUuids.add(uuid)) {
			this.chunk.markUnsaved();
			return true;
		}
		return false;
	}

	public boolean removeProtectedRegionUuid(UUID uuid) {
		if (this.protectedRegionUuids.remove(uuid)) {
			this.chunk.markUnsaved();
			return true;
		}
		return false;
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT compound = new CompoundNBT();
		int[] data = new int[this.protectedRegionUuids.size() * 4];
		int i = 0;
		for (UUID uuid : this.protectedRegionUuids) {
			data[i * 4] = (int) (uuid.getMostSignificantBits() >> 32);
			data[i * 4 + 1] = (int) uuid.getMostSignificantBits();
			data[i * 4 + 2] = (int) (uuid.getLeastSignificantBits() >> 32);
			data[i * 4 + 3] = (int) uuid.getLeastSignificantBits();
			i++;
		}
		compound.put("protectedRegionUuids", new IntArrayNBT(data));
		return compound;
	}

	public void readFromNBT(CompoundNBT compound) {
		this.protectedRegionUuids.clear();
		int[] data = compound.getIntArray("protectedRegionUuids");
		for (int i = 0; i < data.length / 4; i++) {
			this.protectedRegionUuids.add(new UUID(((long) data[i * 4] << 32) | (data[i * 4 + 1] & 0xFFFFFFFFL), ((long) data[i * 4 + 2] << 32) | (data[i * 4 + 3] & 0xFFFFFFFFL)));
		}
	}

}
