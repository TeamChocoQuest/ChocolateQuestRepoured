//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.segments;
//
//import net.minecraft.block.*;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.RandomCastleConfigOptions;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.RoomGridCell;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBossLandingMain;
//
//import java.util.EnumMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Random;
//
//public class CastleMainStructWall {
//	public enum WallOrientation {
//		HORIZONTAL, VERTICAL
//	}
//
//	private final int length;
//	private final int height;
//	private RandomCastleConfigOptions.WindowType windowType = RandomCastleConfigOptions.WindowType.BASIC_GLASS;
//	private boolean enabled = false;
//	private boolean isOuterWall = false;
//	private boolean isRoofEdge = false;
//	private int doorStartOffset = 0;
//	private EnumCastleDoorType doorType = EnumCastleDoorType.NONE;
//	private BlockPos origin;
//	private WallOrientation orientation;
//	private Map<Direction, RoomGridCell> adjacentCells = new EnumMap<>(Direction.class);
//
//	public CastleMainStructWall(BlockPos origin, WallOrientation orientation, int length, int height) {
//		this.origin = origin;
//		this.orientation = orientation;
//		this.length = length;
//		this.height = height;
//	}
//
//	public void registerAdjacentCell(RoomGridCell cell, Direction directionOfCell) {
//		this.adjacentCells.put(directionOfCell, cell);
//	}
//
//	public Optional<RoomGridCell> getAdjacentCell(Direction direction) {
//		if (this.adjacentCells.containsKey(direction)) {
//			return Optional.of(this.adjacentCells.get(direction));
//		} else {
//			return Optional.empty();
//		}
//	}
//
//	public BlockPos getOrigin() {
//		return this.origin;
//	}
//
//	public void enable() {
//		this.enabled = true;
//	}
//
//	public void disable() {
//		this.enabled = false;
//	}
//
//	public void setAsOuterWall() {
//		this.isOuterWall = true;
//	}
//
//	public void setAsInnerWall() {
//		this.isOuterWall = false;
//	}
//
//	public void setAsRoofEdge() {
//		this.isRoofEdge = true;
//	}
//
//	public void setAsNormalWall() {
//		this.isRoofEdge = false;
//	}
//
//	public boolean hasDoor() {
//		return (this.doorType != EnumCastleDoorType.NONE);
//	}
//
//	public boolean isEnabled() {
//		return this.enabled;
//	}
//
//	public boolean isVertical() {
//		return this.orientation == WallOrientation.VERTICAL;
//	}
//
//	public boolean isHorizontal() {
//		return this.orientation == WallOrientation.HORIZONTAL;
//	}
//
//	public int getGenerationPriority() {
//		if (this.enabled) {
//			if (this.isRoofEdge) {
//				// Roof edges have low priority so they don't replace regular walls with air
//				return 3;
//			} else if (this.isOuterWall) {
//				// Outer walls should go first since we want them all to be uniform
//				return 1;
//			} else {
//				// Everything else (inner walls)
//				return 2;
//			}
//		}
//		return Integer.MAX_VALUE;
//	}
//
//	public void determineIfEnabled(Random rand) {
//		Direction checkSide1;
//		Direction checkSide2;
//
//		if (this.orientation == WallOrientation.HORIZONTAL) {
//			checkSide1 = Direction.NORTH;
//			checkSide2 = Direction.SOUTH;
//		} else {
//			checkSide1 = Direction.WEST;
//			checkSide2 = Direction.EAST;
//		}
//
//		Optional<RoomGridCell> neighbor1 = this.getAdjacentCell(checkSide1);
//		Optional<RoomGridCell> neighbor2 = this.getAdjacentCell(checkSide2);
//
//		boolean neighbor1Populated = false;
//		boolean neighbor1IsWalkableRoof = false;
//		boolean neighbor1IsNormalRoof = false;
//		boolean neighbor1IsPreBoss = false;
//		boolean neighbor1IsBoss = false;
//
//		boolean neighbor2Populated = false;
//		boolean neighbor2IsWalkableRoof = false;
//		boolean neighbor2IsNormalRoof = false;
//		boolean neighbor2IsPreBoss = false;
//		boolean neighbor2IsBoss = false;
//
//		if (neighbor1.isPresent()) {
//			neighbor1Populated = neighbor1.get().isPopulated();
//			if (neighbor1Populated) {
//				neighbor1IsWalkableRoof = neighbor1.get().getRoom().isWalkableRoof();
//				neighbor1IsNormalRoof = neighbor1.get().getRoom().isReplacedRoof();
//				neighbor1IsBoss = neighbor1.get().isBossArea();
//				neighbor1IsPreBoss = neighbor1.get().getRoom().isBossLanding();
//			}
//		}
//
//		if (neighbor2.isPresent()) {
//			neighbor2Populated = neighbor2.get().isPopulated();
//			if (neighbor2Populated) {
//				neighbor2IsWalkableRoof = neighbor2.get().getRoom().isWalkableRoof();
//				neighbor2IsNormalRoof = neighbor2.get().getRoom().isReplacedRoof();
//				neighbor2IsBoss = neighbor2.get().isBossArea();
//				neighbor2IsPreBoss = neighbor2.get().getRoom().isBossLanding();
//			}
//		}
//
//		if (neighbor1IsNormalRoof && (neighbor2IsWalkableRoof || neighbor2IsBoss || !neighbor2Populated || neighbor2IsNormalRoof)) {
//			this.disable();
//		} else if (neighbor2IsNormalRoof && (neighbor1IsWalkableRoof || neighbor1IsBoss || !neighbor1Populated || neighbor1IsNormalRoof)) {
//			this.disable();
//		} else if (neighbor1IsBoss || neighbor2IsBoss) {
//			if (neighbor1IsBoss && neighbor2IsPreBoss) {
//				this.enable();
//				this.setAsInnerWall();
//				if (neighbor2.get().getRoom() instanceof CastleRoomBossLandingMain) {
//					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_1, new Random());
//				} else {
//					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_2, new Random());
//				}
//			} else if (neighbor2IsBoss && neighbor1IsPreBoss) {
//				this.enable();
//				this.setAsInnerWall();
//				if (neighbor1.get().getRoom() instanceof CastleRoomBossLandingMain) {
//					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_1, new Random());
//				} else {
//					this.addDoorCentered(EnumCastleDoorType.BOSS_HALF_2, new Random());
//				}
//			} else {
//				this.disable();
//			}
//		} else if (neighbor1Populated && neighbor2Populated) {
//			if (neighbor1.get().isConnectedToCell(neighbor2.get())) {
//				// if rooms are connected then there should be no wall between them
//				this.disable();
//			} else if (neighbor1IsWalkableRoof && neighbor2IsWalkableRoof) {
//				// no walls between roof tiles either
//				this.disable();
//			} else {
//				this.enable();
//				this.setAsInnerWall();
//			}
//		} else if (neighbor1Populated || neighbor2Populated) {
//			this.enable();
//			this.setAsOuterWall();
//			if (neighbor1IsWalkableRoof || neighbor2IsWalkableRoof) {
//				this.setAsRoofEdge();
//			}
//		} else {
//			this.disable();
//		}
//	}
//
//	public void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		BlockPos pos;
//		BlockState blockToBuild;
//
//		Direction iterDirection;
//		this.windowType = dungeon.getRandomWindowType(genArray.getRandom());
//
//		if (this.orientation == WallOrientation.VERTICAL) {
//			iterDirection = Direction.SOUTH;
//		} else {
//			iterDirection = Direction.EAST;
//		}
//
//		for (int i = 0; i < this.length; i++) {
//			for (int y = 0; y < this.height; y++) {
//				pos = this.origin.offset(iterDirection, i).offset(Direction.UP, y);
//				blockToBuild = this.getBlockToBuild(pos, dungeon);
//				BlockStateGenArray.EnumPriority priority = BlockStateGenArray.EnumPriority.MEDIUM;
//
//				if (blockToBuild.getBlock() == Blocks.AIR) {
//					priority = BlockStateGenArray.EnumPriority.LOWEST;
//				}
//				genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, priority);
//			}
//		}
//	}
//
//	protected BlockState getBlockToBuild(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		if (this.isRoofEdge) {
//			return this.getRoofEdgeBlock(pos, dungeon);
//		} else if (this.hasDoor()) {
//			return this.getDoorBlock(pos, dungeon);
//		} else if (this.isOuterWall) {
//			return this.getWindowBlock(pos, dungeon);
//		} else {
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	public boolean offsetIsDoorOrWindow(int distAlongWall, int heightOnWall, DungeonRandomizedCastle dungeon) {
//		// Determine the relative offset within the wall given the distance and height
//		int xDist = (this.orientation == WallOrientation.HORIZONTAL) ? distAlongWall : 0;
//		int zDist = (this.orientation == WallOrientation.VERTICAL) ? distAlongWall : 0;
//		BlockPos wallPosition = this.origin.add(xDist, heightOnWall, zDist);
//
//		// Consider this a door/window block if it is anything other then a regular castle block
//		return (this.getBlockToBuild(wallPosition, dungeon)) != dungeon.getMainBlockState();
//	}
//
//	private BlockState getRoofEdgeBlock(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//
//		if ((y == 0) || ((y == 1) && ((dist == this.length - 1) || (dist % 2 == 0)))) {
//			return dungeon.getMainBlockState();
//		} else {
//			return Blocks.AIR.getDefaultState();
//		}
//	}
//
//	private BlockState getBlockDoorBossHalf1(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//
//		if (dist > 2) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (y < this.height - 1) {
//				blockToBuild = Blocks.AIR.getDefaultState();
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	private BlockState getBlockDoorBossHalf2(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//
//		if (dist < (this.length - 3)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (y < this.height - 1) {
//				blockToBuild = Blocks.AIR.getDefaultState();
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	protected BlockState getDoorBlock(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		switch (this.doorType) {
//		case AIR:
//			return this.getBlockDoorAir(pos, dungeon);
//
//		case BOSS_HALF_1:
//			return this.getBlockDoorBossHalf1(pos, dungeon);
//
//		case BOSS_HALF_2:
//			return this.getBlockDoorBossHalf2(pos, dungeon);
//
//		case STANDARD:
//			return this.getBlockDoorStandard(pos, dungeon);
//
//		case FENCE_BORDER:
//			return this.getBlockDoorFenceBorder(pos, dungeon);
//
//		case STAIR_BORDER:
//			return this.getBlockDoorStairBorder(pos, dungeon);
//
//		case GRAND_ENTRY:
//			return this.getBlockGrandEntry(pos, dungeon);
//
//		default:
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	private BlockState getBlockDoorAir(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//
//		if (this.withinDoorWidth(dist)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (y < this.doorType.getHeight()) {
//				blockToBuild = Blocks.AIR.getDefaultState();
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	private BlockState getBlockDoorStairBorder(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		final int y = pos.getY() - this.origin.getY();
//		final int dist = this.getLengthPoint(pos);
//		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
//
//		if (this.withinDoorWidth(dist)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (dist == halfPoint || dist == halfPoint - 1) {
//				if (y >= 1 && y <= 3) {
//					blockToBuild = Blocks.AIR.getDefaultState();
//				} else if (y == 4) {
//					blockToBuild = dungeon.getSlabBlockState().withProperty(SlabBlock.HALF, SlabBlock.EnumBlockHalf.TOP);
//				}
//			} else if (dist == halfPoint + 1 || dist == halfPoint - 2) {
//				Direction stairFacing;
//
//				if (this.orientation == WallOrientation.HORIZONTAL) {
//					stairFacing = (dist == halfPoint - 2) ? Direction.WEST : Direction.EAST;
//				} else {
//					stairFacing = (dist == halfPoint - 2) ? Direction.NORTH : Direction.SOUTH;
//				}
//
//				BlockState stairBase = dungeon.getStairBlockState().withProperty(StairsBlock.FACING, stairFacing);
//
//				if (y == 1) {
//					blockToBuild = stairBase;
//				} else if (y == 2 || y == 3) {
//					blockToBuild = Blocks.AIR.getDefaultState();
//				} else if (y == 4) {
//					blockToBuild = stairBase.withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP);
//				}
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	private BlockState getBlockDoorFenceBorder(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		final int y = pos.getY() - this.origin.getY();
//		final int dist = this.getLengthPoint(pos);
//		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
//
//		if (this.withinDoorWidth(dist)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (dist == halfPoint || dist == halfPoint - 1) {
//				if (y == 1 || y == 2) {
//					blockToBuild = Blocks.AIR.getDefaultState();
//				} else if (y == 3) {
//					blockToBuild = dungeon.getFenceBlockState();
//				}
//			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
//				blockToBuild = dungeon.getFenceBlockState();
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	private BlockState getBlockDoorStandard(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//		final int y = pos.getY() - this.origin.getY();
//		final int dist = this.getLengthPoint(pos);
//		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
//
//		if (this.withinDoorWidth(dist)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getFloorBlockState();
//			} else if ((dist == halfPoint || dist == halfPoint - 1)) {
//				if (y == 1 || y == 2) {
//					DoorBlock.EnumDoorHalf half = (y == 1) ? DoorBlock.EnumDoorHalf.LOWER : DoorBlock.EnumDoorHalf.UPPER;
//
//					DoorBlock.EnumHingePosition hinge;
//					if (this.orientation == WallOrientation.HORIZONTAL) {
//						hinge = (dist == halfPoint) ? DoorBlock.EnumHingePosition.RIGHT : DoorBlock.EnumHingePosition.LEFT;
//					} else {
//						hinge = (dist == halfPoint) ? DoorBlock.EnumHingePosition.LEFT : DoorBlock.EnumHingePosition.RIGHT;
//					}
//					Direction facing = (this.orientation == WallOrientation.HORIZONTAL) ? Direction.NORTH : Direction.WEST;
//
//					blockToBuild = dungeon.getDoorBlockState().withProperty(DoorBlock.HALF, half).withProperty(DoorBlock.FACING, facing).withProperty(DoorBlock.HINGE, hinge);
//				} else if (y == 3) {
//					blockToBuild = dungeon.getPlankBlockState();
//				}
//
//			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
//				blockToBuild = dungeon.getPlankBlockState();
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	private BlockState getBlockGrandEntry(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild = dungeon.getMainBlockState();
//
//		final int y = pos.getY() - this.origin.getY();
//		final int dist = this.getLengthPoint(pos);
//		final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
//		final int distFromHalf = Math.abs(dist - halfPoint);
//
//		final BlockState outlineBlock = dungeon.getFancyBlockState();
//
//		if (this.withinDoorWidth(dist)) {
//			if (y == 0) {
//				blockToBuild = dungeon.getMainBlockState();
//			} else if (distFromHalf == 0) {
//				if (y <= 3) {
//					return Blocks.AIR.getDefaultState();
//				} else if (y == 4) {
//					return dungeon.getFenceBlockState();
//				} else if (y == 5) {
//					return outlineBlock;
//				}
//			} else if (distFromHalf == 1) {
//				if (y <= 2) {
//					return Blocks.AIR.getDefaultState();
//				} else if (y == 3 || y == 4) {
//					return dungeon.getFenceBlockState();
//				} else if (y == 5) {
//					return outlineBlock;
//				}
//			} else if (Math.abs(dist - halfPoint) == 2) {
//				if (y <= 3) {
//					return dungeon.getFenceBlockState();
//				} else if (y == 4 || y == 5) {
//					return outlineBlock;
//				}
//			} else if (Math.abs(dist - halfPoint) == 3) {
//				if (y <= 4) {
//					return outlineBlock;
//				}
//			}
//		}
//
//		return blockToBuild;
//	}
//
//	protected BlockState getWindowBlock(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		switch (this.windowType) {
//		case BASIC_GLASS:
//			return this.getBlockWindowBasicGlass(pos, dungeon);
//		case CROSS_GLASS:
//			return this.getBlockWindowCrossGlass(pos, dungeon);
//		case SQUARE_BARS:
//			return this.getBlockWindowSquareBars(pos, dungeon);
//		case OPEN_SLIT:
//		default:
//			return this.getBlockWindowOpenSlit(pos, dungeon);
//		}
//	}
//
//	private BlockState getBlockWindowBasicGlass(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//
//		if ((y == 2 || y == 3) && (dist == this.length / 2)) {
//			return Blocks.GLASS_PANE.getDefaultState();
//		} else {
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	private BlockState getBlockWindowCrossGlass(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//		int halfDist = this.length / 2;
//
//		if ((dist == halfDist - 1 && y == 3) || (dist == halfDist && y >= 2 && y <= 4) || (dist == halfDist + 1 && y == 3)) {
//			return Blocks.GLASS_PANE.getDefaultState();
//		} else {
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	private BlockState getBlockWindowSquareBars(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//		int halfDist = this.length / 2;
//
//		if (((y == 2) || (y == 3)) && ((dist == halfDist) || (dist == halfDist + 1))) {
//			return Blocks.IRON_BARS.getDefaultState();
//		} else {
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	private BlockState getBlockWindowOpenSlit(BlockPos pos, DungeonRandomizedCastle dungeon) {
//		int y = pos.getY() - this.origin.getY();
//		int dist = this.getLengthPoint(pos);
//		int halfDist = this.length / 2;
//
//		if ((y == 2) && (dist >= halfDist - 1) && (dist <= halfDist + 1)) {
//			return Blocks.AIR.getDefaultState();
//		} else {
//			return dungeon.getMainBlockState();
//		}
//	}
//
//	public void addDoorCentered(EnumCastleDoorType type, Random random) {
//		if (type == EnumCastleDoorType.RANDOM) {
//			type = EnumCastleDoorType.getRandomRegularType(random);
//		}
//		this.doorType = type;
//		this.doorStartOffset = (this.length - type.getWidth()) / 2;
//	}
//
//	public void addDoorRandomOffset(EnumCastleDoorType type, Random random) {
//		if (type == EnumCastleDoorType.RANDOM) {
//			type = EnumCastleDoorType.getRandomRegularType(random);
//		}
//		this.doorType = type;
//		this.doorStartOffset = 1 + random.nextInt(this.length - type.getWidth() - 1);
//	}
//
//	/*
//	 * Whether to build a door or window is usually determined by how far along the wall we are. This function gets the
//	 * relevant length along the wall based on if
//	 * we are a horizontal wall or a vertical wall.
//	 */
//	protected int getLengthPoint(BlockPos pos) {
//		if (this.orientation == WallOrientation.VERTICAL) {
//			return pos.getZ() - this.origin.getZ();
//		} else {
//			return pos.getX() - this.origin.getX();
//		}
//	}
//
//	protected boolean withinDoorWidth(int value) {
//		int relativeToDoor = value - this.doorStartOffset;
//		return (relativeToDoor >= 0 && relativeToDoor < this.doorType.getWidth());
//	}
//}
