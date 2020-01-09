package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

public class SpiralStrongholdFloor {

	private Tuple<Integer, Integer> entranceCoordinates;
	private Tuple<Integer, Integer> entranceIndex;
	private Tuple<Integer, Integer> exitCoordinates;
	private Tuple<Integer, Integer> exitIndex;
	private boolean isLastFloor = false;
	private ESpiralStrongholdRoomType entranceRoomType;
	private int sideLength;
	private int roomCount;
	private ESpiralStrongholdRoomType[][] roomGrid;
	
	public SpiralStrongholdFloor(Tuple<Integer, Integer> entrancePos, int entranceX, int entranceZ, boolean isLastFloor, int sideLength, int roomCount) {
		this.entranceCoordinates = entrancePos;
		this.entranceIndex = new Tuple<>(entranceX, entranceZ);
		this.isLastFloor = isLastFloor;
		this.sideLength = sideLength;
		this.roomGrid = new ESpiralStrongholdRoomType[sideLength][sideLength];
	}
	
	public void calculateRoomGrid(ESpiralStrongholdRoomType roomTypeOfLastFloorLastRoom, boolean rev) {
		boolean reverse = rev;
		this.entranceRoomType = roomTypeOfLastFloorLastRoom;
		
		ESpiralStrongholdRoomType roomType = this.entranceRoomType;
		int x = entranceIndex.getFirst();
		int z = entranceIndex.getSecond();
		roomGrid[x][z] = ESpiralStrongholdRoomType.NONE;
		switch(roomType) {
		case CURVE_ES:
			z--;
			break;
		case CURVE_NE:
			x++;
			break;
		case CURVE_SW:
			x--;
			break;
		case CURVE_WN:
			z++;
			break;
		case STAIR_EE:
			if(z == 0) {
				reverse = true;
			}
			x++;
			break;
		case STAIR_NN:
			if(x == (sideLength -1)) {
				reverse = true;
			}
			z++;
			break;
		case STAIR_SS:
			if(x == 0) {
				reverse = true;
			}
			z--;
			break;
		case STAIR_WW:
			if(z == (sideLength -1)) {
				reverse = true;
			}
			x--;
			break;
		default:
			break;
		
		}
		
		//TODO: Dependent on init room type -> alter x and z
		
		int roomCounter = 1;
		int maxRooms = (4* sideLength) -4;
	
		while(roomCounter < (roomCount -1) && roomCounter < (maxRooms -1)) {
			//Corners
			if(x == (sideLength -1) && z == (sideLength -1)) {
				//Upper Right Corner
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.CURVE_NE : ESpiralStrongholdRoomType.CURVE_NE;
				if(!reverse) {
					z--;
				} else {
					x--;
				}
			} else if(x == 0 && z == 0) {
				//Lower Left Corner
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.CURVE_SW : ESpiralStrongholdRoomType.CURVE_WS;
				if(!reverse) {
					z++;
				} else {
					x++;
				}
			} else if(x == 0 && z == (sideLength -1)) {
				//Upper Left Corner
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.CURVE_WN : ESpiralStrongholdRoomType.CURVE_NW;
				if(!reverse) {
					x++;
				} else {
					z--;
				}
			} else if(z == 0 && x == (sideLength -1)) {
				//Lower Right Corner
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.CURVE_ES : ESpiralStrongholdRoomType.CURVE_SE;
				if(!reverse) {
					x--;
				} else {
					z++;
				}
			} 
			//Hallways
			else if(x == 0) {
				//Left Column
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.HALLWAY_SN : ESpiralStrongholdRoomType.HALLWAY_NS;
				if(!reverse) {
					z++;
				} else {
					z--;
				}
			} else if(x == (sideLength -1)) {
				//Right Column
				roomGrid[x][z] = reverse ? ESpiralStrongholdRoomType.HALLWAY_SN : ESpiralStrongholdRoomType.HALLWAY_NS;
				if(!reverse) {
					z--;
				} else {
					z++;
				}
			} else if(z == 0) {
				//Lower Row
				roomGrid[x][z] = !reverse ? ESpiralStrongholdRoomType.HALLWAY_EW : ESpiralStrongholdRoomType.HALLWAY_WE;
				if(!reverse) {
					x--;
				} else {
					x++;
				}
			} else if(z == (sideLength -1)) {
				//Upper Row
				roomGrid[x][z] = reverse ? ESpiralStrongholdRoomType.HALLWAY_WE : ESpiralStrongholdRoomType.HALLWAY_EW;
				if(!reverse) {
					x++;
				} else {
					x--;
				}
			}
			exitIndex = new Tuple<>(x,z);
			roomCounter++;
		}
		switch(roomGrid[x][z]) {
		case HALLWAY_SN:
		case CURVE_SE:
		case CURVE_SW:
			roomGrid[x][z] = ESpiralStrongholdRoomType.STAIR_SS;
			break;
			
		case CURVE_EN:
		case HALLWAY_EW:
		case CURVE_ES:
			roomGrid[x][z] = ESpiralStrongholdRoomType.STAIR_EE;
			break;
		
		case CURVE_NW:
		case CURVE_NE:
		case HALLWAY_NS:
			roomGrid[x][z] = ESpiralStrongholdRoomType.STAIR_NN;
			break;

		case CURVE_WN:
		case CURVE_WS:
		case HALLWAY_WE:
			roomGrid[x][z] = ESpiralStrongholdRoomType.STAIR_WW;
			break;
		default:
			break;
		
		}
	}
	
	public void buildRooms(BlockPos firstRoomPosition) {
		
	}

}
