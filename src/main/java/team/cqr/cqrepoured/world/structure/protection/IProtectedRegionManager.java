package team.cqr.cqrepoured.world.structure.protection;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public interface IProtectedRegionManager {

	@Nullable
	ProtectedRegion getProtectedRegion(UUID uuid);

	void addProtectedRegion(ProtectedRegion protectedRegion);

	void removeProtectedRegion(ProtectedRegion protectedRegion);

	void removeProtectedRegion(UUID uuid);

	Iterable<ProtectedRegion> getProtectedRegions();

	List<ProtectedRegion> getProtectedRegionsAt(BlockPos pos);

	void clearProtectedRegions();

}
