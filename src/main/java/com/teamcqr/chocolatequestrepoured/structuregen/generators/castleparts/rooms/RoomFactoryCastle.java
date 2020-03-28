package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import net.minecraft.util.math.BlockPos;

public class RoomFactoryCastle {
	public static CastleRoomDecoratedBase CreateGenericRoom(EnumRoomType type, BlockPos startPos, int sideLength, int height, int floor) {
		switch (type) {
			case KITCHEN:
				return new CastleRoomKitchen(startPos, sideLength, height, floor);

			case BEDROOM_BASIC:
				return new CastleRoomBedroomBasic(startPos, sideLength, height, floor);

			case BEDROOM_FANCY:
				return new CastleRoomBedroomFancy(startPos, sideLength, height, floor);

			case ARMORY:
				return new CastleRoomArmory(startPos, sideLength, height, floor);

			case ALCHEMY_LAB:
				return new CastleRoomAlchemyLab(startPos, sideLength, height, floor);

			case LIBRARY:
				return new CastleRoomLibrary(startPos, sideLength, height, floor);

			default:
				return null;
		}
	}
}
