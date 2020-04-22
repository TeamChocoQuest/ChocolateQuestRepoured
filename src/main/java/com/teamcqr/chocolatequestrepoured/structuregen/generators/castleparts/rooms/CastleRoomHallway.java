package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomHallway extends CastleRoomGenericBase {
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

	public CastleRoomHallway(BlockPos startOffset, int sideLength, int height, Alignment alignment, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.HALLWAY;
		this.alignment = alignment;
		this.defaultFloor = true;
		this.defaultCeiling = true;

		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 25);
		this.decoSelector.registerMidDecor(RoomDecorTypes.PILLAR, 1);

	}

	@Override
	protected IBlockState getFloorBlock(DungeonCastle dungeon) {
		return Blocks.GRAY_GLAZED_TERRACOTTA.getDefaultState();
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		if (this.alignment.canHaveInteriorWall(side)) {
			super.addInnerWall(side);
		}
	}
}