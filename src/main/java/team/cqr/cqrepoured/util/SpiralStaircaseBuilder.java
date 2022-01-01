package team.cqr.cqrepoured.util;

import net.minecraft.block.StairsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class SpiralStaircaseBuilder {

	private static final int STAIR_WIDTH = 2;

	private BlockPos start;
	private Direction firstSide;
	private BlockState platformBlockState;
	private BlockState stairBlockState;

	public SpiralStaircaseBuilder(BlockPos pillarStart, Direction firstStairSide, BlockState platformBlockState, BlockState stairBlockState) {
		this.start = pillarStart;
		this.firstSide = firstStairSide;
		this.platformBlockState = platformBlockState;
		this.stairBlockState = stairBlockState;
	}

	// returns true if a position is within this staircase, meaning that it is within
	// the 3x3 grid of the spiral and at or above the starting Y
	public boolean isPartOfStairs(BlockPos position) {
		return ((Math.abs(position.getX() - this.start.getX()) <= STAIR_WIDTH) && (Math.abs(position.getZ() - this.start.getZ()) <= STAIR_WIDTH) && (position.getY() >= this.start.getY()));
	}

	public BlockState getBlock(BlockPos position) {
		Direction stairSide;
		int startX = this.start.getX();
		int startZ = this.start.getZ();
		int posX = position.getX();
		int posZ = position.getZ();

		if (position.getX() == this.start.getX() && position.getZ() == this.start.getZ()) {
			return this.platformBlockState;
		}

		// The side of the stairs rotates each level from the bottom
		stairSide = this.rotateFacingNTimesCW(this.firstSide, Math.abs(position.getY() - this.start.getY()));
		Direction stairFacing = this.rotateFacingNTimesCW(stairSide, 1);

		switch (stairSide) {
		case NORTH:
			if (posX == startX && this.inBoundsNoZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.stairBlockState.withProperty(StairsBlock.FACING, stairFacing);
			} else if (this.inBoundsNoZero(posX, startX, STAIR_WIDTH) && this.inBoundsWithZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.platformBlockState;
			}
			break;

		case SOUTH:
			if (posX == startX && this.inBoundsNoZero(posZ, startZ, STAIR_WIDTH)) {
				return this.stairBlockState.withProperty(StairsBlock.FACING, stairFacing);
			} else if (this.inBoundsNoZero(posX, startX, -STAIR_WIDTH) && this.inBoundsWithZero(posZ, startZ, STAIR_WIDTH)) {
				return this.platformBlockState;
			}
			break;

		case WEST:
			if (this.inBoundsNoZero(posX, startX, -STAIR_WIDTH) && posZ == startZ) {
				return this.stairBlockState.withProperty(StairsBlock.FACING, stairFacing);
			} else if (this.inBoundsWithZero(posX, startX, -STAIR_WIDTH) && this.inBoundsNoZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.platformBlockState;
			}
			break;

		case EAST:
			if (this.inBoundsNoZero(posX, startX, STAIR_WIDTH) && posZ == startZ) {
				return this.stairBlockState.withProperty(StairsBlock.FACING, stairFacing);
			} else if (this.inBoundsWithZero(posX, startX, STAIR_WIDTH) && this.inBoundsNoZero(posZ, startZ, STAIR_WIDTH)) {
				return this.platformBlockState;
			}
			break;
		default:
			break;
		}
		return Blocks.AIR.getDefaultState();
	}

	private Direction rotateFacingNTimesCW(Direction facing, int n) {
		n = n % 4; // cap at 0-3 rotations, any more is redundant
		while (n != 0) {
			facing = facing.rotateY();
			n--;
		}

		return facing;
	}

	private boolean inBoundsNoZero(int pos, int start, int distance) {
		int diff = pos - start;
		if (distance > 0) {
			return (diff > 0 && diff <= distance);
		} else {
			return (diff < 0 && diff >= distance);
		}
	}

	private boolean inBoundsWithZero(int pos, int start, int distance) {
		int diff = pos - start;
		if (distance > 0) {
			return (diff >= 0 && diff <= distance);
		} else {
			return (diff <= 0 && diff >= distance);
		}
	}

}
