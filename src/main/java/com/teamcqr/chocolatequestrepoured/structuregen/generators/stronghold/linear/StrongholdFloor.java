package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdLinearGenerator;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdFloor {

	private StrongholdLinearGenerator generator;
	private int sideLength;
	private EStrongholdRoomType[][] roomPattern;
	private ESkyDirection currentDirection;
	private boolean lastFloor;
	
	public StrongholdFloor(int size, StrongholdLinearGenerator generator, boolean isLastFloor) {
		this.generator = generator;
		this.sideLength = size;
		this.lastFloor = isLastFloor;
		this.roomPattern = new EStrongholdRoomType[size][size];
	}
	
	public void generateRoomPattern(int gridPosX, int gridPosZ, ESkyDirection prevFloorExitDir) {
		setRoomType(gridPosX, gridPosZ, EStrongholdRoomType.NONE);
		EStrongholdRoomType room = EStrongholdRoomType.NONE;
		boolean curve = gridPosX == 0 && gridPosZ == 0;
		switch (prevFloorExitDir) {
		case EAST:
			room = curve ? EStrongholdRoomType.CURVE_EN : EStrongholdRoomType.HALLWAY_EW;
			break;
		case NORTH:
			room = curve ? EStrongholdRoomType.CURVE_NW : EStrongholdRoomType.HALLWAY_NS;
			break;
		case SOUTH:
			room = curve ? EStrongholdRoomType.CURVE_SE : EStrongholdRoomType.HALLWAY_SN;
			break;
		case WEST:
			room = curve ? EStrongholdRoomType.CURVE_WS : EStrongholdRoomType.HALLWAY_WE;
			break;
		} 
		Tuple<Integer, Integer> roomCoord = getNextRoomCoordinates(gridPosX, gridPosZ, prevFloorExitDir);
		setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), room);
		currentDirection = getRoomExitDirection(room);
		
		int roomCount = this.sideLength * this.sideLength;
		roomCount -= 2;
		boolean reversed = !curve;
		int curveCount = reversed ? 4 : 3;
		while(roomCount > 0) {
			roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection);
			roomCount--;
			if(roomCount == 0) {
				//DONE: Handle stair or boss room
				if(lastFloor) {
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), EStrongholdRoomType.BOSS);
				} else {
					//Handle stair
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getStair(currentDirection));
					currentDirection = getRoomExitDirection(getStair(currentDirection));
				}
				continue;
			}
			if(isCurveRoom(roomCoord.getFirst(), roomCoord.getSecond()) || (reversed && getRoomAt(getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection)) != null)) {
				curveCount--;
				if(curveCount == 0) {
					curveCount = 3;
					if(reversed && getRoomAt(getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection)) != null) {
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
						roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection);
						currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
					} else {
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getHallway(this.currentDirection));
						roomCount--;
						roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection);
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
						currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
					}
				} else {
					//DONE Curve
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
					currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
					continue;
				}
			}
			//DONE: Hallway
			setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getHallway(this.currentDirection));
		}
	}
	
	private EStrongholdRoomType getRoomAt(Tuple<Integer, Integer> gridPos) {
		Tuple<Integer, Integer> arrPos = gridPosToArrayIndices(gridPos);
		return roomPattern[arrPos.getFirst()][arrPos.getSecond()];
	}
	
	private EStrongholdRoomType getHallway(ESkyDirection dir) {
		switch(dir) {
		case EAST:
			return EStrongholdRoomType.HALLWAY_EW;
		case NORTH:
			return EStrongholdRoomType.HALLWAY_NS;
		case SOUTH:
			return EStrongholdRoomType.HALLWAY_SN;
		case WEST:
			return EStrongholdRoomType.HALLWAY_WE;
		default:
			return null;
		}
	}
	
	private EStrongholdRoomType getStair(ESkyDirection dir) {
		switch(dir) {
		case EAST:
			return EStrongholdRoomType.STAIR_EE;
		case NORTH:
			return EStrongholdRoomType.STAIR_NN;
		case SOUTH:
			return EStrongholdRoomType.STAIR_SS;
		case WEST:
			return EStrongholdRoomType.STAIR_WW;
		default:
			return null;
		}
	}
	
	private EStrongholdRoomType getCurve(ESkyDirection dir, boolean reversed) {
		switch(dir) {
		case EAST:
			return reversed ? EStrongholdRoomType.CURVE_ES : EStrongholdRoomType.CURVE_EN;
		case NORTH:
			return reversed ? EStrongholdRoomType.CURVE_NE : EStrongholdRoomType.CURVE_NW;
		case SOUTH:
			return reversed ? EStrongholdRoomType.CURVE_SW : EStrongholdRoomType.CURVE_SE;
		case WEST:
			return reversed ? EStrongholdRoomType.CURVE_WN : EStrongholdRoomType.CURVE_WS;
		default:
			return null;
		}
	}
	
	public void generateRooms(int centerX, int centerZ, int y, PlacementSettings settings, List<List<? extends IStructure>> lists) {
		
	}
	
	private Tuple<Integer, Integer> getNextRoomCoordinates(int oldX, int oldZ, ESkyDirection direction) {
		switch(direction) {
		case EAST:
			return new Tuple<>(oldX +1, oldZ);
		case NORTH:
			return new Tuple<>(oldX, oldZ -1);
		case SOUTH:
			return new Tuple<>(oldX, oldZ +1);
		case WEST:
			return new Tuple<>(oldX -1, oldZ);
		}
		return new Tuple<>(oldX, oldZ);
	}
	
	public Tuple<Integer, Integer> getLastRoomGridPos() {
		return new Tuple<>(0,0);
	}
	
	public Tuple<Integer, Integer> getFirstRoomGridPos() {
		return new Tuple<>(0,0);
	}
	
	private Tuple<Integer, Integer> gridPosToArrayIndices(Tuple<Integer, Integer> gridPosIn) {
		int x = (int) Math.floor(sideLength /2D);
		return new Tuple<>(gridPosIn.getFirst() + x, gridPosIn.getSecond() + x);
	}
	
	private Tuple<Integer, Integer> arrayIndiciesToGridPos(Tuple<Integer, Integer> arrayIndiciesIn) {
		int x = (int) Math.floor(sideLength /2D);
		return new Tuple<>(arrayIndiciesIn.getFirst() - x, arrayIndiciesIn.getSecond() - x);
	}
	
	public ESkyDirection getExitDirection() {
		return currentDirection.getOpposite();
	}
	
	private boolean isCurveRoom(int gpX, int gpZ) {
		return Math.abs(gpX) == Math.abs(gpZ);
	}

	private void setRoomType(int gpX, int gpZ, EStrongholdRoomType type) {
		Tuple<Integer, Integer> coords = gridPosToArrayIndices(new Tuple<>(gpX, gpZ));
		this.roomPattern[coords.getFirst()][coords.getSecond()] = type;
	}
	
	private ESkyDirection getRoomExitDirection(EStrongholdRoomType room) {
		switch(room) {
		case CURVE_EN:
		case CURVE_WN:
		case HALLWAY_SN:
			return ESkyDirection.NORTH;
		case CURVE_ES:
		case CURVE_WS:
		case HALLWAY_NS:
			return ESkyDirection.SOUTH;
		case CURVE_NE:
		case CURVE_SE:
		case HALLWAY_WE:
			return ESkyDirection.EAST;
		case CURVE_NW:
		case CURVE_SW:
		case HALLWAY_EW:
			return ESkyDirection.WEST;
		default: 
			return null;
		}
	}
	
}
