package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

public class RoomFactoryCastle {
	public static CastleRoomDecoratedBase CreateGenericRoom(EnumRoomType type, int sideLength, int height, int floor, Random rand) {
		switch (type) {
		case KITCHEN:
			return new CastleRoomKitchen(sideLength, height, floor, rand);

		case BEDROOM_BASIC:
			return new CastleRoomBedroomBasic(sideLength, height, floor, rand);

		case BEDROOM_FANCY:
			return new CastleRoomBedroomFancy(sideLength, height, floor, rand);

		case ARMORY:
			return new CastleRoomArmory(sideLength, height, floor, rand);

		case ALCHEMY_LAB:
			return new CastleRoomAlchemyLab(sideLength, height, floor, rand);

		case LIBRARY:
			return new CastleRoomLibrary(sideLength, height, floor, rand);

		case POOL:
			return new CastleRoomPool(sideLength, height, floor, rand);

		case PORTAL:
			return new CastleRoomNetherPortal(sideLength, height, floor, rand);

		case JAIL:
			return new CastleRoomJailCell(sideLength, height, floor, rand);

		default:
			return null;
		}
	}
}
