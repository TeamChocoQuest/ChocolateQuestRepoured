package team.cqr.cqrepoured.structuregen.generators.stronghold.spiral;

import java.util.function.Consumer;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.gentest.part.BlockDungeonPart;
import team.cqr.cqrepoured.gentest.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.generators.volcano.StairCaseHelper;

public class EntranceBuilderHelper {

	public static final int SEGMENT_LENGTH = 3;

	public static void buildEntranceSegment(BlockPos startPosCentered, BlockDungeonPart.Builder partBuilder, EnumFacing direction) {
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
			corner1 = startPosCentered.add(0, 0, -3);
			corner2 = startPosCentered.add(3, 0, 3);
			pillar1 = startPosCentered.add(1, 0, 2);
			pillar2 = startPosCentered.add(1, 0, -2);
			torch1 = startPosCentered.add(1, 4, 1);
			torch2 = startPosCentered.add(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.add(-3, 0, 0);
			corner2 = startPosCentered.add(3, 0, -3);
			pillar1 = startPosCentered.add(2, 0, -1);
			pillar2 = startPosCentered.add(-2, 0, -1);
			torch1 = startPosCentered.add(1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.add(3, 0, 0);
			corner2 = startPosCentered.add(-3, 0, 3);
			pillar1 = startPosCentered.add(-2, 0, 1);
			pillar2 = startPosCentered.add(2, 0, 1);
			torch1 = startPosCentered.add(-1, 4, 1);
			torch2 = startPosCentered.add(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.add(0, 0, 3);
			corner2 = startPosCentered.add(-3, 0, -3);
			pillar1 = startPosCentered.add(-1, 0, -2);
			pillar2 = startPosCentered.add(-1, 0, 2);
			torch1 = startPosCentered.add(-1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, 1);
			break;
		default:
			break;
		}
		if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
			/*
			 * for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) { blockInfoList.add(new PreparableBlockInfo(airPos, Blocks.AIR.getDefaultState(), null)); }
			 */
			BlockPos.getAllInBox(corner1, corner2.add(0, 6, 0)).forEach(new Consumer<BlockPos>() {

				@Override
				public void accept(BlockPos t) {
					partBuilder.add(new PreparableBlockInfo(t, Blocks.AIR.getDefaultState(), null));
				}
			});

			buildFloorAndCeiling(corner1, corner2, 5, partBuilder);

			// Left torch -> Facing side: rotate right (90.0°)
			buildPillar(pillar1, partBuilder);
			partBuilder.add(new PreparableBlockInfo(torch1, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.COUNTERCLOCKWISE_90)), null));
			// Right torch -> Facing side: rotate left (-90.0°)
			buildPillar(pillar2, partBuilder);
			partBuilder.add(new PreparableBlockInfo(torch2, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.CLOCKWISE_90)), null));
		}
	}

	private static void buildPillar(BlockPos bottom, BlockDungeonPart.Builder partBuilder) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.add(0, iY, 0);
			partBuilder.add(new PreparableBlockInfo(pos, CQRBlocks.GRANITE_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null));
		}
		partBuilder.add(new PreparableBlockInfo(bottom.add(0, 5, 0), CQRBlocks.GRANITE_CARVED.getDefaultState(), null));
	}

	private static void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, BlockDungeonPart.Builder partBuilder) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());

		// Floor
		for (BlockPos p : BlockPos.getAllInBox(start, endP)) {
			partBuilder.add(new PreparableBlockInfo(p, CQRBlocks.GRANITE_SMALL.getDefaultState(), null));
		}

		// Ceiling
		for (BlockPos p : BlockPos.getAllInBox(start.add(0, ceilingHeight + 1, 0), endP.add(0, ceilingHeight + 1, 0))) {
			partBuilder.add(new PreparableBlockInfo(p, CQRBlocks.GRANITE_SQUARE.getDefaultState(), null));
		}
	}

}
