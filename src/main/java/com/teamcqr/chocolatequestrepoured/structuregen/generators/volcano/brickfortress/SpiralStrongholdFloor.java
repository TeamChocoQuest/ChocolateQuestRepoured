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
		int x = entranceIndex.getFirst();
		int z = entranceIndex.getSecond();
		while(roomCount > 0) {
			roomCount--;
			if(roomCount == 0) {
				exitIndex = new Tuple<>(x,z);
				if(isLastFloor) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.BOSS;
				} else {
					roomGrid[x][z] = getExitRoomType(x, z, rev);
				}
				break;
			}
			if(x == 0 && z == 0) {
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_NE;
					x += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_EN;
					z += 1;
				}
				continue;
			}
			if(x == (sideLength -1) && z == (sideLength -1)) {
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_SW;
					x -= 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_WS;
					z -= 1;
				}
				continue;
			}
			if(x == 0 && z == (sideLength -1)) {
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_ES;
					z -= 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_SE;
					x += 1;
				}
				continue;
			}
			if(x == (sideLength -1) && z == 0) {
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_WN;
					z += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.CURVE_NW;
					x -= 1;
				}
				continue;
			}
			if(x == 0) {
				//Left side
				if(!rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_SN;
					z += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_NS;
					z -= 1;
				}
				continue;
			}
			if(x == (sideLength -1)) {
				//Right side
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_SN;
					z += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_NS;
					z -= 1;
				}
				continue;
			}
			if(z == 0) {
				//Bottom side
				if(rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
			if(z == (sideLength -1)) {
				//Top side
				if(!rev) {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					roomGrid[x][z] = ESpiralStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
		}
		roomGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entranceRoomType;
		//System.out.println("Done");
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
					ESpiralStrongholdRoomType room = roomGrid[iX][iZ];
					if(room != null && !room.equals(ESpiralStrongholdRoomType.NONE)) {
						int x = (iX - entranceIndex.getFirst()) * roomSizeX;
						x += entrancePos.getX();
						int z = (iZ - entranceIndex.getSecond()) * roomSizeZ;
						z += entrancePos.getZ();
						coordinateGrid[iX][iZ] = new BlockPos(x,y,z);
					}
				}
			}
		}
		coordinateGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entrancePos;
		if(!this.isLastFloor) {
			int x = (exitIndex.getFirst() - entranceIndex.getFirst()) * roomSizeX;
			x += entrancePos.getX();
			int z = (exitIndex.getSecond() - entranceIndex.getSecond()) * roomSizeZ;
			z += entrancePos.getZ();
			coordinateGrid[exitIndex.getFirst()][exitIndex.getSecond()] = new BlockPos(x,y,z);
			exitCoordinates = new Tuple<>(x, z);
		}
	}
	
	public Tuple<Integer, Integer> getExitCoordinates() {
		return exitCoordinates;
	}
	public Tuple<Integer, Integer> getExitIndex() {
		return exitIndex;
	}
	
	public void overrideFirstRoomType(ESpiralStrongholdRoomType type) {
		roomGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = type;
	}
	public void overrideLastRoomType(ESpiralStrongholdRoomType type) {
		if(!isLastFloor) {
			roomGrid[exitIndex.getFirst()][exitIndex.getSecond()] = type;
		}
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
						if(dungeon != null && world != null) {
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
	}
	public ESpiralStrongholdRoomType getExitRoomType() {
		return roomGrid[exitIndex.getFirst()][exitIndex.getSecond()];
	}

}
