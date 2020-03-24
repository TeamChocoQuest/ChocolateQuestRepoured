package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLandingSpiral extends CastleRoomDecoratedBase {
	private CastleRoomStaircaseSpiral stairsBelow;

	public CastleRoomLandingSpiral(BlockPos startOffset, int sideLength, int height, CastleRoomStaircaseSpiral stairsBelow, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.LANDING_SPIRAL;
		this.stairsBelow = stairsBelow;
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, CastleDungeon dungeon) {
		BlockPos pos;
		IBlockState blockToBuild;
		BlockPos pillarStart = new BlockPos(this.stairsBelow.getCenterX(), this.origin.getY(), this.stairsBelow.getCenterZ());
		EnumFacing firstStairSide = this.stairsBelow.getLastStairSide().rotateY();

		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide, dungeon.getWallBlock(), dungeon.getStairBlock());

		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				for (int y = 0; y < this.getDecorationLengthY(); y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getInteriorBuildStart().add(x, y, z);

					// continue stairs for 1 layer through floor
					if (y == 0) {
						if (stairs.isPartOfStairs(pos)) {
							blockToBuild = stairs.getBlock(pos);
							this.usedDecoPositions.add(pos);
						} else {
							blockToBuild = dungeon.getFloorBlock().getDefaultState();
						}
					}
					genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
				}
			}
		}
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}
}
