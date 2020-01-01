package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomHallway extends CastleRoomGeneric {
	public enum Alignment {
		VERTICAL, HORIZONTAL;

		private boolean canHaveInteriorWall(EnumFacing side) {
			if (this == VERTICAL) {
				return (side == EnumFacing.WEST || side == EnumFacing.EAST);
			} else {
				return (side == EnumFacing.NORTH || side == EnumFacing.SOUTH);
			}
		}
	}

	private Alignment alignment;

	public CastleRoomHallway(BlockPos startPos, int sideLength, int height, Alignment alignment) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.HALLWAY;
		this.alignment = alignment;
		this.defaultFloor = true;
		this.defaultCeiling = true;

		this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 10);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.TORCH, 1);
	}

	@Override
	protected IBlockState getFloorBlock(CastleDungeon dungeon) {
		return Blocks.GRAY_GLAZED_TERRACOTTA.getDefaultState();
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		if (this.alignment.canHaveInteriorWall(side)) {
			super.addInnerWall(side);
		}
	}
}