package team.cqr.cqrepoured.world.structure.protection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.CQRMain;

public class ClientProtectedRegionManager implements IProtectedRegionManager {

	private final Map<UUID, ProtectedRegion> protectedRegions = new HashMap<>();

	@Override
	@Nullable
	public ProtectedRegion getProtectedRegion(UUID uuid) {
		return this.protectedRegions.get(uuid);
	}

	@Override
	public void addProtectedRegion(ProtectedRegion protectedRegion) {
		if (!protectedRegion.isValid()) {
			return;
		}

		if (this.protectedRegions.containsKey(protectedRegion.uuid())) {
			CQRMain.logger.warn("Protected region with uuid {} already exists.", protectedRegion.uuid());
			return;
		}

		this.protectedRegions.put(protectedRegion.uuid(), protectedRegion);
	}

	@Override
	public void removeProtectedRegion(UUID uuid) {
		this.protectedRegions.remove(uuid);
	}

	@Override
	public Stream<ProtectedRegion> getProtectedRegions() {
		return this.protectedRegions.values()
				.stream()
				.filter(ProtectedRegion::isValid);
	}

	@Override
	public Stream<ProtectedRegion> getProtectedRegionsAt(BlockPos pos) {
		return this.getProtectedRegions()
				.filter(protectedRegion -> protectedRegion.isInsideProtectedRegion(pos));
	}

	@Override
	public void clearProtectedRegions() {
		this.protectedRegions.clear();
	}

}
