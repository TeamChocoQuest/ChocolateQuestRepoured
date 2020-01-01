package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossStairEmpty extends CastleRoom {
	private EnumFacing doorSide;

	public CastleRoomBossStairEmpty(BlockPos startPos, int sideLength, int height, EnumFacing doorSide) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.STAIRCASE_BOSS;
		this.pathable = true;
		this.doorSide = doorSide;
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		if (!(this.doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.NORTH) && !(this.doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.WEST)) {
			super.addInnerWall(side);
		}
	}
}
