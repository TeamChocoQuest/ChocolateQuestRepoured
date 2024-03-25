package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;

public interface IBlockInfo {

	default void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement, boolean noProcessing) {
		if (noProcessing) {
			this.prepareNoProcessing(level, pos, placement);
		} else {
			this.prepare(level, pos, placement);
		}
	}

	void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement);

	void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement);

}
