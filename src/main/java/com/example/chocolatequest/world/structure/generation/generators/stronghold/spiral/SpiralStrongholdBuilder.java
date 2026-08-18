package com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import com.example.chocolatequest.world.structure.generation.generators.stronghold.EStrongholdRoomType;

public class SpiralStrongholdBuilder {

	private final RandomSource random;
	private final StructurePiecesBuilder builder;
	private Direction allowedDirection;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private int roomCount = 0;
	private int floorSideLength = 0;
	private final String entityReplacement;
	private final String templateBasePath;

	public SpiralStrongholdBuilder(StructurePiecesBuilder builder, Direction expansionDirection, RandomSource rand) {
		this(builder, expansionDirection, rand, "cqrepoured:cq_gremlin",
				"cqrepoured:structure/volcano/rooms/");
	}

	public SpiralStrongholdBuilder(StructurePiecesBuilder builder, Direction expansionDirection, RandomSource rand,
			String entityReplacement) {
		this(builder, expansionDirection, rand, entityReplacement,
				"cqrepoured:structure/volcano/rooms/");
	}

	public SpiralStrongholdBuilder(StructurePiecesBuilder builder, Direction expansionDirection, RandomSource rand,
			String entityReplacement, String templateBasePath) {
		this.builder = builder;
		this.allowedDirection = expansionDirection;
		this.random = rand;
		this.entityReplacement = entityReplacement;
		this.templateBasePath = templateBasePath;

		int minRadius = 1;
		int maxRadius = 3;
		this.floorSideLength = (minRadius + rand.nextInt(maxRadius - minRadius + 1)) * 2 + 1;

		int minRooms = 15;
		int maxRooms = 40;
		this.roomCount = minRooms + rand.nextInt(maxRooms - minRooms + 1);

		if (this.roomCount % ((this.floorSideLength - 1) * 4) < 2) {
			this.roomCount++;
		}
		this.floorCount = Mth.ceil((double) this.roomCount / ((this.floorSideLength - 1) * 4));
		this.floors = new SpiralStrongholdFloor[this.floorCount];
	}

	public void calculateFloors(BlockPos strongholdEntrancePos) {
		int entranceXCoord = strongholdEntrancePos.getX();
		int entranceZCoord = strongholdEntrancePos.getZ();
		int middle = this.floorSideLength / 2;
		int entranceX = 0;
		int entranceZ = 0;
		int roomCounter = this.roomCount;
		final int maxRoomsPerFloor = this.floorSideLength * 4 - 4;
		int y = strongholdEntrancePos.getY();

		EStrongholdRoomType entranceType = EStrongholdRoomType.NONE;
		switch (this.allowedDirection) {
		case WEST:
			entranceType = EStrongholdRoomType.CURVE_EN;
			entranceX = this.floorSideLength - 1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = EStrongholdRoomType.CURVE_SE;
			entranceX = middle;
			entranceZ = this.floorSideLength - 1;
			break;
		case SOUTH:
			entranceType = EStrongholdRoomType.CURVE_NW;
			entranceX = middle;
			entranceZ = 0;
			break;
		case EAST:
			entranceType = EStrongholdRoomType.CURVE_WS;
			entranceX = 0;
			entranceZ = middle;
			break;
		default:
			break;
		}

		EStrongholdRoomType firstRoomOverride = entranceType;
		int roomSizeX = 15;
		int roomSizeY = 10;
		int roomSizeZ = 15;

		for (int i = 0; i < this.floors.length; i++) {
			int floorRoomCount = i < this.floors.length - 1 ? maxRoomsPerFloor : roomCounter % maxRoomsPerFloor;
			if (floorRoomCount == 0 && i == this.floors.length - 1) floorRoomCount = maxRoomsPerFloor; // edge case

			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(
					this.builder, entranceXCoord, entranceZCoord, entranceX, entranceZ, 
					roomCounter <= maxRoomsPerFloor || i == (this.floors.length - 1), 
					this.floorSideLength, floorRoomCount, this.random, this.entityReplacement, this.templateBasePath
			);
			floor.calculateRoomGrid(entranceType, (i + 1) % 2 == 0);
			floor.calculateCoordinates(y, roomSizeX, roomSizeZ);
			
			if (i != 0) {
				floor.overrideFirstRoomType(firstRoomOverride);
			}
			entranceXCoord = floor.getExitCoordinatesX();
			entranceZCoord = floor.getExitCoordinatesZ();
			entranceX = floor.getExitIndexX();
			entranceZ = floor.getExitIndexZ();
			
			if (i == (this.floors.length - 1)) {
				floor.overrideLastRoomType(EStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
				firstRoomOverride = entranceType;
				floor.overrideLastRoomType(EStrongholdRoomType.NONE);
			}
			y -= roomSizeY;
			this.floors[i] = floor;
			roomCounter -= floorRoomCount;
		}
	}

	public void buildFloors() {
		for (int i = 0; i < this.floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			if (floor != null) {
				floor.buildRooms();
			}
		}
	}
}
