package team.cqr.cqrepoured.capability.protectedregions;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import net.minecraft.world.level.chunk.LevelChunk;

public class CapabilityProtectedRegionDataImplementation implements CapabilityProtectedRegionData {

	private final LevelChunk chunk;
	private final Set<UUID> protectedRegionUuids = new HashSet<>();
	
	@Override
	public Set<UUID> getRegionUUIDs() {
		return this.protectedRegionUuids;
	}

	public CapabilityProtectedRegionDataImplementation(LevelChunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public Stream<UUID> getProtectedRegionUuids() {
		return this.protectedRegionUuids.stream();
	}

	@Override
	public LevelChunk getChunk() {
		return this.chunk;
	}

}
