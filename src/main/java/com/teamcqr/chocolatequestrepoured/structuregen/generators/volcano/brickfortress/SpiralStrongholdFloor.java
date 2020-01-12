package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

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
	private BlockPos[][] coordinateGrid;
	
	public SpiralStrongholdFloor(Tuple<Integer, Integer> entrancePos, int entranceX, int entranceZ, boolean isLastFloor, int sideLength, int roomCount) {
		this.entranceCoordinates = entrancePos;
		this.entranceIndex = new Tuple<>(entranceX, entranceZ);
		this.isLastFloor = isLastFloor;
		this.sideLength = sideLength;
		this.roomCount = roomCount;
		this.roomGrid = new ESpiralStrongholdRoomType[sideLength][sideLength];
		this.coordinateGrid = new BlockPos[sideLength][sideLength];
	}
	
	public void calculateRoomGrid(ESpiralStrongholdRoomType entranceRoomType, boolean rev) {
		this.entranceRoomType = entranceRoomType;
		for(int iX = 0; iX < sideLength && roomCount > 0; iX++) {
			for(int iZ = 0; iZ < sideLength && roomCount > 0; iZ++) {
				if((iX == 0 || iX == (sideLength -1)) || (iZ == 0 || iZ == (sideLength -1))) {
					roomCount--;
					if(iX == entranceIndex.getFirst() && iZ == entranceIndex.getSecond()) {
						roomGrid[iX][iZ] = entranceRoomType;
					} else {
						if(isLastRoom(iX, iZ) || (roomCount == 0 && isLastFloor)) {
							handleLastRoom(iX, iZ, rev);
						} else {
							//Curves / Edges
							if(iX == 0 && iZ == 0) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.CURVE_NE : ESpiralStrongholdRoomType.CURVE_EN; 
								continue;
							}
							if(iX == 0 && iZ == (sideLength -1)) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.CURVE_ES : ESpiralStrongholdRoomType.CURVE_SE;
								continue;
							}
							if(iX == (sideLength -1) && iZ == 0) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.CURVE_WN : ESpiralStrongholdRoomType.CURVE_NW;
								continue;
							}
							if(iX == (sideLength -1) && iZ == (sideLength -1)) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.CURVE_WS : ESpiralStrongholdRoomType.CURVE_SW;
								continue;
							}
							
							//Hallways / Straight
							if(iZ == 0) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.HALLWAY_WE : ESpiralStrongholdRoomType.HALLWAY_EW;
								continue;
							}
							if(iZ == (sideLength -1)) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.HALLWAY_EW : ESpiralStrongholdRoomType.HALLWAY_WE;
								continue;
							}
							if(iX == 0) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.HALLWAY_NS : ESpiralStrongholdRoomType.HALLWAY_SN;
								continue;
							}
							if(iX == (sideLength -1)) {
								roomGrid[iX][iZ] = rev ? ESpiralStrongholdRoomType.HALLWAY_SN : ESpiralStrongholdRoomType.HALLWAY_NS;
								continue;
							}
						}
					}
				}
			}
		}
		System.out.println("Done");
	}
	
	private void handleLastRoom(int iX, int iZ, boolean rev) {
		ESpiralStrongholdRoomType room = ESpiralStrongholdRoomType.NONE;
		if(isLastFloor && roomCount == 0) {
			//This is the last floor, we wont go higher, we place the boss room here
			room = ESpiralStrongholdRoomType.BOSS;
		} else {
			//STairs in corners
			room = getExitRoomType(iX, iZ, rev);
		}
		exitIndex = new Tuple<>(iX, iZ);
		roomGrid[iX][iZ] = room;
	}
	
	private ESpiralStrongholdRoomType getExitRoomType(int iX, int iZ, boolean rev) {
		if(iX == 0 && iZ == 0) {
			return rev ? ESpiralStrongholdRoomType.STAIR_NN : ESpiralStrongholdRoomType.STAIR_EE;
		}
		if(iX == 0 && iZ == (sideLength -1)) {
			return rev ? ESpiralStrongholdRoomType.STAIR_EE : ESpiralStrongholdRoomType.STAIR_SS;
		}
		if(iX == (sideLength -1) && iZ == 0) {
			return rev ? ESpiralStrongholdRoomType.STAIR_WW : ESpiralStrongholdRoomType.STAIR_NN;
		}
		if(iX == (sideLength -1) && iZ == (sideLength -1)) {
			return rev ? ESpiralStrongholdRoomType.STAIR_SS : ESpiralStrongholdRoomType.STAIR_WW;
		}
		
		//Stairs not in corners
		if(iZ == 0) {
			return rev ? ESpiralStrongholdRoomType.STAIR_WW : ESpiralStrongholdRoomType.STAIR_EE;
		}
		if(iZ == (sideLength -1)) {
			return rev ? ESpiralStrongholdRoomType.STAIR_EE : ESpiralStrongholdRoomType.STAIR_WW;
		}
		if(iX == 0) {
			return rev ? ESpiralStrongholdRoomType.STAIR_NN : ESpiralStrongholdRoomType.STAIR_SS;
		}
		if(iX == (sideLength -1)) {
			return rev ? ESpiralStrongholdRoomType.STAIR_SS : ESpiralStrongholdRoomType.STAIR_NN;
		}
		return ESpiralStrongholdRoomType.NONE;
	}
	
	public void calculateCoordinates(int y, int roomSizeX, int roomSizeZ) {
		BlockPos entrancePos = new BlockPos(entranceCoordinates.getFirst(), y, entranceCoordinates.getSecond());
		coordinateGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entrancePos;
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				if((iX == 0 || iX == (sideLength -1)) || (iZ == 0 || iZ == (sideLength -1))) {
					//if(iX != entranceIndex.getFirst() && iZ != entranceIndex.getSecond()) {
						ESpiralStrongholdRoomType room = roomGrid[iX][iZ];
						if(room != null && !room.equals(ESpiralStrongholdRoomType.NONE)) {
							int x = (iX - entranceIndex.getFirst()) * roomSizeX;
							x += entrancePos.getX();
							int z = (iZ - entranceIndex.getSecond()) * roomSizeZ;
							z += entrancePos.getZ();
							coordinateGrid[iX][iZ] = new BlockPos(x,y,z);
						}
					//}
				}
			}
		}
		coordinateGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entrancePos;
		int x = (exitIndex.getFirst() - entranceIndex.getFirst()) * roomSizeX;
		x += entrancePos.getX();
		int z = (exitIndex.getSecond() - entranceIndex.getSecond()) * roomSizeZ;
		z += entrancePos.getZ();
		coordinateGrid[exitIndex.getFirst()][exitIndex.getSecond()] = new BlockPos(x,y,z);
		exitCoordinates = new Tuple<>(x, z);
	}
	
	public Tuple<Integer, Integer> getExitCoordinates() {
		return exitCoordinates;
	}

	private boolean isLastRoom(int iX, int iZ) {
		if(roomCount == 0) {
			return true;
		}
		if((iX == entranceIndex.getFirst() -1 || iX == entranceIndex.getFirst() +1 || iX == entranceIndex.getFirst())
			&& (iZ == entranceIndex.getSecond() -1 || iZ == entranceIndex.getSecond() +1 || iZ == entranceIndex.getSecond())) {
			double dx = iX - entranceIndex.getFirst();
			double dz = iZ - entranceIndex.getSecond();
			double distance = Math.sqrt(dx * dx + dz * dz);
			//System.out.println("Indx: " + iX + "|" + iZ + "  Distance: " + distance);
			boolean distBool = distance <= 1;
			if(distBool) {
				switch(entranceRoomType) {
				case CURVE_ES:
					return iX == entranceIndex.getFirst() && iZ == (entranceIndex.getSecond() +1);
				case CURVE_NE:
					return (iX == (entranceIndex.getFirst() -1) && iZ == entranceIndex.getSecond());
				case CURVE_SW:
					return (iX == (entranceIndex.getFirst() +1) && iZ == entranceIndex.getSecond());
				case CURVE_WN:
					return (iX == entranceIndex.getFirst() && iZ == (entranceIndex.getSecond() -1));
				case STAIR_EE:
					return (iX == (entranceIndex.getFirst() -1) && iZ == entranceIndex.getSecond());
				case STAIR_NN:
					return (iX == entranceIndex.getFirst() && iZ == (entranceIndex.getSecond() -1));
				case STAIR_SS:
					return (iX == entranceIndex.getFirst() && iZ == (entranceIndex.getSecond() +1));
				case STAIR_WW:
					return (iX == (entranceIndex.getFirst() +1) && iZ == entranceIndex.getSecond());
				default:
					break;
				}
			}
		}
		return false;
	}
	
	public void overrideFirstRoomType(ESpiralStrongholdRoomType type) {
		roomGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = type;
	}
	public void overrideLastRoomType(ESpiralStrongholdRoomType type) {
		roomGrid[exitIndex.getFirst()][exitIndex.getSecond()] = type;
	}

	public ESpiralStrongholdRoomType[][] getRoomGrid() {
		return roomGrid;
	}
	
	public void buildRooms(VolcanoDungeon dungeon, int dunX, int dunZ, World world) {
		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				if((iX == 0 || iX == (sideLength -1)) || (iZ == 0 || iZ == (sideLength -1))) {
					ESpiralStrongholdRoomType type = roomGrid[iX][iZ];
					if(type != null && !type.equals(ESpiralStrongholdRoomType.NONE)) {
						File file = dungeon.getRoomNBTFileForType(type);
						if(file != null) {
							CQStructure room = new CQStructure(file, dungeon, dunX, dunZ, dungeon.isProtectedFromModifications());
							room.placeBlocksInWorld(world, coordinateGrid[iX][iZ], settings, EPosType.CENTER_XZ_LAYER);
						}
					}
				}
			}
		}
	}
	public ESpiralStrongholdRoomType getExitRoomType() {
		return roomGrid[exitIndex.getFirst()][exitIndex.getSecond()];
	}

}
