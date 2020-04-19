package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
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
	private EStrongholdRoomType[][] roomGrid;
	private BlockPos[][] coordinateGrid;
	
	public SpiralStrongholdFloor(Tuple<Integer, Integer> entrancePos, int entranceX, int entranceZ, boolean isLastFloor, int sideLength, int roomCount) {
		this.entranceCoordinates = entrancePos;
		this.entranceIndex = new Tuple<>(entranceX, entranceZ);
		this.isLastFloor = isLastFloor;
		this.sideLength = sideLength;
		this.roomCount = roomCount;
		this.roomGrid = new EStrongholdRoomType[sideLength][sideLength];
		this.coordinateGrid = new BlockPos[sideLength][sideLength];
	}
	
	public void calculateRoomGrid(EStrongholdRoomType entranceRoomType, boolean rev) {
		int x = entranceIndex.getFirst();
		int z = entranceIndex.getSecond();
		while(roomCount > 0) {
			roomCount--;
			if(roomCount == 0) {
				exitIndex = new Tuple<>(x,z);
				if(isLastFloor) {
					roomGrid[x][z] = EStrongholdRoomType.BOSS;
				} else {
					roomGrid[x][z] = getExitRoomType(x, z, rev);
				}
				break;
			}
			if(x == 0 && z == 0) {
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_SE;
					x += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_ES;
					z += 1;
				}
				continue;
			}
			if(x == (sideLength -1) && z == (sideLength -1)) {
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_NW;
					x -= 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_WN;
					z -= 1;
				}
				continue;
			}
			if(x == 0 && z == (sideLength -1)) {
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_EN;
					z -= 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_NE;
					x += 1;
				}
				continue;
			}
			if(x == (sideLength -1) && z == 0) {
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_WS;
					z += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.CURVE_SW;
					x -= 1;
				}
				continue;
			}
			if(x == 0) {
				//Left side
				if(!rev) {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_SN;
					z += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_NS;
					z -= 1;
				}
				continue;
			}
			if(x == (sideLength -1)) {
				//Right side
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_SN;
					z += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_NS;
					z -= 1;
				}
				continue;
			}
			if(z == 0) {
				//Bottom side
				if(rev) {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
			if(z == (sideLength -1)) {
				//Top side
				if(!rev) {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					roomGrid[x][z] = EStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
		}
		roomGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entranceRoomType;
		//System.out.println("Done");
	}
	
	private EStrongholdRoomType getExitRoomType(int iX, int iZ, boolean rev) {
		if(iX == 0 && iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_NN : EStrongholdRoomType.STAIR_EE;
		}
		if(iX == 0 && iZ == (sideLength -1)) {
			return rev ? EStrongholdRoomType.STAIR_EE : EStrongholdRoomType.STAIR_SS;
		}
		if(iX == (sideLength -1) && iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_WW : EStrongholdRoomType.STAIR_NN;
		}
		if(iX == (sideLength -1) && iZ == (sideLength -1)) {
			return rev ? EStrongholdRoomType.STAIR_SS : EStrongholdRoomType.STAIR_WW;
		}
		
		//Stairs not in corners
		if(iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_WW : EStrongholdRoomType.STAIR_EE;
		}
		if(iZ == (sideLength -1)) {
			return rev ? EStrongholdRoomType.STAIR_EE : EStrongholdRoomType.STAIR_WW;
		}
		if(iX == 0) {
			return rev ? EStrongholdRoomType.STAIR_NN : EStrongholdRoomType.STAIR_SS;
		}
		if(iX == (sideLength -1)) {
			return rev ? EStrongholdRoomType.STAIR_SS : EStrongholdRoomType.STAIR_NN;
		}
		return EStrongholdRoomType.NONE;
	}
	
	public void calculateCoordinates(int y, int roomSizeX, int roomSizeZ) {
		BlockPos entrancePos = new BlockPos(entranceCoordinates.getFirst(), y, entranceCoordinates.getSecond());
		coordinateGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = entrancePos;
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				if((iX == 0 || iX == (sideLength -1)) || (iZ == 0 || iZ == (sideLength -1))) {
					EStrongholdRoomType room = roomGrid[iX][iZ];
					if(room != null && !room.equals(EStrongholdRoomType.NONE)) {
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
	
	public void overrideFirstRoomType(EStrongholdRoomType type) {
		roomGrid[entranceIndex.getFirst()][entranceIndex.getSecond()] = type;
	}
	public void overrideLastRoomType(EStrongholdRoomType type) {
		if(!isLastFloor) {
			roomGrid[exitIndex.getFirst()][exitIndex.getSecond()] = type;
		}
	}

	public EStrongholdRoomType[][] getRoomGrid() {
		return roomGrid;
	}
	
	public List<List<? extends IStructure>> buildRooms(DungeonVolcano dungeon, int dunX, int dunZ, World world) {
		List<List<? extends IStructure>> strongholdParts = new ArrayList<>();
		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		for(int iX = 0; iX < sideLength; iX++) {
			for(int iZ = 0; iZ < sideLength; iZ++) {
				if((iX == 0 || iX == (sideLength -1)) || (iZ == 0 || iZ == (sideLength -1))) {
					EStrongholdRoomType type = roomGrid[iX][iZ];
					if(type != null && !type.equals(EStrongholdRoomType.NONE)) {
						if(dungeon != null && world != null) {
							File file = dungeon.getRoomNBTFileForType(type);
							if(file != null) {
								CQStructure room = new CQStructure(file);
								for (List<? extends IStructure> list : room.addBlocksToWorld(world, coordinateGrid[iX][iZ], settings, EPosType.CENTER_XZ_LAYER, dungeon, dunX, dunZ)) {
									strongholdParts.add(list);
								}
							}
						}
					}
				}
			}
		}
		return strongholdParts;
	}
	public EStrongholdRoomType getExitRoomType() {
		return roomGrid[exitIndex.getFirst()][exitIndex.getSecond()];
	}

}
