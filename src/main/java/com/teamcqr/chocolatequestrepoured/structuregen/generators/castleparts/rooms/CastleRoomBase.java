package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.factories.GearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.CastleMainStructWall;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CastleRoomBase {
	protected BlockPos roomOrigin;
	protected BlockPos buildStartPos;
	protected int height;
	protected int sideLength;

	// The following variables are used for rooms that build blocks in a smaller area than the
	// actual room occupies (such as towers). For most room types they will be not be changed from
	// the values set in the default constructor.
	protected int roomLengthX; // actual length of blocks that the room occupies (not counting walls)
	protected int roomLengthZ; // actual length of blocks that the room occupies (not counting walls)
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
	protected Random random;

	protected HashSet<BlockPos> possibleDecoPositions; // set of possible decoration positions
	protected HashSet<BlockPos> usedDecoPositions; // set of decoration positions that have been added (subset of possible)
	protected HashMap<EnumFacing, CastleMainStructWall> walls = new HashMap<>();
	// protected HashSet<BlockPos> decoEdge; // set of all positions that are along the edge of the room (subset of possible)

	public CastleRoomBase(int sideLength, int height, int floor, Random rand) {
		this.sideLength = sideLength;
		this.offsetX = 0;
		this.offsetZ = 0;
		this.roomLengthX = this.sideLength;
		this.roomLengthZ = this.sideLength;
		this.height = height;
		this.floor = floor;
		this.random = rand;
		this.usedDecoPositions = new HashSet<>();
		this.possibleDecoPositions = new HashSet<>();
	}

	public void setRoomOrigin(BlockPos offset) {
		this.roomOrigin = offset;
	}

	public void generate(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		this.generateRoom(castleOrigin, genArray, dungeon);
		// this.generateWalls(genArray, dungeon);

		if (this.defaultFloor) {
			this.generateDefaultFloor(genArray, dungeon);
		}
		if (this.defaultCeiling) {
			this.generateDefaultCeiling(genArray, dungeon);
		}
	}

	public void postProcess(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		;
	}

	protected abstract void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon);

	public void decorate(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon, GearedMobFactory mobFactory) {
		; // Default is no decoration
	}

	public void placeBoss(World world, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon, ResourceLocation bossResourceLocation, ArrayList<String> bossUuids) {
		; // Default is no boss
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

	protected void generateDefaultCeiling(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				genArray.addBlockState(this.getInteriorBuildStart().add(x, (this.height - 1), z), dungeon.getMainBlockState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}
		}
	}

	protected void generateDefaultFloor(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		BlockPos pos = this.getNonWallStartPos();

		for (int z = 0; z < this.getDecorationLengthZ(); z++) {
			for (int x = 0; x < this.getDecorationLengthX(); x++) {
				genArray.addBlockState(pos.add(x, 0, z), this.getFloorBlock(dungeon), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
			}
		}
	}

	protected void fillEmptySpaceWithAir(BlockStateGenArray genArray) {
		HashSet<BlockPos> emptySpaces = new HashSet<>(this.possibleDecoPositions);
		emptySpaces.removeAll(this.usedDecoPositions);

		for (BlockPos emptyPos : emptySpaces) {
			genArray.addBlockState(emptyPos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
		}
	}

	protected IBlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
		return dungeon.getFloorBlockState();
	}

	public EnumRoomType getRoomType() {
		return this.roomType;
	}

	protected BlockPos getRotatedPlacement(int x, int y, int z, EnumFacing rotation) {
		switch (rotation) {
		case EAST:
			return this.roomOrigin.add(z, y, this.sideLength - 2 - x);
		case WEST:
			return this.roomOrigin.add(this.sideLength - 2 - z, y, x);
		case NORTH:
			return this.roomOrigin.add(this.sideLength - 2 - x, y, this.sideLength - 2 - z);
		case SOUTH:
		default:
			return this.roomOrigin.add(x, y, z);
		}
	}

	protected BlockPos getInteriorBuildStart() {
		return this.roomOrigin.add(this.offsetX, 0, this.offsetZ);
	}

	protected BlockPos getExteriorBuildStart() {
		return this.buildStartPos.add(this.offsetX, 0, this.offsetZ);
	}

	protected void setupDecoration(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		this.possibleDecoPositions = new HashSet<>(this.getDecorationArea());
		this.setDoorAndWindowAreasToAir(genArray, dungeon);
	}

	protected void setDoorAndWindowAreasToAir(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		BlockPos northStart = this.getDecorationStartPos();
		BlockPos westStart = this.getDecorationStartPos();
		BlockPos eastStart = this.getDecorationStartPos().add((this.getDecorationLengthX() - 1), 0, 0);
		BlockPos southStart = this.getDecorationStartPos().add(0, 0, (this.getDecorationLengthZ() - 1));

		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			CastleMainStructWall wall = this.walls.get(side);

			if (wall != null) {
				BlockPos decoPos;
				if (side.getAxis() == EnumFacing.Axis.Z) {
					for (int x = 0; x < this.getDecorationLengthX(); x++) {
						for (int y = 0; y < this.getDecorationLengthY(); y++) {
							final int offsetAlongWall = x + 1;
							final int offsetUpWall = (this.hasFloor()) ? y + 1 : y;
							if (wall.offsetIsDoorOrWindow(offsetAlongWall, offsetUpWall, dungeon)) {
								if (side == EnumFacing.NORTH) {
									decoPos = northStart.add(x, y, 0);
								} else {
									decoPos = southStart.add(x, y, 0);
								}
								this.usedDecoPositions.add(decoPos);
								genArray.addBlockState(decoPos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
							}
						}
					}
				} else {
					for (int z = 0; z < this.getDecorationLengthZ(); z++) {
						for (int y = 0; y < this.getDecorationLengthY(); y++) {
							final int offsetAlongWall = z + 1;
							final int offsetUpWall = (this.hasFloor()) ? y + 1 : y;
							if (wall.offsetIsDoorOrWindow(offsetAlongWall, offsetUpWall, dungeon)) {
								if (side == EnumFacing.WEST) {
									decoPos = westStart.add(0, y, z);
								} else {
									decoPos = eastStart.add(0, y, z);
								}
								this.usedDecoPositions.add(decoPos);
								genArray.addBlockState(decoPos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
							}
						}
					}
				}
			}

		}
	}

	/*
	 * Get a list of blocks that make up the decoratable edge of the room. Decoratable edge positions are adjacent to a wall but not in front of a door.
	 */
	protected ArrayList<BlockPos> getDecorationEdge(EnumFacing side) {
		// First get all blocks that are not occupied by walls
		ArrayList<BlockPos> result = this.getDecorationLayer(0);

		this.removeAllButEdge(result, side);
		result.removeIf(p -> this.usedDecoPositions.contains(p)); // Remove block if it is occupied already

		return result;
	}

	protected ArrayList<BlockPos> getDecorationMiddle() {
		ArrayList<BlockPos> result = this.getDecorationLayer(0);
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

		this.removeAllButEdge(result, side);
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
	 * Get a 1-height square of block positions that represents the lowest y position of a room that can be decorated. In other words, the layer just above the
	 * floor that is not already occupied by walls.
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
		return this.roomOrigin.add(this.offsetX, 0, this.offsetZ);
	}

	protected int getDecorationLengthX() {
		int length = this.roomLengthX;
		if (this.walls.containsKey(EnumFacing.EAST)) {
			if (!this.walls.get(EnumFacing.EAST).isEnabled()) {
				++length; // No wall there so this room should extend into that block
			}
		}
		return length;
	}

	protected int getDecorationLengthZ() {
		int length = this.roomLengthZ;
		if (this.walls.containsKey(EnumFacing.SOUTH)) {
			if (!this.walls.get(EnumFacing.SOUTH).isEnabled()) {
				++length; // No wall there so this room should extend into that block
			}
		}
		return length;
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

	public ResourceLocation[] getChestIDs() {
		return new ResourceLocation[0];
	}

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetZ() {
		return this.offsetZ;
	}

	public int getRoomLengthX() {
		return this.roomLengthX;
	}

	public int getRoomLengthZ() {
		return this.roomLengthZ;
	}

	public boolean isWalkableRoof() {
		return (this.roomType == EnumRoomType.WALKABLE_ROOF || this.roomType == EnumRoomType.WALKABLE_TOWER_ROOF);
	}

	public boolean isReplacedRoof() {
		return this.roomType == EnumRoomType.REPLACED_ROOF;
	}

	public boolean isBossLanding() {
		return (this.roomType == EnumRoomType.LANDING_BOSS);
	}

	public void setRoomsInBlock(ArrayList<CastleRoomBase> rooms) {
		this.roomsInBlock = rooms;
		this.makeRoomBlockAdjustments();
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

	public void copyPropertiesOf(CastleRoomBase room) {
		;
	}

	public void registerWalls(HashMap<EnumFacing, CastleMainStructWall> walls) {
		this.walls = walls;
	}
}
