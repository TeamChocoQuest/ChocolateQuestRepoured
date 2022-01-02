package team.cqr.cqrepoured.world.structure.generation.structurefile;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;

public enum Offset {

	NORTH_EAST {
		@Override
		public BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation) {
			/*
			 * 0 -> 0 0
			 * 90 -> 0 1
			 * 180 -> 1 1
			 * 270 -> 1 0
			 */
			BlockPos size = structure.getSize();
			BlockPos p = DungeonPlacement.transform(size.getX(), 0, size.getZ(), mirror, rotation);
			return pos.offset(-Math.min(p.getX(), 0), 0, -Math.min(p.getZ(), 0));
		}
	},
	SOUTH_EAST {
		@Override
		public BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation) {
			/*
			 * 0 -> 0 -1
			 * 90 -> 0 0
			 * 180 -> 1 0
			 * 270 -> 1 -1
			 */
			BlockPos size = structure.getSize();
			BlockPos p = DungeonPlacement.transform(size.getX(), 0, size.getZ(), mirror, rotation);
			return pos.offset(-Math.min(p.getX(), 0), 0, -Math.max(p.getZ(), 0));
		}
	},
	SOUTH_WEST {
		@Override
		public BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation) {
			/*
			 * 0 -> -1 -1
			 * 90 -> -1 0
			 * 180 -> 0 0
			 * 270 -> 0 -1
			 */
			BlockPos size = structure.getSize();
			BlockPos p = DungeonPlacement.transform(size.getX(), 0, size.getZ(), mirror, rotation);
			return pos.offset(-Math.max(p.getX(), 0), 0, -Math.max(p.getZ(), 0));
		}
	},
	NORTH_WEST {
		@Override
		public BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation) {
			/*
			 * 0 -> -1 0
			 * 90 -> -1 1
			 * 180 -> 0 1
			 * 270 -> 0 0
			 */
			BlockPos size = structure.getSize();
			BlockPos p = DungeonPlacement.transform(size.getX(), 0, size.getZ(), mirror, rotation);
			return pos.offset(-Math.max(p.getX(), 0), 0, -Math.min(p.getZ(), 0));
		}
	},
	CENTER {
		@Override
		public BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation) {
			/*
			 * 0 -> -0.5 -0.5
			 * 90 -> -0.5 0.5
			 * 180 -> 0.5 0.5
			 * 270 -> 0.5 -0.5
			 */
			BlockPos size = structure.getSize();
			BlockPos p = DungeonPlacement.transform(size.getX(), 0, size.getZ(), mirror, rotation);
			return pos.offset(-(p.getX() >> 1), 0, -(p.getZ() >> 1));
		}
	};

	public abstract BlockPos apply(BlockPos pos, CQStructure structure, Mirror mirror, Rotation rotation);

}
