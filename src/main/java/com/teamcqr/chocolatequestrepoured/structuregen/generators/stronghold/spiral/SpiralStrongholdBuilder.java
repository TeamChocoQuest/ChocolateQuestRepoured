package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.AbstractDungeonPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpiralStrongholdBuilder {

	private DungeonGenerator dungeonGenerator;
	private ESkyDirection allowedDirection;
	private DungeonVolcano dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();
	private Random rdm;
	
	public SpiralStrongholdBuilder(DungeonGenerator dungeonGenerator, ESkyDirection expansionDirection, DungeonVolcano dungeon, Random rdm) {
		this.dungeonGenerator = dungeonGenerator;
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
		EStrongholdRoomType entranceType = EStrongholdRoomType.NONE;
		switch(allowedDirection) {
		case WEST:
			entranceType = EStrongholdRoomType.CURVE_EN;
			entranceX = dungeon.getFloorSideLength() -1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = EStrongholdRoomType.CURVE_SE;
			entranceX = middle;
			entranceZ = dungeon.getFloorSideLength() -1;
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
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(this.dungeonGenerator, posTuple, entranceX, entranceZ, roomCount <= 0 || i == (floors.length -1), dungeon.getFloorSideLength(), floorRoomCount);
			floor.calculateRoomGrid(entranceType, (i +1) % 2 == 0);
			floor.calculateCoordinates(y, dungeon.getRoomSizeX(), dungeon.getRoomSizeZ());
			posTuple = floor.getExitCoordinates();
			if(i != 0) {
				floor.overrideFirstRoomType(EStrongholdRoomType.NONE);
			}
			entranceX = floor.getExitIndex().getFirst();
			entranceZ = floor.getExitIndex().getSecond();
			if(i == (floors.length -1)) {
				floor.overrideLastRoomType(EStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
			}
			y += dungeon.getRoomSizeY();
			floors[i] = floor;
		}
	}
	
	public void buildFloors(BlockPos strongholdEntrancePos, World world, int dungeonChunkX, int dungeonChunkZ) {
		//BlockPos currentPos = strongholdEntrancePos;
		List<AbstractDungeonPart> floors = new ArrayList<>();
		EDungeonMobType mobType = dungeon.getDungeonMob();
		if (mobType == EDungeonMobType.DEFAULT) {
			mobType = EDungeonMobType.getMobTypeDependingOnDistance(world, dungeonChunkX, dungeonChunkZ);
		}
		for(int i = 0; i < floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floors.addAll(floor.buildRooms(dungeon, strongholdEntrancePos.getX() /16, strongholdEntrancePos.getZ() /16, world, mobType));
		}
		strongholdParts.addAll(floors);
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return strongholdParts;
	}

}
