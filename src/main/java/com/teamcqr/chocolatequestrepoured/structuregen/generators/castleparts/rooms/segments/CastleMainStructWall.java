package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RoomGridCell;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBossLandingMain;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import scala.Int;

public class CastleMainStructWall {
	public enum WallOrientation {
		HORIZONTAL, VERTICAL
	}

	private final int length;
	private final int height;
	private RandomCastleConfigOptions.WindowType windowType = RandomCastleConfigOptions.WindowType.BASIC_GLASS;
	private boolean enabled = false;
	private boolean isOuterWall = false;
	private boolean isRoofEdge = false;
	private int doorStartOffset = 0;
	private EnumCastleDoorType doorType = EnumCastleDoorType.NONE;
	private BlockPos origin;
	private WallOrientation orientation;
	private HashMap<EnumFacing, RoomGridCell> adjacentCells = new HashMap<>();

	public CastleMainStructWall(BlockPos origin, WallOrientation orientation, int length, int height) {
		this.origin = origin;
		this.orientation = orientation;
		this.length = length;
		this.height = height;
	}

	public void registerAdjacentCell(RoomGridCell cell, EnumFacing directionOfCell) {
		this.adjacentCells.put(directionOfCell, cell);
	}

	public Optional<RoomGridCell> getAdjacentCell(EnumFacing direction) {
		if (this.adjacentCells.containsKey(direction)) {
			return Optional.of(this.adjacentCells.get(direction));
		} else {
			return Optional.empty();
		}
	}

	public BlockPos getOrigin() {
		return this.origin;
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}

	public void setAsOuterWall() {
		this.isOuterWall = true;
	}

	public void setAsInnerWall() {
		this.isOuterWall = false;
	}

	public void setAsRoofEdge() {
		this.isRoofEdge = true;
	}

	public void setAsNormalWall() {
		this.isRoofEdge = false;
	}

	public boolean hasDoor() {
		return (this.doorType != EnumCastleDoorType.NONE);
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isVertical() {
		return this.orientation == WallOrientation.VERTICAL;
	}

	public boolean isHorizontal() {
		return this.orientation == WallOrientation.HORIZONTAL;
	}

	public int getGenerationPriority() {
		if (this.enabled) {
			if (this.isRoofEdge) {
				// Roof edges have low priority so they don't replace regular walls with air
				return 3;
			} else if (this.isOuterWall) {
				// Outer walls should go first since we want them all to be uniform
				return 1;
			} else {
				// Everything else (inner walls)
				return 2;
			}
		}
		return Int.MaxValue();
	}

	public void determineIfEnabled() {
		EnumFacing checkSide1;
		EnumFacing checkSide2;

		if (this.orientation == WallOrientation.HORIZONTAL) {
			checkSide1 = EnumFacing.NORTH;
			checkSide2 = EnumFacing.SOUTH;
		} else {
			checkSide1 = EnumFacing.WEST;
			checkSide2 = EnumFacing.EAST;
		}

		Optional<RoomGridCell> neighbor1 = this.getAdjacentCell(checkSide1);
		Optional<RoomGridCell> neighbor2 = this.getAdjacentCell(checkSide2);

		boolean neighbor1Populated = false;
		boolean neighbor1IsWalkableRoof = false;
		boolean neighbor1IsNormalRoof = false;
		boolean neighbor1IsPreBoss = false;
		boolean neighbor1IsBoss = false;

		boolean neighbor2Populated = false;
		boolean neighbor2IsWalkableRoof = false;
		boolean neighbor2IsNormalRoof = false;
		boolean neighbor2IsPreBoss = false;
		boolean neighbor2IsBoss = false;

		if (neighbor1.isPresent()) {
			neighbor1Populated = neighbor1.get().isPopulated();
			if (neighbor1Populated) {
				neighbor1IsWalkableRoof = neighbor1.get().getRoom().isWalkableRoof();
				neighbor1IsNormalRoof = neighbor1.get().getRoom().isReplacedRoof();
				neighbor1IsBoss = neighbor1.get().isBossArea();
				neighbor1IsPreBoss = neighbor1.get().getRoom().isBossLanding();
			}
		}

		if (neighbor2.isPresent()) {
			neighbor2Populated = neighbor2.get().isPopulated();
			if (neighbor2Populated) {
				neighbor2IsWalkableRoof = neighbor2.get().getRoom().isWalkableRoof();
				neighbor2IsNormalRoof = neighbor2.get().getRoom().isReplacedRoof();
				neighbor2IsBoss = neighbor2.get().isBossArea();
				neighbor2IsPreBoss = neighbor2.get().getRoom().isBossLanding();
			}
		}

		if (neighbor1IsNormalRoof && (neighbor2IsWalkableRoof || neighbor2IsBoss || !neighbor2Populated || neighbor2IsNormalRoof)) {
			this.disable();
		} else if (neighbor2IsNormalRoof && (neighbor1IsWalkableRoof || neighbor1IsBoss || !neighbor1Populated || neighbor1IsNormalRoof)) {
			this.disable();
		} else if (neighbor1IsBoss || neighbor2IsBoss) {
			if (neighbor1IsBoss && neighbor2IsPreBoss) {
				this.enable();
				this.setAsInnerWall();
				if (neighbor2.get().getRoom() instanceof CastleRoomBossLandingMain) {
					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_1, new Random());
				} else {
					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_2, new Random());
				}
			} else if (neighbor2IsBoss && neighbor1IsPreBoss) {
				this.enable();
				this.setAsInnerWall();
				if (neighbor1.get().getRoom() instanceof CastleRoomBossLandingMain) {
					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_1, new Random());
				} else {
					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_2, new Random());
				}
			} else {
				this.disable();
			}
		} else if (neighbor1Populated && neighbor2Populated) {
			if (neighbor1.get().isConnectedToCell(neighbor2.get())) {
				// if rooms are connected then there should be no wall between them
				this.disable();
			} else if (neighbor1IsWalkableRoof && neighbor2IsWalkableRoof) {
				// no walls between roof tiles either
				this.disable();
			} else {
				this.enable();
				this.setAsInnerWall();
			}
		} else if (neighbor1Populated || neighbor2Populated) {
			this.enable();
			this.setAsOuterWall();
			if (neighbor1IsWalkableRoof || neighbor2IsWalkableRoof) {
				this.setAsRoofEdge();
			}
		} else {
			this.disable();
		}
	}

	public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
		BlockPos pos;
		IBlockState blockToBuild;

		EnumFacing iterDirection;
		this.windowType = dungeon.getRandomWindowType();

		if (this.orientation == WallOrientation.VERTICAL) {
			iterDirection = EnumFacing.SOUTH;
		} else {
			iterDirection = EnumFacing.EAST;
		}

		for (int i = 0; i < this.length; i++) {
			for (int y = 0; y < this.height; y++) {
				pos = this.origin.offset(iterDirection, i).offset(EnumFacing.UP, y);
				blockToBuild = this.getBlockToBuild(pos, dungeon);
				BlockStateGenArray.EnumPriority priority = BlockStateGenArray.EnumPriority.MEDIUM;

				if (blockToBuild.getBlock() == Blocks.AIR) {
					priority = BlockStateGenArray.EnumPriority.LOWEST;
				}
				genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, priority);
			}
		}
	}

	protected IBlockState getBlockToBuild(BlockPos pos, DungeonCastle dungeon) {
		if (this.isRoofEdge) {
			return this.getRoofEdgeBlock(pos, dungeon);
		} else if (this.hasDoor()) {
			return this.getDoorBlock(pos, dungeon);
		} else if (this.isOuterWall) {
			return this.getWindowBlock(pos, dungeon);
		} else {
			return dungeon.getMainBlockState();
		}
	}

	public boolean offsetIsDoorOrWindow(int distAlongWall, int heightOnWall, DungeonCastle dungeon) {
		// Determine the relative offset within the wall given the distance and height
		int xDist = (this.orientation == WallOrientation.HORIZONTAL) ? distAlongWall : 0;
		int zDist = (this.orientation == WallOrientation.VERTICAL) ? distAlongWall : 0;
		BlockPos wallPosition = this.origin.add(xDist, heightOnWall, zDist);

		// Consider this a door/window block if it is anything other then a regular castle block
		return (this.getBlockToBuild(wallPosition, dungeon)) != dungeon.getMainBlockState();
	}

	private IBlockState getRoofEdgeBlock(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);

		if ((y == 0) || ((y == 1) && ((dist == this.length - 1) || (dist % 2 == 0)))) {
			return dungeon.getMainBlockState();
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	private IBlockState getBlockDoorBossHalf1(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);

		if (dist > 2) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y < this.height - 1) {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorBossHalf2(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);

		if (dist < (this.length - 3)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y < this.height - 1) {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
		}

		return blockToBuild;
	}

	protected IBlockState getDoorBlock(BlockPos pos, DungeonCastle dungeon) {
		switch (this.doorType) {
		case AIR:
			return this.getBlockDoorAir(pos, dungeon);

		case BOSS_HALF_1:
			return this.getBlockDoorBossHalf1(pos, dungeon);

		case BOSS_HALF_2:
			return this.getBlockDoorBossHalf2(pos, dungeon);

		case STANDARD:
			return this.getBlockDoorStandard(pos, dungeon);

		case FENCE_BORDER:
			return this.getBlockDoorFenceBorder(pos, dungeon);

		case STAIR_BORDER:
			return this.getBlockDoorStairBorder(pos, dungeon);

		case GRAND_ENTRY:
			return this.getBlockGrandEntry(pos, dungeon);

		default:
			return dungeon.getMainBlockState();
		}
	}

	private IBlockState getBlockDoorAir(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y < this.doorType.getHeight()) {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorStairBorder(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.origin.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y >= 1 && y <= 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = dungeon.getSlabBlockState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
				}
			} else if (dist == halfPoint + 1 || dist == halfPoint - 2) {
				EnumFacing stairFacing;

				if (this.orientation == WallOrientation.HORIZONTAL) {
					stairFacing = (dist == halfPoint - 2) ? EnumFacing.WEST : EnumFacing.EAST;
				} else {
					stairFacing = (dist == halfPoint - 2) ? EnumFacing.NORTH : EnumFacing.SOUTH;
				}

				IBlockState stairBase = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);

				if (y == 1) {
					blockToBuild = stairBase;
				} else if (y == 2 || y == 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = stairBase.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
				}
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorFenceBorder(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.origin.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y == 1 || y == 2) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 3) {
					blockToBuild = dungeon.getFenceBlockState();
				}
			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
				blockToBuild = dungeon.getFenceBlockState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockDoorStandard(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.origin.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getFloorBlockState();
			} else if ((dist == halfPoint || dist == halfPoint - 1)) {
				if (y == 1 || y == 2) {
					BlockDoor.EnumDoorHalf half = (y == 1) ? BlockDoor.EnumDoorHalf.LOWER : BlockDoor.EnumDoorHalf.UPPER;

					BlockDoor.EnumHingePosition hinge;
					if (this.orientation == WallOrientation.HORIZONTAL) {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT;
					} else {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT;
					}
					EnumFacing facing = (this.orientation == WallOrientation.HORIZONTAL) ? EnumFacing.NORTH : EnumFacing.WEST;

					blockToBuild = dungeon.getDoorBlockState().withProperty(BlockDoor.HALF, half).withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, hinge);
				} else if (y == 3) {
					blockToBuild = dungeon.getPlankBlockState();
				}

			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
				blockToBuild = dungeon.getPlankBlockState();
			}
		}

		return blockToBuild;
	}

	private IBlockState getBlockGrandEntry(BlockPos pos, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getMainBlockState();

		final int y = pos.getY() - this.origin.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
		final int distFromHalf = Math.abs(dist - halfPoint);

		final IBlockState outlineBlock = dungeon.getFancyBlockState();

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (distFromHalf == 0) {
				if (y <= 3) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					return dungeon.getFenceBlockState();
				} else if (y == 5) {
					return outlineBlock;
				}
			} else if (distFromHalf == 1) {
				if (y <= 2) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 3 || y == 4) {
					return dungeon.getFenceBlockState();
				} else if (y == 5) {
					return outlineBlock;
				}
			} else if (Math.abs(dist - halfPoint) == 2) {
				if (y <= 3) {
					return dungeon.getFenceBlockState();
				} else if (y == 4 || y == 5) {
					return outlineBlock;
				}
			} else if (Math.abs(dist - halfPoint) == 3) {
				if (y <= 4) {
					return outlineBlock;
				}
			}
		}

		return blockToBuild;
	}

	protected IBlockState getWindowBlock(BlockPos pos, DungeonCastle dungeon) {
		switch (this.windowType) {
		case BASIC_GLASS:
			return this.getBlockWindowBasicGlass(pos, dungeon);
		case CROSS_GLASS:
			return this.getBlockWindowCrossGlass(pos, dungeon);
		case SQUARE_BARS:
			return this.getBlockWindowSquareBars(pos, dungeon);
		case OPEN_SLIT:
		default:
			return this.getBlockWindowOpenSlit(pos, dungeon);
		}
	}

	private IBlockState getBlockWindowBasicGlass(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);

		if ((y == 2 || y == 3) && (dist == this.length / 2)) {
			return Blocks.GLASS_PANE.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private IBlockState getBlockWindowCrossGlass(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = this.length / 2;

		if ((dist == halfDist - 1 && y == 3) || (dist == halfDist && y >= 2 && y <= 4) || (dist == halfDist + 1 && y == 3)) {
			return Blocks.GLASS_PANE.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private IBlockState getBlockWindowSquareBars(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = this.length / 2;

		if (((y == 2) || (y == 3)) && ((dist == halfDist) || (dist == halfDist + 1))) {
			return Blocks.IRON_BARS.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private IBlockState getBlockWindowOpenSlit(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.origin.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = this.length / 2;

		if ((y == 2) && (dist >= halfDist - 1) && (dist <= halfDist + 1)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	public void addDoorCentered(EnumCastleDoorType type, Random random) {
		if (type == EnumCastleDoorType.RANDOM) {
			type = EnumCastleDoorType.getRandomRegularType(random);
		}
		this.doorType = type;
		this.doorStartOffset = (this.length - type.getWidth()) / 2;
	}

	public void addDoorRandomOffset(EnumCastleDoorType type, Random random) {
		if (type == EnumCastleDoorType.RANDOM) {
			type = EnumCastleDoorType.getRandomRegularType(random);
		}
		this.doorType = type;
		this.doorStartOffset = 1 + random.nextInt(this.length - type.getWidth() - 1);
	}

	/*
	 * Whether to build a door or window is usually determined by how far along the wall we are. This function gets the relevant length along the wall based on if
	 * we are a horizontal wall or a vertical wall.
	 */
	protected int getLengthPoint(BlockPos pos) {
		if (this.orientation == WallOrientation.VERTICAL) {
			return pos.getZ() - this.origin.getZ();
		} else {
			return pos.getX() - this.origin.getX();
		}
	}

	protected boolean withinDoorWidth(int value) {
		int relativeToDoor = value - this.doorStartOffset;
		return (relativeToDoor >= 0 && relativeToDoor < this.doorType.getWidth());
	}
}
