package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.GearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.EnumCastleDoorType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWalls;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public abstract class CastleRoomBase {
	protected BlockPos origin;
	protected BlockPos buildStartPos;
	protected int height;
	protected int sideLength;

	// The following variables are used for rooms that build blocks in a smaller area than the
	// actual room occupies (such as towers). For most room types they will be not be changed from
	// the values set in the default constructor.
	protected int buildLengthX; // actual length of constructed part of room
	protected int buildLengthZ; // actual length of constructed part of room
	protected int offsetX; // x offset from origin that actual room starts
	protected int offsetZ; // z offset from origin that actual room starts
	protected int floor;

	protected int maxSlotsUsed = 1; // Max number of contiguous room grid slots this can occupy
	protected boolean isRootRoomInBlock = false;
	protected ArrayList<CastleRoomBase> roomsInBlock = new ArrayList<>();

	protected boolean isTower = false;
	protected boolean pathable = true;

	protected EnumRoomType roomType = EnumRoomType.NONE;
	protected boolean defaultCeiling = false;
	protected boolean defaultFloor = false;
	protected Random random = new Random();

	protected RoomWalls walls; // the walls of this room
	protected HashSet<EnumFacing> adjacentWalls; // track which adjacent rooms have walls
	protected HashSet<BlockPos> possibleDecoPositions; // set of possible decoration positions
	protected HashSet<BlockPos> usedDecoPositions; // set of decoration positions that have been added (subset of possible)
	//protected HashSet<BlockPos> decoEdge; // set of all positions that are along the edge of the room (subset of possible)

	public CastleRoomBase(BlockPos startOffset, int sideLength, int height, int floor) {
		this.origin = new BlockPos(startOffset);
		this.buildStartPos = new BlockPos(startOffset);
		this.sideLength = sideLength;
		this.offsetX = 0;
		this.offsetZ = 0;
		this.buildLengthX = this.sideLength;
		this.buildLengthZ = this.sideLength;
		this.height = height;
		this.floor = floor;
		this.walls = new RoomWalls();
		this.adjacentWalls = new HashSet<>();
		this.usedDecoPositions = new HashSet<>();
		this.possibleDecoPositions = new HashSet<>();
	}

	public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
		this.setupDecoration(genArray);
		this.generateRoom(genArray, dungeon);
		this.generateWalls(genArray, dungeon);

		if (this.defaultFloor) {
			this.generateDefaultFloor(genArray, dungeon);
		}
		if (this.defaultCeiling) {
			this.generateDefaultCeiling(genArray, dungeon);
		}
	}

	public void postProcess(BlockStateGenArray genArray, DungeonCastle dungeon) {
		;
	}

	protected abstract void generateRoom(BlockStateGenArray genArray, DungeonCastle dungeon);

	public void decorate(World world, BlockStateGenArray genArray, DungeonCastle dungeon, GearedMobFactory mobFactory) {
		; // Default is no decoration
	}

	public void placeBoss(World world, BlockStateGenArray genArray, DungeonCastle dungeon, ResourceLocation bossResourceLocation, ArrayList<String> bossUuids) {
		; // Default is no boss
	}

	protected void generateWalls(BlockStateGenArray genArray, DungeonCastle dungeon) {
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			if (this.walls.hasWallOnSide(side)) {
				int wallLength = (side.getAxis() == EnumFacing.Axis.X) ? this.buildLengthZ : this.buildLengthX;
				BlockPos wallStart;
				if (side == EnumFacing.EAST) {
					wallStart = this.getExteriorBuildStart().offset(EnumFacing.EAST, this.buildLengthX - 1);
				} else if (side == EnumFacing.SOUTH) {
					wallStart = this.getExteriorBuildStart().offset(EnumFacing.SOUTH, this.buildLengthZ - 1);
				} else {
					wallStart = new BlockPos(this.getExteriorBuildStart());
				}

				this.createAndGenerateWallBuilder(genArray, dungeon, side, wallLength, wallStart);
			}
		}
	}

	protected void createAndGenerateWallBuilder(BlockStateGenArray genArray, DungeonCastle dungeon, EnumFacing side, int wallLength, BlockPos wallStart) {
		RoomWallBuilder builder = new RoomWallBuilder(wallStart, this.height, wallLength, this.walls.getOptionsForSide(side), side);
		builder.generate(genArray, dungeon);
	}

	public boolean canBuildDoorOnSide(EnumFacing side) {
		return true;
	}

	public boolean canBuildInnerWallOnSide(EnumFacing side) {
		return true;
	}

	public boolean reachableFromSide(EnumFacing side) {
		return true;
	}

	public boolean isTower() {
		return this.roomType.isTowerRoom();
	}

	public boolean isStairsOrLanding() {
		return this.roomType.isStairRoom();
	}

	public boolean isPathable() {
		return this.roomType.isPathable();
	}

	protected void generateDefaultCeiling(BlockStateGenArray genArray, DungeonCastle dungeon) {
		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				genArray.addBlockState(this.getInteriorBuildStart().add(x, (this.height - 1), z), dungeon.getMainBlockState(), BlockStateGenArray.GenerationPhase.MAIN);
			}
		}
	}

	protected void generateDefaultFloor(BlockStateGenArray genArray, DungeonCastle dungeon) {
		BlockPos pos = this.getNonWallStartPos();

		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				genArray.addBlockState(pos.add(x, 0, z), this.getFloorBlock(dungeon), BlockStateGenArray.GenerationPhase.MAIN);
			}
		}
	}

    protected void fillEmptySpaceWithAir(BlockStateGenArray genArray) {
        HashSet<BlockPos> emptySpaces = new HashSet<>(this.possibleDecoPositions);
        emptySpaces.removeAll(this.usedDecoPositions);

        for (BlockPos emptyPos : emptySpaces) {
            genArray.addBlockState(emptyPos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
        }
    }

	protected IBlockState getFloorBlock(DungeonCastle dungeon) {
		return dungeon.getFloorBlockState();
	}

	public EnumRoomType getRoomType() {
		return this.roomType;
	}

	protected BlockPos getRotatedPlacement(int x, int y, int z, EnumFacing rotation) {
		switch (rotation) {
		case EAST:
			return this.origin.add(z, y, this.sideLength - 2 - x);
		case WEST:
			return this.origin.add(this.sideLength - 2 - z, y, x);
		case NORTH:
			return this.origin.add(this.sideLength - 2 - x, y, this.sideLength - 2 - z);
		case SOUTH:
		default:
			return this.origin.add(x, y, z);
		}
	}

	protected BlockPos getInteriorBuildStart() {
		return this.origin.add(this.offsetX, 0, this.offsetZ);
	}

	protected BlockPos getExteriorBuildStart() {
		return this.buildStartPos.add(this.offsetX, 0, this.offsetZ);
	}

	public boolean hasWallOnSide(EnumFacing side) {
		return this.walls.hasWallOnSide(side);
	}

	protected boolean adjacentRoomHasWall(EnumFacing side) {
		return this.adjacentWalls.contains(side);
	}

	public boolean hasDoorOnSide(EnumFacing side) {
		return this.walls.hasDoorOnSide(side);
	}

	public DoorPlacement addDoorOnSideCentered(EnumFacing side) {
		int sideLength = (side.getAxis() == EnumFacing.Axis.X) ? this.buildLengthZ : this.buildLengthX;
		return this.walls.addCenteredDoor(this.random, sideLength, side, EnumCastleDoorType.RANDOM);
	}

	public DoorPlacement addDoorOnSideRandom(Random random, EnumFacing side) {
		int sideLength = (side.getAxis() == EnumFacing.Axis.X) ? this.buildLengthZ : this.buildLengthX;
		return this.walls.addRandomDoor(random, sideLength, side, EnumCastleDoorType.RANDOM);
	}

	public DoorPlacement addGrandEntrance(EnumFacing side) {
		int sideLength = (side.getAxis() == EnumFacing.Axis.X) ? this.buildLengthZ : this.buildLengthX;
		return this.walls.addCenteredDoor(this.random, sideLength, side, EnumCastleDoorType.GRAND_ENTRY);
	}

	public void addOuterWall(EnumFacing side) {
		if (!this.walls.hasWallOnSide(side)) {
			this.walls.addOuter(side);

			if (side == EnumFacing.NORTH) {
				this.buildStartPos = this.buildStartPos.north();
				++this.buildLengthZ;
			} else if (side == EnumFacing.WEST) {
				this.buildStartPos = this.buildStartPos.west();
				++this.buildLengthX;
			}
		}
	}

	public void addInnerWall(EnumFacing side) {
		if (!this.walls.hasWallOnSide(side)) {
			this.walls.addInner(side);
		}
	}

	public void removeWall(EnumFacing side) {
		this.walls.removeWall(side);
	}

	public void registerAdjacentRoomDoor(EnumFacing side, DoorPlacement door) {
		this.walls.registerAdjacentDoor(side, door);
	}

	public void registerAdjacentRoomWall(EnumFacing side) {
		this.adjacentWalls.add(side);
	}

	protected void setupDecoration(BlockStateGenArray genArray) {
		this.possibleDecoPositions = new HashSet<>(this.getDecorationArea());
		this.setDoorAreasToAir(genArray);
	}

	protected void setDoorAreasToAir(BlockStateGenArray genArray) {
		BlockPos toAdd;
		BlockPos topLeft = this.getDecorationStartPos();
		int xStart = topLeft.getX();
		int zStart = topLeft.getZ();
		int xEnd = xStart + (this.getDecorationLengthX() - 1);
		int zEnd = zStart + (this.getDecorationLengthZ() - 1);
		int yStart = topLeft.getY();

		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			DoorPlacement placement = null;

			if (this.walls.hasDoorOnSide(side)) {
				placement = this.walls.getDoorOnSide(side);
			} else if (this.walls.adjacentRoomHasDoorOnSide(side)) {
				placement = this.walls.getAdjacentDoor(side);
			}

			if (placement != null) {
				final int doorStart;
				final int doorEnd;
				final int yEnd = yStart + placement.getHeight() - 1;

				if (side.getAxis() == EnumFacing.Axis.Z) {
					doorStart = this.origin.getX() + placement.getOffset();
					doorEnd = doorStart + placement.getWidth() - 1;

					int z;
					if (side == EnumFacing.NORTH) {
						z = zStart;
					} else // SOUTH
					{
						z = zEnd;
					}
					for (int x = doorStart; x <= doorEnd; x++) {
						for (int y = yStart; y < yEnd; y++) {
							toAdd = new BlockPos(x, y, z);
							this.usedDecoPositions.add(toAdd);
						}
					}
				} else {
					doorStart = this.origin.getZ() + placement.getOffset();
					doorEnd = doorStart + placement.getWidth() - 1;

					int x;
					if (side == EnumFacing.WEST) {
						x = xStart;
					} else // SOUTH
					{
						x = xEnd;
					}
					for (int z = doorStart; z <= doorEnd; z++) {
						for (int y = yStart; y < yEnd; y++) {
							toAdd = new BlockPos(x, y, z);
							this.usedDecoPositions.add(toAdd);
						}
					}
				}
			}
		}
	}

	/*
	 * Get a list of blocks that make up the decoratable edge of the room.
	 * Decoratable edge positions are adjacent to a wall but not in front of a door.
	 */
	protected ArrayList<BlockPos> getDecorationEdge(EnumFacing side) {
		// First get all blocks that are not occupied by walls
		ArrayList<BlockPos> result = this.getDecorationLayer(0);

		removeAllButEdge(result, side);
		result.removeIf(p -> this.usedDecoPositions.contains(p)); // Remove block if it is occupied already

		return result;
	}

	protected ArrayList<BlockPos> getDecorationMiddle() {
		ArrayList<BlockPos> result = getDecorationLayer(0);
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			result.removeAll(this.getDecorationEdge(side));
		}

		return result;
	}

	/*
	 * Get a list of blocks that make up the ring of potential painting locations
	 */
	protected ArrayList<BlockPos> getWallDecorationEdge(EnumFacing side) {
		// First get all blocks that are not occupied by walls
		ArrayList<BlockPos> result = this.getDecorationLayer(2);

		removeAllButEdge(result, side);
		result.removeIf(p -> this.usedDecoPositions.contains(p)); // Remove block if it is occupied already

		return result;
	}

	private void removeAllButEdge(ArrayList<BlockPos> blockList, EnumFacing side) {
		BlockPos topLeft = this.getDecorationStartPos();
		final int xStart = topLeft.getX();
		final int zStart = topLeft.getZ();
		final int xEnd = xStart + (this.getDecorationLengthX() - 1);
		final int zEnd = zStart + (this.getDecorationLengthZ() - 1);

		if (side == EnumFacing.NORTH) {
			blockList.removeIf(p -> p.getZ() != zStart);
			blockList.sort(Comparator.comparingInt(BlockPos::getX));
		} else if (side == EnumFacing.SOUTH) {
			blockList.removeIf(p -> p.getZ() != zEnd);
			blockList.sort(Comparator.comparingInt(BlockPos::getX).reversed());
		} else if (side == EnumFacing.WEST) {
			blockList.removeIf(p -> p.getX() != xStart);
			blockList.sort(Comparator.comparingInt(BlockPos::getZ));
		} else if (side == EnumFacing.EAST) {
			blockList.removeIf(p -> p.getX() != xEnd);
			blockList.sort(Comparator.comparingInt(BlockPos::getZ).reversed());
		}
	}

	/*
	 * Get a 1-height square of block positions that represents the lowest y position
	 * of a room that can be decorated. In other words, the layer just above the floor
	 * that is not already occupied by walls.
	 */
	protected ArrayList<BlockPos> getDecorationLayer(int layer) {
		ArrayList<BlockPos> result = this.getDecorationArea();

		if (!result.isEmpty()) {
			BlockPos lowerUpperLeft = result.get(0);
			int y = lowerUpperLeft.getY() + layer;
			result.removeIf(p -> p.getY() != y);
		}

		return result;
	}

	protected ArrayList<BlockPos> getDecorationArea() {
		ArrayList<BlockPos> result = new ArrayList<>();
		BlockPos start = this.getDecorationStartPos();

		for (int x = 0; x < this.getDecorationLengthX(); x++) {
			for (int y = 0; y < this.getDecorationLengthY(); y++) {
				for (int z = 0; z < this.getDecorationLengthZ(); z++) {
					result.add(start.add(x, y, z));
				}
			}
		}

		return result;
	}

	protected BlockPos getDecorationStartPos() {
		if (this.hasFloor()) {
			return this.getNonWallStartPos().up(); // skip the floor
		} else {
			return this.getNonWallStartPos();
		}
	}

	protected boolean hasFloor() {
		return true;
	}

	protected BlockPos getNonWallStartPos() {
		return this.origin.add(this.offsetX, 0, this.offsetZ);
	}

	protected int getDecorationLengthX() {
		int result = this.buildLengthX;

		if (this.walls.hasWallOnSide(EnumFacing.WEST)) {
			--result;
		}
		if (this.walls.hasWallOnSide(EnumFacing.EAST)) {
			--result;
		}

		return result;
	}

	protected int getDecorationLengthZ() {
		int result = this.buildLengthZ;

		if (this.walls.hasWallOnSide(EnumFacing.NORTH)) {
			--result;
		}
		if (this.walls.hasWallOnSide(EnumFacing.SOUTH)) {
			--result;
		}

		return result;
	}

	public int getDecorationLengthY() {
		int result = this.height;

		if (this.defaultCeiling) {
			--result;
		}
		if (this.hasFloor()) {
			--result;
		}

		return result;
	}

	public int[] getChestIDs() {
		return null;
	}

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetZ() {
		return this.offsetZ;
	}

	public int getBuildLengthX() {
		return this.buildLengthX;
	}

	public int getBuildLengthZ() {
		return this.buildLengthZ;
	}

	public boolean isWalkableRoof() {
		return (this.roomType == EnumRoomType.WALKABLE_ROOF || this.roomType == EnumRoomType.WALKABLE_TOWER_ROOF);
	}

	public void setRoomsInBlock(ArrayList<CastleRoomBase> rooms) {
		this.roomsInBlock = rooms;
		makeRoomBlockAdjustments();
	}

	protected void makeRoomBlockAdjustments() {
		;
	}

	public void setAsRootRoom() {
		this.isRootRoomInBlock = true;
	}

	@Override
	public String toString() {
		return this.roomType.toString();
	}
}
