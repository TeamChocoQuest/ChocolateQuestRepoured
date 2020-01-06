package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomStaircaseDirected extends CastleRoom {
	private static final int PLATFORM_LENGTH = 2;
	private EnumFacing doorSide;
	private int numRotations;
	private int upperStairWidth;
	private int upperStairLength;
	private int centerStairWidth;
	private int centerStairLength;

	public CastleRoomStaircaseDirected(BlockPos startPos, int sideLength, int height, EnumFacing doorSide) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.STAIRCASE_DIRECTED;
		this.doorSide = doorSide;
		this.numRotations = this.getNumYRotationsFromStartToEndFacing(EnumFacing.SOUTH, this.doorSide);
		this.defaultCeiling = false;

		this.upperStairWidth = 0;

		// Determine the width of the center stairs and the two upper side stairs. Find the largest possible
		// side width such that the center width is still greater than or equal to the length of each side.
		do {
			this.upperStairWidth++;
			this.centerStairWidth = (sideLength - 1) - this.upperStairWidth * 2;
		} while ((this.centerStairWidth - 2) >= (this.upperStairWidth + 1));

		// Each stair section should cover half the ascent
		this.upperStairLength = height / 2;
		this.centerStairLength = height + 1 - this.upperStairLength; // center section will either be same length or 1 more
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		IBlockState blockToBuild;
		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				this.buildFloorBlock(x, z, world, dungeon);

				if (z < 2) {
					this.buildPlatform(x, z, world, dungeon);
				} else if (((x < this.upperStairWidth) || (x >= this.centerStairWidth + this.upperStairWidth)) && z < this.upperStairLength + PLATFORM_LENGTH) {
					this.buildUpperStair(x, z, world, dungeon);
				} else if (((x >= this.upperStairWidth) || (x < this.centerStairWidth + this.upperStairWidth)) && z <= this.centerStairLength + PLATFORM_LENGTH) {
					this.buildLowerStair(x, z, world, dungeon);
				}
			}
		}
	}

	public void setDoorSide(EnumFacing side) {
		this.doorSide = side;
	}

	public int getUpperStairEndZ() {
		return (this.upperStairLength);
	}

	public int getUpperStairWidth() {
		return this.upperStairWidth;
	}

	public int getCenterStairWidth() {
		return this.centerStairWidth;
	}

	public EnumFacing getDoorSide() {
		return this.doorSide;
	}

	private void buildFloorBlock(int x, int z, World world, CastleDungeon dungeon) {
		IBlockState blockToBuild = dungeon.getFloorBlock().getDefaultState();
		world.setBlockState(this.origin.add(x, 0, z), blockToBuild);
	}

	private void buildUpperStair(int x, int z, World world, CastleDungeon dungeon) {
		int stairHeight = this.centerStairLength + (z - PLATFORM_LENGTH);
		EnumFacing stairFacing = this.rotateFacingNTimesAboutY(EnumFacing.SOUTH, this.numRotations);
		IBlockState blockToBuild;
		for (int y = 1; y < this.height; y++) {
			if (y < stairHeight) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (y == stairHeight) {
				blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			world.setBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild);
		}
	}

	private void buildLowerStair(int x, int z, World world, CastleDungeon dungeon) {
		int stairHeight = this.centerStairLength - (z - PLATFORM_LENGTH + 1);
		EnumFacing stairFacing = this.rotateFacingNTimesAboutY(EnumFacing.NORTH, this.numRotations);
		IBlockState blockToBuild;
		for (int y = 1; y < this.height; y++) {
			if (y < stairHeight) {
				blockToBuild = dungeon.getWallBlock().getDefaultState();
			} else if (y == stairHeight) {
				blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			world.setBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild);
		}
	}

	private void buildPlatform(int x, int z, World world, CastleDungeon dungeon) {
		IBlockState blockToBuild;
		int platformHeight = this.centerStairLength; // the stair length is also the platform height

		for (int y = 1; y < this.height; y++) {
			if (y < platformHeight) {
				blockToBuild = dungeon.getFloorBlock().getDefaultState();
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			world.setBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild);
		}
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		return (side == this.doorSide);
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return (side == this.doorSide);
	}
}
