package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpiralStrongholdBuilder {

	private ESkyDirection allowedDirection;
	private VolcanoDungeon dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private List<List<? extends IStructure>> strongholdParts = new ArrayList<>();
	private Random rdm;
	
	public SpiralStrongholdBuilder(ESkyDirection expansionDirection, VolcanoDungeon dungeon, Random rdm) {
		this.rdm = rdm;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;
		
		floorCount = dungeon.getFloorCount(this.rdm);
		this.floors = new SpiralStrongholdFloor[floorCount];
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
			entranceType = ESpiralStrongholdRoomType.CURVE_EN;
			entranceX = dungeon.getFloorSideLength() -1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_SE;
			entranceX = middle;
			entranceZ = dungeon.getFloorSideLength() -1;
			break;
		case SOUTH:
			entranceType = ESpiralStrongholdRoomType.CURVE_NW;
			entranceX = middle;
			entranceZ = 0;
			break;
		case EAST:
			entranceType = ESpiralStrongholdRoomType.CURVE_WS;
			entranceX = 0;
			entranceZ = middle;
			break;
		default:
			break;
		
		}
		int y = strongholdEntrancePos.getY();
		for(int i = 0; i < floors.length; i++) {
			if(posTuple == null || roomCount <= 0) {
				floorCount--;
				continue;
			}
			int floorRoomCount = maxRoomsPerFloor;
			if(roomCount >= maxRoomsPerFloor) {
				roomCount -= maxRoomsPerFloor;
				/* We add one cause the room above the stair does not count as room*/
				roomCount++;
			} else {
				floorRoomCount = roomCount;
				roomCount = 0;
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
		//BlockPos currentPos = strongholdEntrancePos;
		List<List<? extends IStructure>> floors = new ArrayList<>();
		for(int i = 0; i < floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floors.addAll(floor.buildRooms(dungeon, strongholdEntrancePos.getX() /16, strongholdEntrancePos.getZ() /16, world));
		}
		strongholdParts.addAll(floors);
	}

	public List<List<? extends IStructure>> getStrongholdParts() {
		return strongholdParts;
	}

}
