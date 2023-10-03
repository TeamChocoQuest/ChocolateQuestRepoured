package team.cqr.cqrepoured.capability.protectedregions;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface CapabilityProtectedRegionData extends INBTSerializable<CompoundTag> {

	public Set<UUID> getRegionUUIDs();
	
	public LevelChunk getChunk();
	
	public Stream<UUID> getProtectedRegionUuids();

	public default void removeIf(Predicate<UUID> predicate) {
		Iterator<UUID> iterator = this.getRegionUUIDs().iterator();
		while (iterator.hasNext()) {
			UUID uuid = iterator.next();
			if (predicate.test(uuid)) {
				iterator.remove();
				this.getChunk().setUnsaved(true);
			}
		}
	}

	public default void clearProtectedRegionUuids() {
		if (!this.getRegionUUIDs().isEmpty()) {
			this.getRegionUUIDs().clear();
			this.getChunk().setUnsaved(true);
		}
	}

	public default boolean addProtectedRegionUuid(UUID uuid) {
		if (this.getRegionUUIDs().add(uuid)) {
			this.getChunk().setUnsaved(true);
			return true;
		}
		return false;
	}

	public default boolean removeProtectedRegionUuid(UUID uuid) {
		if (this.getRegionUUIDs().remove(uuid)) {
			this.getChunk().setUnsaved(true);
			return true;
		}
		return false;
	}
	
	@Override
	public default CompoundTag serializeNBT() {
		CompoundTag compound = new CompoundTag();
		int[] data = new int[this.getRegionUUIDs().size() * 4];
		int i = 0;
		for (UUID uuid : this.getRegionUUIDs()) {
			data[i * 4] = (int) (uuid.getMostSignificantBits() >> 32);
			data[i * 4 + 1] = (int) uuid.getMostSignificantBits();
			data[i * 4 + 2] = (int) (uuid.getLeastSignificantBits() >> 32);
			data[i * 4 + 3] = (int) uuid.getLeastSignificantBits();
			i++;
		}
		compound.put("protectedRegionUuids", new IntArrayTag(data));
		return compound;
	}

	@Override
	public default void deserializeNBT(CompoundTag compound) {
		this.getRegionUUIDs().clear();
		int[] data = compound.getIntArray("protectedRegionUuids");
		for (int i = 0; i < data.length / 4; i++) {
			this.getRegionUUIDs().add(new UUID(((long) data[i * 4] << 32) | (data[i * 4 + 1] & 0xFFFFFFFFL), ((long) data[i * 4 + 2] << 32) | (data[i * 4 + 3] & 0xFFFFFFFFL)));
		}
	}

}