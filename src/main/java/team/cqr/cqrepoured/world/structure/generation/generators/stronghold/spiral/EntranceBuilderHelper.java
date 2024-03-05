package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.world.structure.generation.generation.CQRStructurePiece;
import team.cqr.cqrepoured.world.structure.generation.generators.volcano.StairCaseHelper;

public class EntranceBuilderHelper {

	public static final int SEGMENT_LENGTH = 3;

	public static void buildEntranceSegment(BlockPos startPosCentered, CQRStructurePiece.Builder partBuilder, Direction direction) {
		// COrner 2 is always the reference location for the part (!)
		BlockPos corner1, corner2, pillar1, pillar2, torch1, torch2;
		corner1 = null;
		corner2 = null;
		// Pillars are in the middle of the part (on the expansion axis)
		pillar1 = null;
		pillar2 = null;
		// marks the positions of the torches
		torch1 = null;
		torch2 = null;
		// these mark the corners of the complete part
		switch (direction) {
		case EAST:
			corner1 = startPosCentered.offset(0, 0, -3);
			corner2 = startPosCentered.offset(3, 0, 3);
			pillar1 = startPosCentered.offset(1, 0, 2);
			pillar2 = startPosCentered.offset(1, 0, -2);
			torch1 = startPosCentered.offset(1, 4, 1);
			torch2 = startPosCentered.offset(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.offset(-3, 0, 0);
			corner2 = startPosCentered.offset(3, 0, -3);
			pillar1 = startPosCentered.offset(2, 0, -1);
			pillar2 = startPosCentered.offset(-2, 0, -1);
			torch1 = startPosCentered.offset(1, 4, -1);
			torch2 = startPosCentered.offset(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.offset(3, 0, 0);
			corner2 = startPosCentered.offset(-3, 0, 3);
			pillar1 = startPosCentered.offset(-2, 0, 1);
			pillar2 = startPosCentered.offset(2, 0, 1);
			torch1 = startPosCentered.offset(-1, 4, 1);
			torch2 = startPosCentered.offset(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.offset(0, 0, 3);
			corner2 = startPosCentered.offset(-3, 0, -3);
			pillar1 = startPosCentered.offset(-1, 0, -2);
			pillar2 = startPosCentered.offset(-1, 0, 2);
			torch1 = startPosCentered.offset(-1, 4, -1);
			torch2 = startPosCentered.offset(-1, 4, 1);
			break;
		default:
			break;
		}
		if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
			/*
			 * for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) { blockInfoList.add(new PreparableBlockInfo(airPos,
			 * Blocks.AIR.getDefaultState(), null)); }
			 */
			BlockPos.betweenClosed(corner1, corner2.offset(0, 6, 0)).forEach(t -> partBuilder.getLevel().setBlockState(t, Blocks.AIR.defaultBlockState(), null));

			buildFloorAndCeiling(corner1, corner2, 5, partBuilder);

			// Left torch -> Facing side: rotate right (90.0°)
			buildPillar(pillar1, partBuilder);
			partBuilder.getLevel().setBlockState(torch1, CQRBlocks.UNLIT_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.COUNTERCLOCKWISE_90)), null);
			// Right torch -> Facing side: rotate left (-90.0°)
			buildPillar(pillar2, partBuilder);
			partBuilder.getLevel().setBlockState(torch2, CQRBlocks.UNLIT_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.CLOCKWISE_90)), null);
		}
	}

	private static void buildPillar(BlockPos bottom, CQRStructurePiece.Builder partBuilder) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.offset(0, iY, 0);
			partBuilder.getLevel().setBlockState(pos, CQRBlocks.GRANITE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), null);
		}
		partBuilder.getLevel().setBlockState(bottom.offset(0, 5, 0), CQRBlocks.GRANITE_CARVED.get().defaultBlockState(), null);
	}

	private static void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, CQRStructurePiece.Builder partBuilder) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());

		// Floor
		for (BlockPos p : BlockPos.betweenClosed(start, endP)) {
			partBuilder.getLevel().setBlockState(p, CQRBlocks.GRANITE_SMALL.get().defaultBlockState(), null);
		}

		// Ceiling
		for (BlockPos p : BlockPos.betweenClosed(start.offset(0, ceilingHeight + 1, 0), endP.offset(0, ceilingHeight + 1, 0))) {
			partBuilder.getLevel().setBlockState(p, CQRBlocks.GRANITE_SQUARE.get().defaultBlockState(), null);
		}
	}

}
