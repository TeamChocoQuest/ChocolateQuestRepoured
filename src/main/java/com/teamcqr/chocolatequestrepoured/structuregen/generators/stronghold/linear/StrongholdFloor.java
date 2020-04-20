package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.io.File;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdLinearGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdFloor {

	private StrongholdLinearGenerator generator;
	private int sideLength;
	private EStrongholdRoomType[][] roomPattern;
	//Where do we face currently? its the direction we face after exiting the last part we were in
	private ESkyDirection currentDirection;
	private boolean lastFloor;
	private int lastX, lastZ;
	
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
		//When you enter the curve: you face the OPPOSITE of FIRST_LETTER, when you leave you face SECOND_LETTER
		case EAST:
			room = curve ? EStrongholdRoomType.CURVE_WN : EStrongholdRoomType.HALLWAY_WE;
			break;
		case NORTH:
			room = curve ? EStrongholdRoomType.CURVE_SW : EStrongholdRoomType.HALLWAY_SN;
			break;
		case SOUTH:
			room = curve ? EStrongholdRoomType.CURVE_NE : EStrongholdRoomType.HALLWAY_NS;
			break;
		case WEST:
			room = curve ? EStrongholdRoomType.CURVE_ES : EStrongholdRoomType.HALLWAY_EW;
			break;
		} 
		this.currentDirection = prevFloorExitDir;
		/*if(!curve) {
			this.currentDirection = this.currentDirection.getOpposite();
		}*/
		Tuple<Integer, Integer> roomCoord = getNextRoomCoordinates(gridPosX, gridPosZ, this.currentDirection);
		//System.out.println("X: " + gridPosX + "    Z: " + gridPosZ + "        Room: 0");
		//System.out.println("X: " + roomCoord.getFirst() + "    Z: " + roomCoord.getSecond() + "        Room: 1");
		setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), room);
		this.currentDirection = getRoomExitDirection(room);
		
		int roomCount = this.sideLength * this.sideLength;
		roomCount -= 2;
		boolean reversed = !curve;
		int curveCount = 4;
		Tuple<Integer, Integer> prevCoords = new Tuple<>(roomCoord.getFirst(), roomCoord.getSecond());
		System.out.println("Beginning gen...");
		while(roomCount > 0) {
			prevCoords = new Tuple<>(roomCoord.getFirst(), roomCoord.getSecond());
			roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), this.currentDirection);
			//System.out.println("X: " + roomCoord.getFirst() + "    Z: " + roomCoord.getSecond() + "        Room: " + ((this.sideLength * this.sideLength) - roomCount));
			roomCount--;
			if(roomCount == 0 && (!reversed || (roomCoord.getFirst() == 0 && roomCoord.getSecond() == 0))) {
				//DONE: Handle stair or boss room
				if(lastFloor) {
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), EStrongholdRoomType.BOSS);
				} else {
					//Handle stair
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getStair(currentDirection));
					this.currentDirection = getRoomExitDirection(getStair(currentDirection));
				}
				break;
			}
			//TODO: Move decission about curve to own method
			//DONE: Rewrite curve, hallway and stair calculation so DIRECTION is used consistently
			if(isCurveRoom(roomCoord.getFirst(), roomCoord.getSecond())) {
				curveCount--;
				if(curveCount == 0) {
					curveCount = 4;
					if(reversed && getRoomAt(roomCoord) != null) {
						//We are going "backwards" (left turns, not right turns) and the next curve room is also set, so it is NOT a curve room
						roomCoord = prevCoords;
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
						this.currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
						
						//As we needed to "fuck go back", we need to increment the room counter again so taht we dont forget one room
						roomCount++;
						
						roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection);
						if(!(roomCoord.getFirst() == 0 && roomCoord.getSecond() == 0)) {
							setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getHallway(this.currentDirection));
							roomCount--;
							this.currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
						}
					} else {
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getHallway(this.currentDirection));
						roomCount--;
						roomCoord = getNextRoomCoordinates(roomCoord.getFirst(), roomCoord.getSecond(), currentDirection);
						//System.out.println("Setting extra room...");
						setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
						this.currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
					}
					//System.out.println("X: " + roomCoord.getFirst() + "    Z: " + roomCoord.getSecond() + "        Room: " + ((this.sideLength * this.sideLength) - roomCount));
				} else {
					//DONE Curve
					setRoomType(roomCoord.getFirst(), roomCoord.getSecond(), getCurve(this.currentDirection, reversed));
					this.currentDirection = getRoomExitDirection(getCurve(this.currentDirection, reversed));
				}
				continue;
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
			return EStrongholdRoomType.HALLWAY_WE;
		case NORTH:
			return EStrongholdRoomType.HALLWAY_SN;
		case SOUTH:
			return EStrongholdRoomType.HALLWAY_NS;
		case WEST:
			return EStrongholdRoomType.HALLWAY_EW;
		default:
			return null;
		}
	}
	
	private EStrongholdRoomType getStair(ESkyDirection dir) {
		switch(dir) {
		case EAST:
			return EStrongholdRoomType.STAIR_WW;
		case NORTH:
			return EStrongholdRoomType.STAIR_SS;
		case SOUTH:
			return EStrongholdRoomType.STAIR_NN;
		case WEST:
			return EStrongholdRoomType.STAIR_EE;
		default:
			return null;
		}
	}
	
	private EStrongholdRoomType getCurve(ESkyDirection dir, boolean reversed) {
		//When you enter the curve: you face the OPPOSITE of FIRST_LETTER, when you leave you face SECOND_LETTER
		switch(dir) {
		case EAST:
			return reversed ? EStrongholdRoomType.CURVE_WS : EStrongholdRoomType.CURVE_WN;
		case NORTH:
			return reversed ? EStrongholdRoomType.CURVE_SE : EStrongholdRoomType.CURVE_SW;
		case SOUTH:
			return reversed ? EStrongholdRoomType.CURVE_NW : EStrongholdRoomType.CURVE_NE;
		case WEST:
			return reversed ? EStrongholdRoomType.CURVE_EN : EStrongholdRoomType.CURVE_ES;
		default:
			return null;
		}
	}
	
	public void generateRooms(int centerX, int centerZ, int y, PlacementSettings settings, List<List<? extends IStructure>> lists, World world) {
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				EStrongholdRoomType room = roomPattern[iX][iZ];
				if(room != null && room!=EStrongholdRoomType.NONE) {
					Tuple<Integer,Integer> gridPos = arrayIndiciesToGridPos(new Tuple<>(iX, iZ));
					int x = centerX + (gridPos.getFirst() * generator.getDungeon().getRoomSizeX());
					int z = centerZ + (gridPos.getSecond() * generator.getDungeon().getRoomSizeZ());
					int y1 = y;
					if(room.toString().startsWith("STAIR_")) {
						y1 -= generator.getDungeon().getRoomSizeY();
					}
					BlockPos pos = new BlockPos(x,y1,z);
					File struct = generator.getDungeon().getRoom(room);
					if(struct != null) {
						CQStructure structure = new CQStructure(struct);
						for (List<? extends IStructure> list : structure.addBlocksToWorld(world, pos, settings, EPosType.CENTER_XZ_LAYER, generator.getDungeon(), centerX, centerZ)) {
							lists.add(list);
						}
					}
				}
			}
		}
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
		return new Tuple<>(lastX,lastZ);
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
		//When you enter the stair, you face the opposite direction
		return currentDirection;
	}
	
	private boolean isCurveRoom(int gpX, int gpZ) {
		return Math.abs(gpX) == Math.abs(gpZ);
	}

	private void setRoomType(int gpX, int gpZ, EStrongholdRoomType type) {
		Tuple<Integer, Integer> coords = gridPosToArrayIndices(new Tuple<>(gpX, gpZ));
		//if(roomPattern[coords.getFirst()][coords.getSecond()] == null) {
			lastX = gpX;
			lastZ = gpZ;
			System.out.println("X: " + gpX + "    Z: " + gpZ + "        Room: " + type.toString());
			this.roomPattern[coords.getFirst()][coords.getSecond()] = type;
		//}
	}
	
	private ESkyDirection getRoomExitDirection(EStrongholdRoomType room) {
		switch(room) {
		case CURVE_EN:
		case CURVE_WN:
		case HALLWAY_SN:
		case STAIR_NN:
			return ESkyDirection.NORTH;
		case CURVE_ES:
		case CURVE_WS:
		case HALLWAY_NS:
		case STAIR_SS:
			return ESkyDirection.SOUTH;
		case CURVE_NE:
		case CURVE_SE:
		case HALLWAY_WE:
		case STAIR_EE:
			return ESkyDirection.EAST;
		case CURVE_NW:
		case CURVE_SW:
		case HALLWAY_EW:
		case STAIR_WW:
			return ESkyDirection.WEST;
		default: 
			return null;
		}
	}
	
}
