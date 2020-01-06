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
		this.roomGrid = new StrongholdRoom[roomCount * 2 + 2][roomCount * 2 + 2];
	}

	public StrongholdLinearGenerator getGenerator() {
		return this.generator;
	}

	// First room always is at [roomCount +2][roomCount +2]
	public void generateRoomPattern() {
		StrongholdRoom room = new StrongholdRoom(this);
		room.setGridIndex(this.roomCount - 1, this.roomCount - 1);
		this.rooms.add(room);
		this.roomsWithFreeNeighbors.add(room);
		this.roomGrid[room.getGridIndex().getFirst()][room.getGridIndex().getSecond()] = room;
		this.firstRoomIndexes = room.getGridIndex();
		this.lastRoomIndexes = this.firstRoomIndexes;
		Random rdm = new Random();
		for (int i = 0; i < this.roomCount; i++) {
			if (this.roomsWithFreeNeighbors.isEmpty()) {
				break;
			}
			StrongholdRoom prevRoom = this.getRandomUsableRoom(rdm);
			if (prevRoom != null) {
				ESkyDirection expansionDir = prevRoom.getRandomFreeDirection(rdm);
				prevRoom.connectRoomOnSide(expansionDir);
				Tuple<Integer, Integer> v = this.getSkyDirectionAsGridVector(expansionDir);
				int gridX = prevRoom.getGridIndex().getFirst() + v.getFirst();
				int gridZ = prevRoom.getGridIndex().getSecond() + v.getSecond();

				if (!prevRoom.hasFreeSides()) {
					this.roomsWithFreeNeighbors.remove(prevRoom);
				}

				StrongholdRoom newRoom = new StrongholdRoom(this);
				newRoom.connectRoomOnSide(expansionDir.getOpposite());
				newRoom.setGridIndex(gridX, gridZ);
				this.rooms.add(newRoom);
				this.roomsWithFreeNeighbors.add(newRoom);
				this.roomGrid[gridX][gridZ] = newRoom;
				this.lastRoomIndexes = newRoom.getGridIndex();

				this.setConnectionStateOnGridForCoordinates(gridX, gridZ);
			} else {
				break;
			}
		}
	}

	public void generateRooms(BlockPos upperFloorExitPos, boolean firstStairIsEntranceStair, boolean isLastFloor, PlacementSettings settings, World world, StrongholdLinearDungeon dungeon) {
		// Entrance stair
		StrongholdRoom entranceRoom = this.roomGrid[this.firstRoomIndexes.getFirst()][this.firstRoomIndexes.getSecond()];
		CQStructure stair = null;
		if (firstStairIsEntranceStair) {
			stair = new CQStructure(dungeon.getEntranceStairRoom(), dungeon, this.generator.getDunX(), this.generator.getDunZ(), dungeon.isProtectedFromModifications());
		} else {
			stair = new CQStructure(dungeon.getStairRoom(), dungeon, this.generator.getDunX(), this.generator.getDunZ(), dungeon.isProtectedFromModifications());
		}
		this.floorY = upperFloorExitPos.getY() - stair.getSizeY();
		entranceRoom.generateRoom(dungeon, upperFloorExitPos.subtract(new Vec3i(0, stair.getSizeY(), 0)), world, settings, stair, true);

		// Rest of the rooms
		for (int iX = 0; iX < (this.roomCount + 2); iX++) {
			for (int iZ = 0; iZ < (this.roomCount + 2); iZ++) {
				StrongholdRoom room = this.roomGrid[iX][iZ];
				if (room != null) {
					Vec3i v = new Vec3i(room.getGridIndex().getFirst() - this.firstRoomIndexes.getFirst(), 0, room.getGridIndex().getSecond() - this.firstRoomIndexes.getSecond());
					System.out.println("V: " + v.toString());
					BlockPos pos = new BlockPos(upperFloorExitPos.getX() + (dungeon.getRoomSizeX() * v.getX()), this.floorY, upperFloorExitPos.getZ() + (dungeon.getRoomSizeZ() * v.getZ()));
					System.out.println("Pos: " + pos.toString());
					// first room
					if (!(iX == this.firstRoomIndexes.getFirst() && iZ == this.firstRoomIndexes.getSecond())) {
						// Last room
						CQStructure struct = null;
						if (iX == this.lastRoomIndexes.getFirst() && iZ == this.lastRoomIndexes.getSecond()) {
							if (isLastFloor) {
								struct = new CQStructure(dungeon.getBossRoom(), dungeon, this.generator.getDunX(), this.generator.getDunZ(), dungeon.isProtectedFromModifications());
								room.generateRoom(dungeon, pos, world, settings, struct, true);
							}
						}
						// Normal rooms
						else {
							room.generateRoom(dungeon, pos, world, settings);
						}
					}
				}
			}
		}
	}

	public BlockPos getLastRoomPastePos(BlockPos upperFloorExitPos, StrongholdLinearDungeon dungeon) {
		Vec3i v = new Vec3i(this.lastRoomIndexes.getFirst() - this.firstRoomIndexes.getFirst(), 0, this.lastRoomIndexes.getSecond() - this.firstRoomIndexes.getSecond());
		BlockPos pos = new BlockPos(upperFloorExitPos.getX() + (dungeon.getRoomSizeX() * v.getX()), this.floorY, upperFloorExitPos.getZ() + (dungeon.getRoomSizeZ() * v.getZ()));
		return pos;
	}

	private void setConnectionStateOnGridForCoordinates(int gridX, int gridZ) {
		StrongholdRoom roomAtPos = this.roomGrid[gridX][gridZ];
		for (ESkyDirection direction : ESkyDirection.values()) {
			StrongholdRoom room = this.roomGrid[gridX + this.getSkyDirectionAsGridVector(direction).getFirst()][gridZ + this.getSkyDirectionAsGridVector(direction).getSecond()];
			if (room != null) {
				roomAtPos.connectRoomOnSide(direction);
				room.connectRoomOnSide(direction.getOpposite());
				if (!room.hasFreeSides()) {
					this.roomsWithFreeNeighbors.remove(room);
				}
			}
		}
	}

	private Tuple<Integer, Integer> getSkyDirectionAsGridVector(ESkyDirection expansionDir) {
		int f = 0;
		int s = 0;
		switch (expansionDir) {
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
		return new Tuple<>(f, s);
	}

	private StrongholdRoom getRandomUsableRoom(Random rdm) {
		if (!this.roomsWithFreeNeighbors.isEmpty()) {
			StrongholdRoom room = this.roomsWithFreeNeighbors.get(rdm.nextInt(this.roomsWithFreeNeighbors.size()));
			while (!room.hasFreeSides() && !this.roomsWithFreeNeighbors.isEmpty()) {
				this.roomsWithFreeNeighbors.remove(room);
				if (this.roomsWithFreeNeighbors.isEmpty()) {
					return null;
				}
				room = this.roomsWithFreeNeighbors.get(rdm.nextInt(this.roomsWithFreeNeighbors.size()));
			}
			if (this.roomsWithFreeNeighbors.isEmpty()) {
				return null;
			}
			return room;
		}
		return null;
	}

}
