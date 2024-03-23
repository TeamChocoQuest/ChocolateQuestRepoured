package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;

public abstract class PreparablePosInfo {

	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		if (CQRConfig.SERVER_CONFIG.advanced.structureImportMode.get()) {
			this.prepareDebug(level, pos, placement);
		} else {
			this.prepareNormal(level, pos, placement);
		}
	}

	protected abstract void prepareNormal(StructureLevel level, BlockPos pos, DungeonPlacement placement);

	protected abstract void prepareDebug(StructureLevel level, BlockPos pos, DungeonPlacement placement);

}
