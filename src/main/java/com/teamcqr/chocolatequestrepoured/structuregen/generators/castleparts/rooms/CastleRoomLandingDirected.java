package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLandingDirected extends CastleRoomBase {
	private int openingWidth;
	private int openingSeparation;
	private int stairZ;
	private EnumFacing stairStartSide;

	public CastleRoomLandingDirected(BlockPos startOffset, int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.LANDING_DIRECTED;
		this.openingWidth = stairsBelow.getUpperStairWidth();
		this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
		this.openingSeparation = stairsBelow.getCenterStairWidth();
		this.stairStartSide = stairsBelow.getDoorSide();
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, CastleDungeon dungeon) {
		IBlockState blockToBuild;
		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					if (y == 0) {
						if (z > this.stairZ) {
							blockToBuild = dungeon.getFloorBlock().getDefaultState();
						} else if (x < this.openingWidth || ((x >= this.openingSeparation + this.openingWidth) && (x < this.openingSeparation + this.openingWidth * 2))) {
							if (z == this.stairZ) {
								EnumFacing stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(EnumFacing.SOUTH, DungeonGenUtils.getCWRotationsBetween(EnumFacing.SOUTH, this.stairStartSide));
								blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
							}
						} else {
							blockToBuild = dungeon.getFloorBlock().getDefaultState();
						}
					}

					genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.stairStartSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
				}
			}
		}
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		// Really only works on this side, could add logic to align the doors for other sides later
		return (side == this.stairStartSide);
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return (side == this.stairStartSide || side == this.stairStartSide.getOpposite());
	}
}
