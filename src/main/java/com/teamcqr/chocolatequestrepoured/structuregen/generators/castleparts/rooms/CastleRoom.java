package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.EnumCastleDoorType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWalls;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CastleRoom {
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

	protected int maxSlotsUsed = 1; // Max number of contiguous room grid slots this can occupy

	protected boolean isTower = false;
	protected boolean pathable = true;

	protected EnumRoomType roomType = EnumRoomType.NONE;
	protected boolean defaultCeiling = false;
	protected boolean defaultFloor = false;
	protected Random random = new Random();

	protected RoomWalls walls; // the walls of this room
	protected HashSet<EnumFacing> adjacentWalls; // track which adjacent rooms have walls
	protected HashSet<BlockPos> decoMap;
	protected HashSet<BlockPos> decoArea;

	public CastleRoom(BlockPos startPos, int sideLength, int height) {
		this.origin = new BlockPos(startPos);
		this.buildStartPos = new BlockPos(startPos);
		this.sideLength = sideLength;
		this.offsetX = 0;
		this.offsetZ = 0;
		this.buildLengthX = this.sideLength;
		this.buildLengthZ = this.sideLength;
		this.height = height;
		this.walls = new RoomWalls();
		this.adjacentWalls = new HashSet<>();
		this.decoMap = new HashSet<>();
		this.decoArea = new HashSet<>();
	}

	public void generate(World world, CastleDungeon dungeon) {
		this.generateRoom(world, dungeon);
		this.generateWalls(world, dungeon);

		if (this.defaultFloor) {
			this.generateDefaultFloor(world, dungeon);
		}
		if (this.defaultCeiling) {
			this.generateDefaultCeiling(world, dungeon);
		}
	}

	protected abstract void generateRoom(World world, CastleDungeon dungeon);

	public void decorate(World world, CastleDungeon dungeon) {
		; // Default is no decoration
	}

	protected void generateWalls(World world, CastleDungeon dungeon) {
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

				this.createAndGenerateWallBuilder(world, dungeon, side, wallLength, wallStart);
			}
		}
	}

	protected void createAndGenerateWallBuilder(World world, CastleDungeon dungeon, EnumFacing side, int wallLength, BlockPos wallStart) {
		RoomWallBuilder builder = new RoomWallBuilder(wallStart, this.height, wallLength, this.walls.getOptionsForSide(side), side);
		builder.generate(world, dungeon);
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

	protected void generateDefaultCeiling(World world, CastleDungeon dungeon) {
		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				world.setBlockState(this.getInteriorBuildStart().add(x, (this.height - 1), z), dungeon.getWallBlock().getDefaultState());
			}
		}
	}

	protected void generateDefaultFloor(World world, CastleDungeon dungeon) {
		BlockPos pos = this.getNonWallStartPos();

		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				world.setBlockState(pos.add(x, 0, z), this.getFloorBlock(dungeon));
			}
		}
	}

	protected IBlockState getFloorBlock(CastleDungeon dungeon) {
		return dungeon.getFloorBlock().getDefaultState();
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

	protected int getNumYRotationsFromStartToEndFacing(EnumFacing start, EnumFacing end) {
		int rotations = 0;
		if (start.getAxis().isHorizontal() && end.getAxis().isHorizontal()) {
			while (start != end) {
				start = start.rotateY();
				rotations++;
			}
		}
		return rotations;
	}

	protected EnumFacing rotateFacingNTimesAboutY(EnumFacing facing, int n) {
		for (int i = 0; i < n; i++) {
			facing = facing.rotateY();
		}
		return facing;
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

	protected void setupDecoration(World world) {
		this.decoArea = new HashSet<>(this.getDecorationArea());
		this.setDoorAreasToAir(world);
	}

	protected void setDoorAreasToAir(World world) {
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
							world.setBlockState(toAdd, Blocks.AIR.getDefaultState());
							this.decoMap.add(toAdd);
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
							world.setBlockState(toAdd, Blocks.AIR.getDefaultState());
							this.decoMap.add(toAdd);
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
		ArrayList<BlockPos> result = this.getDecorationFirstLayer();

		BlockPos topLeft = this.getDecorationStartPos();
		final int xStart = topLeft.getX();
		final int zStart = topLeft.getZ();
		final int xEnd = xStart + (this.getDecorationLengthX() - 1);
		final int zEnd = zStart + (this.getDecorationLengthZ() - 1);

		if (side == EnumFacing.NORTH) {
			result.removeIf(p -> p.getZ() != zStart);
			result.sort(Comparator.comparingInt(BlockPos::getX));
		} else if (side == EnumFacing.SOUTH) {
			result.removeIf(p -> p.getZ() != zEnd);
			result.sort(Comparator.comparingInt(BlockPos::getX).reversed());
		} else if (side == EnumFacing.WEST) {
			result.removeIf(p -> p.getX() != xStart);
			result.sort(Comparator.comparingInt(BlockPos::getZ));
		} else if (side == EnumFacing.EAST) {
			result.removeIf(p -> p.getX() != xEnd);
			result.sort(Comparator.comparingInt(BlockPos::getZ).reversed());
		}

		result.removeIf(p -> this.decoMap.contains(p)); // Remove block if it is occupied already

		return result;
	}

	/*
	 * Get a 1-height square of block positions that represents the lowest y position
	 * of a room that can be decorated. In other words, the layer just above the floor
	 * that is not already occupied by walls.
	 */
	protected ArrayList<BlockPos> getDecorationFirstLayer() {
		ArrayList<BlockPos> result = this.getDecorationArea();

		if (!result.isEmpty()) {
			BlockPos lowerUpperLeft = result.get(0);
			result.removeIf(p -> p.getY() != lowerUpperLeft.getY());
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
		if (this.defaultFloor) {
			return this.getNonWallStartPos().up(); // skip the floor
		} else {
			return this.getNonWallStartPos();
		}
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

	protected int getDecorationLengthY() {
		int result = this.height; // Remove one for the floor tiles

		if (this.defaultFloor) {
			--result;
		}
		if (this.defaultCeiling) {
			--result;
		}

		return result;
	}

	public int[] getChestIDs() {
		return null;
	}

	protected int getSpawnerCount() {
		return 2;
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

	@Override
	public String toString() {
		return this.roomType.toString();
	}
}
