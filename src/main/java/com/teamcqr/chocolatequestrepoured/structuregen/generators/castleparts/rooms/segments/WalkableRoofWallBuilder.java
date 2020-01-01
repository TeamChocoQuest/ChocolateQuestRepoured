package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WalkableRoofWallBuilder extends RoomWallBuilder {
	public WalkableRoofWallBuilder(BlockPos roomStart, int height, int length, WallOptions options, EnumFacing side) {
		super(roomStart, height, length, options, side);
	}

	@Override
	public void generate(World world, CastleDungeon dungeon) {
		BlockPos pos;
		IBlockState blockToBuild;

		EnumFacing iterDirection;

		if (this.side.getAxis() == EnumFacing.Axis.X) {
			iterDirection = EnumFacing.SOUTH;
		} else {
			iterDirection = EnumFacing.EAST;
		}

		for (int i = 0; i < this.length; i++) {
			for (int y = 0; y < this.height; y++) {
				pos = this.wallStart.offset(iterDirection, i).offset(EnumFacing.UP, y);
				blockToBuild = this.getBlockToBuild(pos, dungeon);
				world.setBlockState(pos, blockToBuild);
			}
		}
	}

	@Override
	protected IBlockState getBlockToBuild(BlockPos pos, CastleDungeon dungeon) {
		if (this.options.hasDoor() && this.inDoorFrame(pos)) {
			return Blocks.AIR.getDefaultState();
		} else if (this.shouldBuildCrenellatedRoof(pos)) {
			return dungeon.getWallBlock().getDefaultState();
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	private boolean inDoorFrame(BlockPos pos) {
		// int y = pos.getY() - wallStart.getY();
		int dist = this.getLengthPoint(pos);

		return this.withinDoorWidth(dist);
	}

	private boolean shouldBuildCrenellatedRoof(BlockPos pos) {
		int heightPoint = pos.getY() - this.wallStart.getY();
		int lengthPoint;
		if (this.side.getAxis() == EnumFacing.Axis.X) {
			lengthPoint = pos.getZ() - this.wallStart.getZ();
		} else {
			lengthPoint = pos.getX() - this.wallStart.getX();
		}

		if (heightPoint == 0) {
			return true;
		} else if (heightPoint == 1) {
			return (lengthPoint == this.length - 1 || lengthPoint % 2 == 0);
		} else {
			return false;
		}
	}
}
