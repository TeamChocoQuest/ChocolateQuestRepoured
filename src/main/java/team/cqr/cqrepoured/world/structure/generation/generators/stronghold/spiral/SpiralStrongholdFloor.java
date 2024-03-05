package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral;

import java.io.File;
import java.util.Random;

import net.minecraft.util.Tuple;
import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.world.structure.generation.generation.CQRStructurePiece;
import team.cqr.cqrepoured.world.structure.generation.generators.stronghold.EStrongholdRoomType;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class SpiralStrongholdFloor {

	private final Random random;
	private CQRStructurePiece.Builder dungeonBuilder;
	private Tuple<Integer, Integer> entranceCoordinates;
	private Tuple<Integer, Integer> entranceIndex;
	private Tuple<Integer, Integer> exitCoordinates;
	private Tuple<Integer, Integer> exitIndex;
	private boolean isLastFloor = false;
	private int sideLength;
	private int roomCount;
	private EStrongholdRoomType[][] roomGrid;
	private BlockPos[][] coordinateGrid;

	public SpiralStrongholdFloor(CQRStructurePiece.Builder dungeonBuilder, Tuple<Integer, Integer> entrancePos, int entranceX, int entranceZ, boolean isLastFloor, int sideLength, int roomCount, Random rand) {
		this.dungeonBuilder = dungeonBuilder;
		this.entranceCoordinates = entrancePos;
		this.entranceIndex = new Tuple<>(entranceX, entranceZ);
		this.isLastFloor = isLastFloor;
		this.sideLength = sideLength;
		this.roomCount = roomCount;
		this.roomGrid = new EStrongholdRoomType[sideLength][sideLength];
		this.coordinateGrid = new BlockPos[sideLength][sideLength];
		this.random = rand;
	}

	public void calculateRoomGrid(EStrongholdRoomType entranceRoomType, boolean rev) {
		int x = this.entranceIndex.getA();
		int z = this.entranceIndex.getB();
		while (this.roomCount > 0) {
			this.roomCount--;
			if (this.roomCount == 0) {
				this.exitIndex = new Tuple<>(x, z);
				if (this.isLastFloor) {
					this.roomGrid[x][z] = EStrongholdRoomType.BOSS;
				} else {
					this.roomGrid[x][z] = this.getExitRoomType(x, z, rev);
				}
				break;
			}
			if (x == 0 && z == 0) {
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_SE;
					x += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_ES;
					z += 1;
				}
				continue;
			}
			if (x == (this.sideLength - 1) && z == (this.sideLength - 1)) {
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_NW;
					x -= 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_WN;
					z -= 1;
				}
				continue;
			}
			if (x == 0 && z == (this.sideLength - 1)) {
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_EN;
					z -= 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_NE;
					x += 1;
				}
				continue;
			}
			if (x == (this.sideLength - 1) && z == 0) {
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_WS;
					z += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.CURVE_SW;
					x -= 1;
				}
				continue;
			}
			if (x == 0) {
				// Left side
				if (!rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_NS;
					z += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_SN;
					z -= 1;
				}
				continue;
			}
			if (x == (this.sideLength - 1)) {
				// Right side
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_NS;
					z += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_SN;
					z -= 1;
				}
				continue;
			}
			if (z == 0) {
				// Bottom side
				if (rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
			if (z == (this.sideLength - 1)) {
				// Top side
				if (!rev) {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_WE;
					x += 1;
				} else {
					this.roomGrid[x][z] = EStrongholdRoomType.HALLWAY_EW;
					x -= 1;
				}
				continue;
			}
		}
		this.roomGrid[this.entranceIndex.getA()][this.entranceIndex.getB()] = entranceRoomType;
		// System.out.println("Done");
	}

	private EStrongholdRoomType getExitRoomType(int iX, int iZ, boolean rev) {
		if (iX == 0 && iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_SS : EStrongholdRoomType.STAIR_EE;
		}
		if (iX == 0 && iZ == (this.sideLength - 1)) {
			return rev ? EStrongholdRoomType.STAIR_EE : EStrongholdRoomType.STAIR_NN;
		}
		if (iX == (this.sideLength - 1) && iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_WW : EStrongholdRoomType.STAIR_SS;
		}
		if (iX == (this.sideLength - 1) && iZ == (this.sideLength - 1)) {
			return rev ? EStrongholdRoomType.STAIR_NN : EStrongholdRoomType.STAIR_WW;
		}

		// Stairs not in corners
		if (iZ == 0) {
			return rev ? EStrongholdRoomType.STAIR_WW : EStrongholdRoomType.STAIR_EE;
		}
		if (iZ == (this.sideLength - 1)) {
			return rev ? EStrongholdRoomType.STAIR_EE : EStrongholdRoomType.STAIR_WW;
		}
		if (iX == 0) {
			return rev ? EStrongholdRoomType.STAIR_SS : EStrongholdRoomType.STAIR_NN;
		}
		if (iX == (this.sideLength - 1)) {
			return rev ? EStrongholdRoomType.STAIR_NN : EStrongholdRoomType.STAIR_SS;
		}
		return EStrongholdRoomType.NONE;
	}

	public void calculateCoordinates(int y, int roomSizeX, int roomSizeZ) {
		BlockPos entrancePos = new BlockPos(this.entranceCoordinates.getA(), y, this.entranceCoordinates.getB());
		this.coordinateGrid[this.entranceIndex.getA()][this.entranceIndex.getB()] = entrancePos;
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if ((iX == 0 || iX == (this.sideLength - 1)) || (iZ == 0 || iZ == (this.sideLength - 1))) {
					EStrongholdRoomType room = this.roomGrid[iX][iZ];
					if (room != null && room != EStrongholdRoomType.NONE) {
						int x = (iX - this.entranceIndex.getA()) * roomSizeX;
						x += entrancePos.getX();
						int z = (iZ - this.entranceIndex.getB()) * roomSizeZ;
						z += entrancePos.getZ();
						this.coordinateGrid[iX][iZ] = new BlockPos(x, y, z);
					}
				}
			}
		}
		this.coordinateGrid[this.entranceIndex.getA()][this.entranceIndex.getB()] = entrancePos;
		if (!this.isLastFloor) {
			int x = (this.exitIndex.getA() - this.entranceIndex.getA()) * roomSizeX;
			x += entrancePos.getX();
			int z = (this.exitIndex.getB() - this.entranceIndex.getB()) * roomSizeZ;
			z += entrancePos.getZ();
			this.coordinateGrid[this.exitIndex.getA()][this.exitIndex.getB()] = new BlockPos(x, y, z);
			this.exitCoordinates = new Tuple<>(x, z);
		}
	}

	public Tuple<Integer, Integer> getExitCoordinates() {
		return this.exitCoordinates;
	}

	public Tuple<Integer, Integer> getExitIndex() {
		return this.exitIndex;
	}

	public void overrideFirstRoomType(EStrongholdRoomType type) {
		this.roomGrid[this.entranceIndex.getA()][this.entranceIndex.getB()] = type;
	}

	public void overrideLastRoomType(EStrongholdRoomType type) {
		if (!this.isLastFloor) {
			this.roomGrid[this.exitIndex.getA()][this.exitIndex.getB()] = type;
		}
	}

	public EStrongholdRoomType[][] getRoomGrid() {
		return this.roomGrid;
	}

	public void buildRooms(DungeonVolcano dungeon, CQRStructurePiece.Builder builder) {
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if ((iX == 0 || iX == (this.sideLength - 1)) || (iZ == 0 || iZ == (this.sideLength - 1))) {
					EStrongholdRoomType type = this.roomGrid[iX][iZ];
					if (type != null && type != EStrongholdRoomType.NONE) {
						if (dungeon != null && builder != null) {
							File file = dungeon.getRoomNBTFileForType(type, this.random);
							if (file != null) {
								CQStructure room = CQStructure.createFromFile(file);
								room.addAll(this.dungeonBuilder, this.coordinateGrid[iX][iZ], Offset.CENTER);
							}
						}
					}
				}
			}
		}
	}

	public EStrongholdRoomType getExitRoomType() {
		return this.roomGrid[this.exitIndex.getA()][this.exitIndex.getB()];
	}

}
