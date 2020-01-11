package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

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
		int entranceX = 0;
		int entranceZ = 0;
		int roomCount = dungeon.getStrongholdRoomCount(rdm);
		final int maxRoomsPerFloor = dungeon.getFloorSideLength() * 4 -4;
		ESpiralStrongholdRoomType entranceType = ESpiralStrongholdRoomType.NONE;
		switch(allowedDirection) {
		case EAST:
			entranceType = ESpiralStrongholdRoomType.CURVE_ES;
			break;
		case NORTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_NE;
			break;
		case SOUTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_SW;
			break;
		case WEST:
			entranceType = ESpiralStrongholdRoomType.CURVE_WN;
			break;
		default:
			break;
		
		}
		for(int i = 0; i < floors.length; i++) {
			int floorRoomCount = maxRoomsPerFloor;
			if(roomCount >= maxRoomsPerFloor) {
				roomCount -= maxRoomsPerFloor;
			} else {
				floorRoomCount = roomCount;
			}
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(posTuple, entranceX, entranceZ, roomCount <= 0 || i == (floors.length -1), dungeon.getFloorSideLength(), floorRoomCount);
			floor.calculateRoomGrid(entranceType, i % 2 == 0);
			entranceType = floor.getExitRoomType();
		}
	}
	
	public void calculateFloorCoordinates(BlockPos strongholdEntrancePos) {
		BlockPos currentPos = strongholdEntrancePos;
		for(int i = 0; i < floors.length; i++) {
			SpiralStrongholdFloor floor = floors[i];
			if(i != 0) {
				floor.overrideFirstRoomType(ESpiralStrongholdRoomType.NONE);
			}
			if(i == (floors.length -1)) {
				floor.overrideLastRoomType(ESpiralStrongholdRoomType.BOSS);
			}
			floor.calculateCoordinates(currentPos.getY(), dungeon.getRoomSizeX(), dungeon.getRoomSizeZ());
			currentPos.add(0,dungeon.getRoomSizeY(),0);
		}
	}
	
	public void buildFloors(BlockPos strongholdEntrancePos) {
		BlockPos currentPos = strongholdEntrancePos;
		for(int i = 0; i < floors.length; i++) {
			SpiralStrongholdFloor floor = floors[i];
			floor.buildRooms(currentPos);
			currentPos.add(0,dungeon.getRoomSizeY(),0);
			currentPos = new BlockPos(floor.getExitCoordinates().getFirst(), currentPos.getY(), floor.getExitCoordinates().getSecond());
		}
	}

}
