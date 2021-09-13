package team.cqr.cqrepoured.structuregen.generators.stronghold.spiral;

import java.util.List;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.generators.volcano.StairCaseHelper;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockInfo;

public class EntranceBuilderHelper {

	public static final int SEGMENT_LENGTH = 3;
	
	public static void buildEntranceSegment(BlockPos startPosCentered, List<AbstractBlockInfo> blockInfoList, EnumFacing direction) {
		// COrner 2 is always the reference location for the part (!)
		@SuppressWarnings("unused")
		BlockPos corner1, corner2, pillar1, pillar2, torch1, torch2, air1, air2;
		corner1 = null;
		corner2 = null;
		// Pillars are in the middle of the part (on the expansion axis)
		pillar1 = null;
		pillar2 = null;
		// marks the positions of the torches
		torch1 = null;
		torch2 = null;
		// these mark the corners of the complete part
		air1 = null;
		air2 = null;
		switch (direction) {
		case EAST:
			corner1 = startPosCentered.add(0, 0, -3);
			corner2 = startPosCentered.add(3, 0, 3);
			air1 = startPosCentered.add(0, 1, -2);
			air2 = startPosCentered.add(3, 5, -2);
			pillar1 = startPosCentered.add(1, 0, 2);
			pillar2 = startPosCentered.add(1, 0, -2);
			torch1 = startPosCentered.add(1, 4, 1);
			torch2 = startPosCentered.add(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.add(-3, 0, 0);
			corner2 = startPosCentered.add(3, 0, -3);
			air1 = startPosCentered.add(-2, 1, 0);
			air2 = startPosCentered.add(2, 5, -3);
			pillar1 = startPosCentered.add(2, 0, -1);
			pillar2 = startPosCentered.add(-2, 0, -1);
			torch1 = startPosCentered.add(1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.add(3, 0, 0);
			corner2 = startPosCentered.add(-3, 0, 3);
			air1 = startPosCentered.add(2, 1, 0);
			air2 = startPosCentered.add(-2, 5, 3);
			pillar1 = startPosCentered.add(-2, 0, 1);
			pillar2 = startPosCentered.add(2, 0, 1);
			torch1 = startPosCentered.add(-1, 4, 1);
			torch2 = startPosCentered.add(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.add(0, 0, 3);
			corner2 = startPosCentered.add(-3, 0, -3);
			air1 = startPosCentered.add(0, 1, 2);
			air2 = startPosCentered.add(-3, 5, 2);
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
			 * for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) { blockInfoList.add(new BlockInfo(airPos, Blocks.AIR.getDefaultState(), null)); }
			 */
			buildFloorAndCeiling(corner1, corner2, 5, blockInfoList);

			// Left torch -> Facing side: rotate right (90.0°)
			buildPillar(pillar1, blockInfoList);
			blockInfoList.add(new BlockInfo(torch1, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.COUNTERCLOCKWISE_90)), null));
			// Right torch -> Facing side: rotate left (-90.0°)
			buildPillar(pillar2, blockInfoList);
			blockInfoList.add(new BlockInfo(torch2, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(direction, Rotation.CLOCKWISE_90)), null));
		}
	}

	private static void buildPillar(BlockPos bottom, List<AbstractBlockInfo> blockInfoList) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.add(0, iY, 0);
			blockInfoList.add(new BlockInfo(pos, CQRBlocks.GRANITE_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null));
		}
		blockInfoList.add(new BlockInfo(bottom.add(0, 5, 0), CQRBlocks.GRANITE_CARVED.getDefaultState(), null));
	}

	private static void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, List<AbstractBlockInfo> blockInfoList) {
		BlockPos endP = new BlockPos(end.getX(), start.getY() + ceilingHeight +1, end.getZ());

		for (BlockPos p : BlockPos.getAllInBox(start, endP.add(0, ceilingHeight +1, 0))) {
			if(p.getY() == start.getY()) {
				blockInfoList.add(new BlockInfo(p, CQRBlocks.GRANITE_SMALL.getDefaultState(), null));
			} else if(p.getY() == endP.getY() ) {
				blockInfoList.add(new BlockInfo(p, CQRBlocks.GRANITE_SQUARE.getDefaultState(), null));
			} else {
				blockInfoList.add(new BlockInfo(p, Blocks.AIR.getDefaultState(), null));
			}
		}
	}
	
}
