package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdLinearGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdFloor {
	
	private int floorY;
	private int roomCount;
	
	private List<StrongholdRoom> rooms = new ArrayList<>();
	private List<StrongholdRoom> roomsWithFreeNeighbors = new ArrayList<>();
	
	private StrongholdRoom[][] roomGrid;
	private Tuple<Integer, Integer> lastRoomIndexes;
	private Tuple<Integer, Integer> firstRoomIndexes;

	private StrongholdLinearGenerator generator;
	
	public StrongholdFloor(int roomCount, StrongholdLinearGenerator generator) {
		this.generator = generator;
		this.roomCount = roomCount;
		roomGrid = new StrongholdRoom[roomCount *2 +2][roomCount *2 +2];
	}
	
	public StrongholdLinearGenerator getGenerator() {
		return this.generator;
	}
	
	//First room always is at [roomCount +2][roomCount +2]
	public void generateRoomPattern() {
		StrongholdRoom room = new StrongholdRoom(this);
		room.setGridIndex(roomCount-1, roomCount-1);
		rooms.add(room);
		roomsWithFreeNeighbors.add(room);
		roomGrid[room.getGridIndex().getFirst()][room.getGridIndex().getSecond()] = room;
		firstRoomIndexes = room.getGridIndex();
		Random rdm = new Random();
		for(int i = 0; i < roomCount; i++) {
			if(roomsWithFreeNeighbors.isEmpty()) {
				break;
			}
			StrongholdRoom prevRoom = getRandomUsableRoom(rdm);
			ESkyDirection expansionDir = prevRoom.getRandomFreeDirection(rdm);
			prevRoom.connectRoomOnSide(expansionDir);
			Tuple<Integer, Integer> v = getSkyDirectionAsGridVector(expansionDir);
			int gridX = prevRoom.getGridIndex().getFirst() + v.getFirst();
			int gridZ = prevRoom.getGridIndex().getSecond() + v.getSecond();
		
			if(!prevRoom.hasFreeSides()) {
				roomsWithFreeNeighbors.remove(prevRoom);
			}
			
			StrongholdRoom newRoom = new StrongholdRoom(this);
			newRoom.connectRoomOnSide(expansionDir.getOpposite());
			newRoom.setGridIndex(gridX, gridZ);
			rooms.add(newRoom);
			roomsWithFreeNeighbors.add(newRoom);
			roomGrid[gridX][gridZ] = newRoom;
			lastRoomIndexes = newRoom.getGridIndex();
			
			setConnectionStateOnGridForCoordinates(gridX, gridZ);
		}
	}
	
	public void generateRooms(BlockPos upperFloorExitPos, boolean firstStairIsEntranceStair, boolean isLastFloor, PlacementSettings settings, World world, StrongholdLinearDungeon dungeon) {
		//Entrance stair
		StrongholdRoom entranceRoom = roomGrid[firstRoomIndexes.getFirst()][firstRoomIndexes.getSecond()];
		CQStructure stair = null;
		if(firstStairIsEntranceStair) {
			stair = new CQStructure(dungeon.getEntranceStairRoom(), dungeon, generator.getDunX(), generator.getDunZ(), dungeon.isProtectedFromModifications());
		} else {
			stair = new CQStructure(dungeon.getStairRoom(), dungeon, generator.getDunX(), generator.getDunZ(), dungeon.isProtectedFromModifications());
		}
		floorY = upperFloorExitPos.getY() - stair.getSizeY();
		entranceRoom.generateRoom(dungeon, upperFloorExitPos.subtract(new Vec3i(0,stair.getSizeY(),0)), world, settings, stair, true);
		
		
		//Rest of the rooms
		for(int iX = 0; iX < (roomCount +2); iX++) {
			for(int iZ = 0; iZ < (roomCount +2); iZ++) {
				StrongholdRoom room = roomGrid[iX][iZ];
				if(room != null) {
					Vec3i v = new Vec3i(iX - firstRoomIndexes.getFirst(), 0, iZ - firstRoomIndexes.getSecond());
					BlockPos pos = new BlockPos(upperFloorExitPos.getX() + (dungeon.getRoomSizeX() * v.getX()), floorY, upperFloorExitPos.getX() + (dungeon.getRoomSizeZ() * v.getZ()));
					
					//first room
					if(!(iX == firstRoomIndexes.getFirst() && iZ == firstRoomIndexes.getSecond())) {
						//Last room
						CQStructure struct = null;
						if(iX == lastRoomIndexes.getFirst() && iZ == lastRoomIndexes.getSecond()) {
							if(isLastFloor) {
								struct = new CQStructure(dungeon.getBossRoom(), dungeon, generator.getDunX(), generator.getDunZ(), dungeon.isProtectedFromModifications());
								room.generateRoom(dungeon, pos, world, settings, struct, true);
							} 
						} 
						//Normal rooms
						else {
							room.generateRoom(dungeon, pos, world, settings);
						}
					}
				}
			}
		}
	}
	
	public BlockPos getLastRoomPastePos(BlockPos upperFloorExitPos, StrongholdLinearDungeon dungeon) {
		Vec3i v = new Vec3i(lastRoomIndexes.getFirst() - firstRoomIndexes.getFirst(), 0, lastRoomIndexes.getSecond() - firstRoomIndexes.getSecond());
		BlockPos pos = new BlockPos(upperFloorExitPos.getX() + (dungeon.getRoomSizeX() * v.getX()), floorY, upperFloorExitPos.getX() + (dungeon.getRoomSizeZ() * v.getZ()));
		return pos;
	}
	
	private void setConnectionStateOnGridForCoordinates(int gridX, int gridZ) {
		for(ESkyDirection direction : ESkyDirection.values()) {
			StrongholdRoom room = roomGrid[gridX + getSkyDirectionAsGridVector(direction).getFirst()][gridZ + getSkyDirectionAsGridVector(direction).getSecond()];
			if(room != null) {
				room.connectRoomOnSide(direction.getOpposite());
				if(!room.hasFreeSides()) {
					roomsWithFreeNeighbors.remove(room);
				}
			}
		}
	}

	private Tuple<Integer, Integer> getSkyDirectionAsGridVector(ESkyDirection expansionDir) {
		int f = 0;
		int s = 0;
		switch(expansionDir) {
		case EAST:
			f = 1;
			break;
		case NORTH:
			s = 1;
			break;
		case SOUTH:
			s = -1;
			break;
		case WEST:
			f = -1;
			break;
		default:
			break;
		}
		return new Tuple<>(f,s);
	}

	private StrongholdRoom getRandomUsableRoom(Random rdm) {
		if(!roomsWithFreeNeighbors.isEmpty()) {
			return roomsWithFreeNeighbors.get(rdm.nextInt(roomsWithFreeNeighbors.size()));
		}
		return null;
	}

}
