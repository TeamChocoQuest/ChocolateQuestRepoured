package com.example.chocolatequest.world.structure.generation.generators.stronghold.spiral;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import com.example.chocolatequest.world.structure.generation.generators.stronghold.EStrongholdRoomType;
import com.example.chocolatequest.world.structure.generation.piece.CQRTemplatePiece;

public class SpiralStrongholdFloor {

	private final RandomSource random;
	private final StructurePiecesBuilder builder;
	private int entranceXCoord, entranceZCoord;
	private int entranceIndexX, entranceIndexZ;
	private int exitCoordinatesX, exitCoordinatesZ;
	private int exitIndexX, exitIndexZ;
	private boolean isLastFloor = false;
	private int sideLength;
	private int roomCount;
	private EStrongholdRoomType[][] roomGrid;
	private BlockPos[][] coordinateGrid;
	private final String entityReplacement;
	private final String templateBasePath;

	public SpiralStrongholdFloor(StructurePiecesBuilder builder, int entranceXCoord, int entranceZCoord, int entranceIndexX, int entranceIndexZ, boolean isLastFloor, int sideLength, int roomCount, RandomSource rand, String entityReplacement, String templateBasePath) {
		this.builder = builder;
		this.entranceXCoord = entranceXCoord;
		this.entranceZCoord = entranceZCoord;
		this.entranceIndexX = entranceIndexX;
		this.entranceIndexZ = entranceIndexZ;
		this.isLastFloor = isLastFloor;
		this.sideLength = sideLength;
		this.roomCount = roomCount;
		this.roomGrid = new EStrongholdRoomType[sideLength][sideLength];
		this.coordinateGrid = new BlockPos[sideLength][sideLength];
		this.random = rand;
		this.entityReplacement = entityReplacement;
		this.templateBasePath = templateBasePath;
	}

	public void calculateRoomGrid(EStrongholdRoomType entranceRoomType, boolean rev) {
		int x = this.entranceIndexX;
		int z = this.entranceIndexZ;
		while (this.roomCount > 0) {
			this.roomCount--;
			if (this.roomCount == 0) {
				this.exitIndexX = x;
				this.exitIndexZ = z;
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
		this.roomGrid[this.entranceIndexX][this.entranceIndexZ] = entranceRoomType;
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
		BlockPos entrancePos = new BlockPos(this.entranceXCoord, y, this.entranceZCoord);
		this.coordinateGrid[this.entranceIndexX][this.entranceIndexZ] = entrancePos;
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if ((iX == 0 || iX == (this.sideLength - 1)) || (iZ == 0 || iZ == (this.sideLength - 1))) {
					EStrongholdRoomType room = this.roomGrid[iX][iZ];
					if (room != null && room != EStrongholdRoomType.NONE) {
						int x = (iX - this.entranceIndexX) * roomSizeX;
						x += entrancePos.getX();
						int z = (iZ - this.entranceIndexZ) * roomSizeZ;
						z += entrancePos.getZ();
						this.coordinateGrid[iX][iZ] = new BlockPos(x, y, z);
					}
				}
			}
		}
		this.coordinateGrid[this.entranceIndexX][this.entranceIndexZ] = entrancePos;
		if (!this.isLastFloor) {
			int x = (this.exitIndexX - this.entranceIndexX) * roomSizeX;
			x += entrancePos.getX();
			int z = (this.exitIndexZ - this.entranceIndexZ) * roomSizeZ;
			z += entrancePos.getZ();
			this.coordinateGrid[this.exitIndexX][this.exitIndexZ] = new BlockPos(x, y, z);
			this.exitCoordinatesX = x;
			this.exitCoordinatesZ = z;
		}
	}

	public int getExitCoordinatesX() { return exitCoordinatesX; }
	public int getExitCoordinatesZ() { return exitCoordinatesZ; }
	public int getExitIndexX() { return exitIndexX; }
	public int getExitIndexZ() { return exitIndexZ; }

	public void overrideFirstRoomType(EStrongholdRoomType type) {
		this.roomGrid[this.entranceIndexX][this.entranceIndexZ] = type;
	}

	public void overrideLastRoomType(EStrongholdRoomType type) {
		if (!this.isLastFloor) {
			this.roomGrid[this.exitIndexX][this.exitIndexZ] = type;
		}
	}

	public void buildRooms() {
		for (int iX = 0; iX < this.sideLength; iX++) {
			for (int iZ = 0; iZ < this.sideLength; iZ++) {
				if ((iX == 0 || iX == (this.sideLength - 1)) || (iZ == 0 || iZ == (this.sideLength - 1))) {
					EStrongholdRoomType type = this.roomGrid[iX][iZ];
					if (type != null && type != EStrongholdRoomType.NONE) {
						ResourceLocation loc = getTemplateForType(type, this.random);
						if (loc != null) {
							BlockPos pos = this.coordinateGrid[iX][iZ];
							BlockPos startPos = pos.offset(-7, 0, -7);
							// Create a valid bounding box for the room with chunk-safe padding
							BoundingBox box = new BoundingBox(startPos.getX() - 4, startPos.getY() - 4, startPos.getZ() - 4, startPos.getX() + 19, startPos.getY() + 32, startPos.getZ() + 19);
							this.builder.addPiece(new CQRTemplatePiece(loc, startPos, box, this.entityReplacement));
						}
					}
				}
			}
		}
	}

	public EStrongholdRoomType getExitRoomType() {
		return this.roomGrid[this.exitIndexX][this.exitIndexZ];
	}

	private ResourceLocation getTemplateForType(EStrongholdRoomType type, RandomSource random) {
		String basePath = this.templateBasePath;
		switch (type) {
			case BOSS:
				if (basePath.contains("volcano")) {
					return ResourceLocation.parse(basePath + (random.nextBoolean() ? "boss/volcano_boss_room1" : "boss/volcano_boss_room2"));
				}
				return ResourceLocation.parse(basePath + (random.nextBoolean() ? "boss/stronghold_boss_room_desert" : "boss/stronghold_boss_room_taiga"));
			case CURVE_NE: return ResourceLocation.parse(basePath + "curves/ne/stronghold_north_east_" + (1 + random.nextInt(3)));
			case CURVE_EN: return ResourceLocation.parse(basePath + "curves/en/stronghold_east_north_" + (1 + random.nextInt(4)));
			case CURVE_ES: return ResourceLocation.parse(basePath + "curves/es/stronghold_east_south_" + (1 + random.nextInt(3)));
			case CURVE_SE: return ResourceLocation.parse(basePath + "curves/se/stronghold_south_east_" + (1 + random.nextInt(3)));
			case CURVE_SW: return ResourceLocation.parse(basePath + "curves/sw/stronghold_south_west_" + (1 + random.nextInt(3)));
			case CURVE_WS: return ResourceLocation.parse(basePath + "curves/ws/stronghold_west_south_" + (1 + random.nextInt(4)));
			case CURVE_WN: return ResourceLocation.parse(basePath + "curves/wn/stronghold_west_north_" + (1 + random.nextInt(3)));
			case CURVE_NW: return ResourceLocation.parse(basePath + "curves/nw/stronghold_north_west_" + (1 + random.nextInt(4)));

			case HALLWAY_NS: return ResourceLocation.parse(basePath + "hallway/ns/stronghold_north_south_" + (1 + random.nextInt(4)));
			case HALLWAY_SN: return ResourceLocation.parse(basePath + "hallway/sn/stronghold_south_north_" + (1 + random.nextInt(4)));
			case HALLWAY_EW: return ResourceLocation.parse(basePath + "hallway/ew/stronghold_east_west_" + (1 + random.nextInt(4)));
			case HALLWAY_WE: return ResourceLocation.parse(basePath + "hallway/we/stronghold_west_east_" + (1 + random.nextInt(4)));

			case STAIR_NN: return ResourceLocation.parse(basePath + "stairs/n/stronghold_stairs_north");
			case STAIR_SS: return ResourceLocation.parse(basePath + "stairs/s/stronghold_stairs_south");
			case STAIR_EE: return ResourceLocation.parse(basePath + "stairs/e/stronghold_stairs_east");
			case STAIR_WW: return ResourceLocation.parse(basePath + "stairs/w/stronghold_stairs_west");
			default: return null;
		}
	}
}
