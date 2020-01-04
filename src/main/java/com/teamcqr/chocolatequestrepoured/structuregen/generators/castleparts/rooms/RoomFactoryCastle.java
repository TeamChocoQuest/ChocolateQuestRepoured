package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import net.minecraft.util.math.BlockPos;

public class RoomFactoryCastle {
	public static CastleRoomGeneric CreateGenericRoom(EnumRoomType type, BlockPos startPos, int sideLength, int height) {
		switch (type) {
		case KITCHEN:
			return new CastleRoomKitchen(startPos, sideLength, height);

		case BEDROOM:
			return new CastleRoomBedroom(startPos, sideLength, height);

		case ARMORY:
			return new CastleRoomArmory(startPos, sideLength, height);

		case ALCHEMY_LAB:
			return new CastleRoomAlchemyLab(startPos, sideLength, height);

		default:
			return null;
		}
	}
}
