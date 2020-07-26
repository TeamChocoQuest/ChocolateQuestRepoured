package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

public class RoomFactoryCastle {
	public static CastleRoomDecoratedBase CreateGenericRoom(EnumRoomType type, int sideLength, int height, int floor) {
		switch (type) {
		case KITCHEN:
			return new CastleRoomKitchen(sideLength, height, floor);

		case BEDROOM_BASIC:
			return new CastleRoomBedroomBasic(sideLength, height, floor);

		case BEDROOM_FANCY:
			return new CastleRoomBedroomFancy(sideLength, height, floor);

		case ARMORY:
			return new CastleRoomArmory(sideLength, height, floor);

		case ALCHEMY_LAB:
			return new CastleRoomAlchemyLab(sideLength, height, floor);

		case LIBRARY:
			return new CastleRoomLibrary(sideLength, height, floor);

		case POOL:
			return new CastleRoomPool(sideLength, height, floor);

		case PORTAL:
			return new CastleRoomNetherPortal(sideLength, height, floor);

		case JAIL:
			return new CastleRoomJailCell(sideLength, height, floor);

		default:
			return null;
		}
	}
}
