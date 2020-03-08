package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomAlchemyLab extends CastleRoomGeneric {
	public CastleRoomAlchemyLab(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.ALCHEMY_LAB;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 4);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.TORCH, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.CAULDRON, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.BREW_STAND, 1);

		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 10);
		this.decoSelector.registerMidDecor(RoomDecorTypes.WATER_BASIN, 1);
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		super.generateRoom(world, dungeon);
	}
}
