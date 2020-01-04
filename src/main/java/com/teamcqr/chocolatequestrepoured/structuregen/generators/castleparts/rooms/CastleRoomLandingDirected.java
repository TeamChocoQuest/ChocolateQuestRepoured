package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomLandingDirected extends CastleRoom {
	private int openingWidth;
	private int openingSeparation;
	private int stairZ;
	private EnumFacing stairStartSide;

	public CastleRoomLandingDirected(BlockPos startPos, int sideLength, int height, CastleRoomStaircaseDirected stairsBelow) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.LANDING_DIRECTED;
		this.openingWidth = stairsBelow.getUpperStairWidth();
		this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
		this.openingSeparation = stairsBelow.getCenterStairWidth();
		this.stairStartSide = stairsBelow.getDoorSide();
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
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
								EnumFacing stairFacing = this.rotateFacingNTimesAboutY(EnumFacing.SOUTH, this.getNumYRotationsFromStartToEndFacing(EnumFacing.SOUTH, this.stairStartSide));
								blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
							}
						} else {
							blockToBuild = dungeon.getFloorBlock().getDefaultState();
						}
					}

					world.setBlockState(this.getRotatedPlacement(x, y, z, this.stairStartSide), blockToBuild);
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
