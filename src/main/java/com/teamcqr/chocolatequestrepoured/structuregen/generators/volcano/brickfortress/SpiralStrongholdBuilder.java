package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpiralStrongholdBuilder {

	private ESkyDirection allowedDirection;
	private VolcanoDungeon dungeon;
	private SpiralStrongholdFloor[] floors;
	
	private Random rdm;
	
	public SpiralStrongholdBuilder(ESkyDirection expansionDirection, VolcanoDungeon dungeon, Random rdm) {
		this.rdm = rdm;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;
		
		this.floors = new SpiralStrongholdFloor[dungeon.getFloorCount(this.rdm)];
	}
	
	public void calculateFloors(BlockPos strongholdEntrancePos) {
		Tuple<Integer, Integer> posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		int middle = (int) Math.floor(dungeon.getFloorSideLength() / 2 );
		int entranceX = 0;
		int entranceZ = 0;
		int roomCount = dungeon.getStrongholdRoomCount(rdm);
		final int maxRoomsPerFloor = dungeon.getFloorSideLength() * 4 -4;
		ESpiralStrongholdRoomType entranceType = ESpiralStrongholdRoomType.NONE;
		switch(allowedDirection) {
		case WEST:
			entranceType = ESpiralStrongholdRoomType.CURVE_ES;
			entranceX = dungeon.getFloorSideLength() -1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_NE;
			entranceX = middle;
			entranceZ = dungeon.getFloorSideLength() -1;
			break;
		case SOUTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_SW;
			entranceX = middle;
			entranceZ = 0;
			break;
		case EAST:
			entranceType = ESpiralStrongholdRoomType.CURVE_WN;
			entranceX = 0;
			entranceZ = middle;
			break;
		default:
			break;
		
		}
		int y = strongholdEntrancePos.getY();
		for(int i = 0; i < floors.length; i++) {
			int floorRoomCount = maxRoomsPerFloor;
			if(roomCount >= maxRoomsPerFloor) {
				roomCount -= maxRoomsPerFloor;
			} else {
				floorRoomCount = roomCount;
			}
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(posTuple, entranceX, entranceZ, roomCount <= 0 || i == (floors.length -1), dungeon.getFloorSideLength(), floorRoomCount);
			floor.calculateRoomGrid(entranceType, (i +1) % 2 == 0);
			floor.calculateCoordinates(y, dungeon.getRoomSizeX(), dungeon.getRoomSizeZ());
			posTuple = floor.getExitCoordinates();
			if(i != 0) {
				floor.overrideFirstRoomType(ESpiralStrongholdRoomType.NONE);
			}
			entranceX = floor.getExitIndex().getFirst();
			entranceZ = floor.getExitIndex().getSecond();
			if(i == (floors.length -1)) {
				floor.overrideLastRoomType(ESpiralStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
			}
			y += dungeon.getRoomSizeY();
			floors[i] = floor;
		}
	}
	
	public void buildFloors(BlockPos strongholdEntrancePos, World world) {
		BlockPos currentPos = strongholdEntrancePos;
		for(int i = 0; i < floors.length; i++) {
			SpiralStrongholdFloor floor = floors[i];
			floor.buildRooms(dungeon, strongholdEntrancePos.getX() /16, strongholdEntrancePos.getZ() /16, world);
			currentPos.add(0,dungeon.getRoomSizeY(),0);
			System.out.println("###### FLOOR " + (i +1) +" ######" );
			for(int x = 0; x < dungeon.getFloorSideLength(); x++) {
				for(int z = 0; z < dungeon.getFloorSideLength(); z++) {
					if(x == 0 || x == (dungeon.getFloorSideLength() -1) || z == 0 || z == (dungeon.getFloorSideLength() -1)) {
						if(floor.getRoomGrid() != null && floor.getRoomGrid()[x][z] != null) {
							System.out.println("Room at " + x + " | " + z + " is type: " + floor.getRoomGrid()[x][z].toString());
						}
					}
				}
			}
			System.out.println("");
			System.out.println("");
			if(i < (floors.length -1)) {
				currentPos = new BlockPos(floor.getExitCoordinates().getFirst(), currentPos.getY(), floor.getExitCoordinates().getSecond());
			}
		}
	}

}
