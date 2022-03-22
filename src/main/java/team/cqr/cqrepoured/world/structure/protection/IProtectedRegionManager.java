package team.cqr.cqrepoured.world.structure.protection;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

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
