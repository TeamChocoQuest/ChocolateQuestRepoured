package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.EnumFacing;

public class RoomGrid {
	public static class Area2D {
		public RoomGridPosition start;
		public int sizeX;
		public int sizeZ;

		public Area2D(RoomGridPosition start, int sizeX, int sizeZ) {
			this.start = start;
			this.sizeX = sizeX;
			this.sizeZ = sizeZ;
		}

		public Area2D(Area2D area) {
			this.start = area.start;
			this.sizeX = area.sizeX;
			this.sizeZ = area.sizeZ;
		}

		public Area2D addFloors(int numFloors) {
			return new Area2D(this.start.move(EnumFacing.UP, numFloors), this.sizeX, this.sizeZ);
		}

		public int getStartX() {
			return this.start.getX();
		}

		public int getStartZ() {
			return this.start.getZ();
		}

		public int getEndX() {
			return this.start.getX() + this.sizeX - 1;
		}

		public int getEndZ() {
			return this.start.getZ() + this.sizeZ - 1;
		}

		public RoomGridPosition getTopRight() {
			return this.start.move(EnumFacing.EAST, this.sizeX - 1);
		}

		public RoomGridPosition getBottomLeft() {
			return this.start.move(EnumFacing.SOUTH, this.sizeZ - 1);
		}

		public boolean dimensionsAre(int dim1, int dim2) {
			return (this.sizeX == dim1 && this.sizeZ == dim2) || (this.sizeX == dim2 && this.sizeZ == 1);
		}

		public boolean dimensionsAreAtLeast(int dim1, int dim2) {
			int larger = Math.max(dim1, dim2);
			int smaller = Math.min(dim1, dim2);
			return (Math.min(this.sizeX, this.sizeZ) >= smaller) && (Math.max(this.sizeX, this.sizeZ) >= larger);
		}

		public int cellsFromStartX(RoomGridPosition gridPos) {
			return gridPos.getX() - this.start.getX();
		}

		public int cellsFromStartZ(RoomGridPosition gridPos) {
			return gridPos.getZ() - this.start.getZ();
		}

		public ArrayList<RoomGridPosition> getPositionList() {
			ArrayList<RoomGridPosition> positions = new ArrayList<>();

			for (int x = 0; x < this.sizeX; x++) {
				for (int z = 0; z < this.sizeZ; z++) {
					positions.add(this.start.move(EnumFacing.EAST, x).move(EnumFacing.SOUTH, z));
				}
			}

			return positions;
		}

		public void removeFromList(List<RoomGridPosition> positions) {
			ArrayList<RoomGridPosition> myPositions = this.getPositionList();
			positions.removeAll(myPositions);
		}

		public Area2D getRandomSubArea(Random random, int minDim1, int minDim2, boolean mustBeSmaller) {
			// Make sure this area has the space to fit the sub dimensions
			if (this.dimensionsAreAtLeast(minDim1, minDim2)) {
				int resultX;
				int resultZ;
				int shrink = mustBeSmaller ? 1 : 0;

				// Figure out which dimension is larger (so parameter order doesn't matter)
				int larger = Math.max(minDim1, minDim2);
				int smaller = Math.min(minDim1, minDim2);

				// Determine which directions (X and Z) have the room to fit the longer of the two dimensions
				boolean fitsX = this.sizeX >= larger;
				boolean fitsZ = this.sizeZ >= larger;

				// If either dimension could be the long side, then pick at random
				if (fitsX && fitsZ) {
					if (random.nextBoolean()) {
						resultX = DungeonGenUtils.randomBetweenGaussian(random, larger, this.sizeX - shrink);
						resultZ = DungeonGenUtils.randomBetweenGaussian(random, smaller, this.sizeZ - shrink);
					} else {
						resultZ = DungeonGenUtils.randomBetweenGaussian(random, larger, this.sizeZ - shrink);
						resultX = DungeonGenUtils.randomBetweenGaussian(random, smaller, this.sizeX - shrink);
					}
				}
				// Otherwise use the side with more room as the long side
				else if (fitsX) {
					resultX = DungeonGenUtils.randomBetweenGaussian(random, larger, this.sizeX - shrink);
					resultZ = DungeonGenUtils.randomBetweenGaussian(random, smaller, this.sizeZ - shrink);
				} else {
					resultZ = DungeonGenUtils.randomBetweenGaussian(random, larger, this.sizeZ - shrink);
					resultX = DungeonGenUtils.randomBetweenGaussian(random, smaller, this.sizeX - shrink);
				}

				return this.randomlyPositionSubArea(random, resultX, resultZ);
			} else {
				// Impossible to meet dimension constraints so just stay the same
				return this;
			}
		}

		public Area2D getExactSubArea(Random random, int minDim1, int minDim2) {
			// Make sure this area has the space to fit the sub dimensions
			if (this.dimensionsAreAtLeast(minDim1, minDim2)) {
				int resultX;
				int resultZ;
				boolean xIsLonger;

				// Figure out which dimension is larger (so parameter order doesn't matter)
				int larger = Math.max(minDim1, minDim2);
				int smaller = Math.min(minDim1, minDim2);

				// Determine which directions (X and Z) have the room to fit the longer of the two dimensions
				boolean fitsX = this.sizeX >= larger;
				boolean fitsZ = this.sizeZ >= larger;

				// If either dimension could be the long side, then pick at random, otherwise force long side
				xIsLonger = (fitsX && fitsZ) ? (random.nextBoolean()) : fitsX;

				resultX = xIsLonger ? larger : smaller;
				resultZ = xIsLonger ? smaller : larger;

				return this.randomlyPositionSubArea(random, resultX, resultZ);
			} else {
				// Impossible to meet dimension constraints so just stay the same
				return this;
			}
		}

		private Area2D randomlyPositionSubArea(Random random, int resultX, int resultZ) {
			RoomGridPosition subStart = this.start;
			int maxMoveX = this.sizeX - resultX;
			int maxMoveZ = this.sizeZ - resultZ;
			if (maxMoveX > 0) {
				subStart = subStart.move(EnumFacing.EAST, random.nextInt(this.sizeX - resultX));
			}
			if (maxMoveZ > 0) {
				subStart = subStart.move(EnumFacing.SOUTH, random.nextInt(this.sizeZ - resultZ));
			}

			return new Area2D(subStart, resultX, resultZ);
		}

		@Nullable
		public Area2D sliceToSideOfArea(Area2D mask, EnumFacing side) {
			if (mask != null) {
				RoomGridPosition resultStart;
				int resultSizeX;
				int resultSizeZ;

				if (side == EnumFacing.NORTH) {
					resultStart = this.start;
					resultSizeX = this.sizeX;
					resultSizeZ = mask.getStartZ() - this.getStartZ();
				} else if (side == EnumFacing.SOUTH) {
					resultStart = new RoomGridPosition(this.start.getFloor(), this.start.getX(), mask.getEndZ() + 1);
					resultSizeX = this.sizeX;
					resultSizeZ = this.getEndZ() - mask.getEndZ();
				} else if (side == EnumFacing.WEST) {
					resultStart = this.start;
					resultSizeX = mask.getStartX() - this.getStartX();
					resultSizeZ = this.sizeZ;
				} else // East
				{
					resultStart = new RoomGridPosition(this.start.getFloor(), mask.getEndX() + 1, this.start.getZ());
					resultSizeX = this.getEndX() - mask.getEndX();
					resultSizeZ = this.sizeZ;
				}

				ArrayList<RoomGridPosition> boundary = this.getPositionList();
				if (boundary.contains(resultStart) && resultSizeX > 0 && resultSizeZ > 0) {
					return new Area2D(resultStart, resultSizeX, resultSizeZ);
				} else {
					return null;
				}
			}

			return null;
		}

		public void alignToSide(Random random, Area2D targetArea, EnumFacing side, Area2D boundary) {
			int distance; // distance to move toward target area so we are adjacent

			// slide position is position perpendicular to the side to are least 1 square is touching
			int minSlide;
			int maxSlide;
			int slideDest;

			if (side == EnumFacing.NORTH) {
				this.start.setZ(targetArea.getStartZ() - this.sizeZ);

				minSlide = Math.max((targetArea.getStartX() - (this.sizeX - 1)), boundary.start.getX());
				maxSlide = Math.min((targetArea.getEndX()), boundary.getEndX() - (this.sizeX - 1));
				slideDest = DungeonGenUtils.randomBetweenGaussian(random, minSlide, maxSlide);

				this.start.setX(slideDest);
			} else if (side == EnumFacing.SOUTH) {
				this.start.setZ(targetArea.getEndZ() + 1);

				minSlide = Math.max((targetArea.getStartX() - (this.sizeX - 1)), boundary.start.getX());
				maxSlide = Math.min((targetArea.getEndX()), boundary.getEndX() - (this.sizeX - 1));
				slideDest = DungeonGenUtils.randomBetweenGaussian(random, minSlide, maxSlide);

				this.start.setX(slideDest);
			} else if (side == EnumFacing.WEST) {
				this.start.setX(targetArea.getStartX() - this.sizeX);

				minSlide = Math.max((targetArea.getStartZ() - (this.sizeZ - 1)), boundary.start.getZ());
				maxSlide = Math.min((targetArea.getEndZ()), boundary.getEndZ() - (this.sizeZ - 1));
				slideDest = DungeonGenUtils.randomBetweenGaussian(random, minSlide, maxSlide);

				this.start.setZ(slideDest);
			} else // East
			{
				this.start.setX(targetArea.getEndX() + 1);

				minSlide = Math.max((targetArea.getStartZ() - (this.sizeZ - 1)), boundary.start.getZ());
				maxSlide = Math.min((targetArea.getEndZ()), boundary.getEndZ() - (this.sizeZ - 1));
				slideDest = DungeonGenUtils.randomBetweenGaussian(random, minSlide, maxSlide);

				this.start.setZ(slideDest);
			}
		}

		@Override
		public String toString() {
			return String.format("RoomGrid.Area2D{start=%s, sizeX=%d, sizeZ=%d}", this.start, this.sizeX, this.sizeZ);
		}
	}

	private static final int MAX_lAYERS = 5;

	private int floors;
	private int roomsX;
	private int roomsZ;
	private Random random;
	private RoomGridCell[][][] cellArray;
	private List<RoomGridCell> cellList;
	private Area2D bossArea = null;

	public RoomGrid(int floors, int roomsX, int roomsZ, Random random) {
		this.floors = floors;
		this.roomsX = roomsX;
		this.roomsZ = roomsZ;
		this.random = random;
		this.cellArray = new RoomGridCell[floors][roomsX][roomsZ];
		this.cellList = new ArrayList<>();

		// initialize the room grid
		for (int floor = 0; floor < floors; floor++) {
			for (int x = 0; x < roomsX; x++) {
				for (int z = 0; z < roomsZ; z++) {
					this.cellArray[floor][x][z] = new RoomGridCell(floor, x, z, null);
					this.cellList.add(this.cellArray[floor][x][z]);
				}
			}
		}
	}

	public void setRoomReachable(int floor, int x, int z) {
		this.cellArray[floor][x][z].setReachable();
	}

	public ArrayList<RoomGridCell> getCellListCopy() {
		return new ArrayList<>(this.cellList);
	}

	public ArrayList<RoomGridCell> getAllCellsWhere(Predicate<RoomGridCell> p) {
		return this.getCellListCopy().stream().filter(p).collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<RoomGridCell> getSelectedCellsInColumn(int floor, int columnIndex) {
		ArrayList<RoomGridCell> result = this.getCellListCopy();
		result.removeIf(r -> r.getFloor() != floor);
		result.removeIf(r -> r.getGridX() != columnIndex);
		result.removeIf(r -> !r.isSelectedForBuilding());
		result.removeIf(r -> !r.isPopulated());
		return result;
	}

	public ArrayList<RoomGridCell> getSelectedCellsInRow(int floor, int rowIndex) {
		ArrayList<RoomGridCell> result = this.getCellListCopy();
		result.removeIf(r -> r.getFloor() != floor);
		result.removeIf(r -> r.getGridZ() != rowIndex);
		result.removeIf(r -> !r.isSelectedForBuilding());
		result.removeIf(r -> !r.isPopulated());
		return result;
	}

	public ArrayList<RoomGridCell> getSelectedMainStructCells(int floor) {
		ArrayList<RoomGridCell> result = this.getCellListCopy();
		result.removeIf(r -> r.getFloor() != floor);
		result.removeIf(r -> !r.isSelectedForBuilding());
		result.removeIf(r -> !r.isMainStruct());
		return result;
	}

	public ArrayList<RoomGridCell> getCellsWithoutAType() {
		ArrayList<RoomGridCell> result = this.getCellListCopy();
		result.removeIf(r -> !r.needsRoomType());
		return result;
	}

	/*
	 * Returns a list (from largest area to smallest area) of contiguous 2d grid areas
	 * that are on a given floor and satisfy a given condition.
	 *
	 * Note: Areas 2x2 or larger will always evaluate as larger than anything 1xN or Nx1.
	 * This is done on purpose because 2x2+ areas are more useful for building.
	 */
	public ArrayList<Area2D> getAllGridAreasWhere(int floor, Predicate<RoomGridCell> condition, int minDimension1, int minDimension2) {
		ArrayList<RoomGridPosition> floorPositions = new ArrayList<>();
		condition = condition.and(c -> c.getFloor() == floor);

		this.getAllCellsWhere(condition).forEach(c -> floorPositions.add(c.getGridPosition()));

		ArrayList<Area2D> areas = new ArrayList<>();

		Area2D largest = this.getLargestAreaWhere(floorPositions, condition);

		while (largest != null && !floorPositions.isEmpty() && largest.dimensionsAreAtLeast(minDimension1, minDimension2)) {
			areas.add(largest);
			largest.removeFromList(floorPositions);

			largest = this.getLargestAreaWhere(floorPositions, condition);
		}

		return areas;
	}

	@Nullable
	public Area2D getLargestAreaWhere(ArrayList<RoomGridPosition> floorPositions, Predicate<RoomGridCell> condition) {
		int largestArea = 0;
		int largestX = 0;
		int largestZ = 0;
		RoomGridPosition largestStart = null;

		if (!floorPositions.isEmpty()) {
			for (RoomGridPosition startPos : floorPositions) {
				int x = 1;
				int z = 1;
				boolean incX = true;
				boolean incZ = true;

				RoomGridPosition pos = new RoomGridPosition(startPos);
				do {
					if (incX) {
						++x;
					}
					for (int i = 0; i < z; i++) {
						RoomGridPosition checkPos = startPos.move(EnumFacing.EAST, (x - 1)).move(EnumFacing.SOUTH, i);
						if (!floorPositions.contains(checkPos) || !this.withinGridBounds(checkPos) || !condition.test(this.getCellAt(checkPos))) {
							incX = false;
							--x;
							break;
						}
					}

					if (incZ) {
						++z;
					}
					for (int i = 0; i < x; i++) {
						RoomGridPosition checkPos = startPos.move(EnumFacing.EAST, i).move(EnumFacing.SOUTH, (z - 1));
						if (!floorPositions.contains(checkPos) || !this.withinGridBounds(checkPos) || !condition.test(this.getCellAt(checkPos))) {
							incZ = false;
							--z;
							break;
						}
					}
				} while (incX || incZ);

				final int area = x * z;

				// Anything > 2x2 should always evaluate as larger than anything 1xN or Nx1.
				if ((area > largestArea) || (x > 1 && z > 1 && (largestX == 1 || largestZ == 1))) {
					largestArea = x * z;
					largestX = x;
					largestZ = z;
					largestStart = startPos;
				}
			}

			return new Area2D(largestStart, largestX, largestZ);
		} else {
			return null;
		}
	}

	public int getContiguousUntypedRoomsX(RoomGridPosition start) {
		RoomGridPosition pos = start;
		RoomGridCell cell = this.getCellAt(pos);
		int result = 0;

		while (cell != null && cell.needsRoomType()) {
			++result;
			pos = pos.move(EnumFacing.EAST);
			cell = this.getCellAt(pos);
		}

		return result;
	}

	public int getContiguousUntypedRoomsZ(RoomGridPosition start) {
		RoomGridPosition pos = start;
		RoomGridCell cell = this.getCellAt(pos);
		int result = 0;

		while (cell != null && cell.needsRoomType()) {
			++result;
			pos = pos.move(EnumFacing.SOUTH);
			cell = this.getCellAt(pos);
		}

		return result;
	}

	public RoomGridCell getCellAt(int floor, int x, int z) {
		if (this.withinGridBounds(floor, x, z)) {
			return this.cellArray[floor][x][z];
		} else {
			return null;
		}
	}

	@Nullable
	public RoomGridCell getCellAt(RoomGridPosition position) {
		if (this.withinGridBounds(position.getFloor(), position.getX(), position.getZ())) {
			return this.cellArray[position.getFloor()][position.getX()][position.getZ()];
		} else {
			return null;
		}
	}

	public void selectBlockOfCellsForBuilding(Area2D area, int numFloors) {
		ArrayList<RoomGridPosition> positions = area.getPositionList();
		for (RoomGridPosition areaPos : positions) {
			int floor = 0;
			RoomGridPosition gridPos;
			RoomGridCell cell;

			// First go through each floor and select the cells for building
			for (; floor < numFloors; floor++) {
				gridPos = areaPos.move(EnumFacing.UP, floor);
				cell = this.getCellAt(gridPos);
				if (cell != null) {
					cell.selectForBuilding();
				}
			}

			// Then set the one cell above floors to buildable to begin the next layer
			gridPos = areaPos.move(EnumFacing.UP, floor);
			cell = this.getCellAt(gridPos);
			if (cell != null) {
				cell.setBuildable();
			}
		}

		this.initPathingForCellArea(area, numFloors);
	}

	public void initPathingForCellArea(Area2D baseArea, int floors) {
		for (int floor = 0; floor < floors; floor++) {
			Area2D currentFloorArea;
			if (floor == 0) {
				currentFloorArea = baseArea;
			} else {
				currentFloorArea = baseArea.addFloors(floor);
			}

			HashSet<RoomGridCell> cellsInArea = new HashSet<>();
			currentFloorArea.getPositionList().forEach(p -> cellsInArea.add(this.getCellAt(p)));

			for (RoomGridCell cell : cellsInArea) {
				if (cell == null) {
					System.out.println("How did this happen?");
					return;
				}
				cell.addPathableCells(cellsInArea);
			}
			this.setPathingForCellSet(cellsInArea);
		}
	}

	public void initPathingForSingleCell(RoomGridPosition gridPos) {
		if (this.withinGridBounds(gridPos) && this.getCellAt(gridPos) != null) {
			HashSet<RoomGridCell> cellsInArea = new HashSet<>();
			cellsInArea.add(this.getCellAt(gridPos));

			this.setPathingForCellSet(cellsInArea);
		}
	}

	private void setPathingForCellSet(HashSet<RoomGridCell> cellsInArea) {
		// For each cell in this area
		for (RoomGridCell cell : cellsInArea) {
			// Check N E S W
			for (EnumFacing direction : EnumFacing.HORIZONTALS) {
				// If adjacent cell isn't part of my area and is selected
				RoomGridCell adjacent = this.getAdjacentCell(cell, direction);
				if (adjacent != null && // adjacent cell exists
						!cellsInArea.contains(adjacent) && // not part of my area already
						adjacent.isSelectedForBuilding() && // adjacent cell is selected
						!cell.getPathableCellsCopy().contains(adjacent)) // I haven't already been pathed to it
				{
					// Copy the adjacent cell's pathable list to all cells in my area
					for (RoomGridCell myPathable : cellsInArea) {
						myPathable.addPathableCells(adjacent.getPathableCellsCopy());
					}
					// Copy my pathable list out to all the adjacent cell's pathable cells
					HashSet<RoomGridCell> adjacentPathable = adjacent.getPathableCellsCopy();
					for (RoomGridCell theirPathable : adjacentPathable) {
						theirPathable.addPathableCells(cell.getPathableCellsCopy());
					}
				}
			}
		}
	}

	public boolean adjacentCellIsPopulated(RoomGridCell startCell, EnumFacing direction) {
		RoomGridCell adjacent = this.getAdjacentCell(startCell, direction);
		return (adjacent != null && adjacent.isPopulated());
	}

	public boolean adjacentCellIsFullRoom(RoomGridCell startCell, EnumFacing direction) {
		RoomGridCell adjacent = this.getAdjacentCell(startCell, direction);
		return (adjacent != null && adjacent.isPopulated() && !(adjacent.getRoom() instanceof CastleRoomWalkableRoof));
	}

	public boolean adjacentCellIsSelected(RoomGridCell startCell, EnumFacing direction) {
		RoomGridCell adjacent = this.getAdjacentCell(startCell, direction);
		return (adjacent != null && adjacent.isSelectedForBuilding());
	}

	public boolean adjacentCellIsWalkableRoof(RoomGridCell startCell, EnumFacing direction) {
		RoomGridCell adjacent = this.getAdjacentCell(startCell, direction);
		return (adjacent != null && adjacent.isPopulated() && adjacent.getRoom() instanceof CastleRoomWalkableRoof);
	}

	public boolean cellIsValidForRoof(RoomGridCell cell) {
		RoomGridCell below = this.getAdjacentCell(cell, EnumFacing.DOWN);

		return (below != null && !cell.isSelectedForBuilding() && below.isPopulated() && !below.getRoom().getRoomType().isBossRoom());
	}

	public boolean cellIsOuterEdge(RoomGridCell cell, EnumFacing direction) {
		RoomGridPosition coords = cell.getGridPosition();

		coords = coords.move(direction);
		while (this.withinGridBounds(coords)) {
			if (this.getCellAt(coords).isPopulated()) {
				return false;
			}
			coords = coords.move(direction);
		}

		return true;
	}

	public ArrayList<RoomGridCell> getAdjacentSelectedCellsInRow(RoomGridPosition position) {
		ArrayList<RoomGridCell> result = new ArrayList<>();

		while (this.getCellAt(position.move(EnumFacing.WEST)) != null && this.getCellAt(position.move(EnumFacing.WEST)).isSelectedForBuilding()) {
			position = position.move(EnumFacing.WEST);
		}

		result.add(this.getCellAt(position));

		while (this.getCellAt(position.move(EnumFacing.EAST)) != null && this.getCellAt(position.move(EnumFacing.EAST)).isSelectedForBuilding()) {
			position = position.move(EnumFacing.EAST);
			result.add(this.getCellAt(position));
		}

		return result;
	}

	public ArrayList<RoomGridCell> getAdjacentSelectedCellsInColumn(RoomGridPosition position) {
		ArrayList<RoomGridCell> result = new ArrayList<>();

		while (this.getCellAt(position.move(EnumFacing.NORTH)) != null && this.getCellAt(position.move(EnumFacing.NORTH)).isSelectedForBuilding()) {
			position = position.move(EnumFacing.NORTH);
		}

		result.add(this.getCellAt(position));

		while (this.getCellAt(position.move(EnumFacing.SOUTH)) != null && this.getCellAt(position.move(EnumFacing.SOUTH)).isSelectedForBuilding()) {
			position = position.move(EnumFacing.SOUTH);
			result.add(this.getCellAt(position));
		}

		return result;
	}

	/*
	 * Determine if a tower can be attached next to the given cell
	 */
	public boolean canAttachTower(RoomGridCell cell, EnumFacing side) {
		RoomGridCell adjacent = this.getAdjacentCell(cell, side);

		return (!cell.getRoom().isTower() && !cell.getRoom().hasDoorOnSide(side) && adjacent != null && !(adjacent.isPopulated() && !cell.getRoom().isStairsOrLanding()));
	}

	public double distanceBetweenCells2D(RoomGridCell c1, RoomGridCell c2) {
		int distX = Math.abs(c1.getGridX() - c2.getGridX());
		int distZ = Math.abs(c1.getGridZ() - c2.getGridZ());
		return (Math.hypot(distX, distZ));
	}

	public boolean cellBordersRoomType(RoomGridCell cell, EnumRoomType type) {
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			RoomGridCell adjacent = this.getAdjacentCell(cell, side);
			if (adjacent != null && adjacent.isPopulated() && adjacent.getRoom().getRoomType() == type) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	public RoomGridCell getAdjacentCell(RoomGridCell startCell, EnumFacing direction) {
		RoomGridPosition startPosition = startCell.getGridPosition();
		int floor = startPosition.getFloor();
		int x = startPosition.getX();
		int z = startPosition.getZ();

		switch (direction) {
		case UP:
			floor += 1;
			break;
		case DOWN:
			floor -= 1;
			break;
		case NORTH:
			z -= 1;
			break;
		case SOUTH:
			z += 1;
			break;
		case WEST:
			x -= 1;
			break;
		case EAST:
			x += 1;
			break;
		default:
			break;
		}

		if (this.withinGridBounds(floor, x, z)) {
			return this.cellArray[floor][x][z];
		} else {
			return null;
		}
	}

	public void setBossArea(Area2D area) {
		this.bossArea = new Area2D(area);
		for (RoomGridPosition gridPos : area.getPositionList()) {
			if (this.withinGridBounds(gridPos)) {
				this.getCellAt(gridPos).setAsBossArea();
			}
		}
	}

	@Nullable
	public Area2D getBossArea() {
		return this.bossArea;
	}

	public boolean bossAreaSet() {
		return this.bossArea != null;
	}

	public boolean withinGridBounds(int floor, int x, int z) {
		return (floor >= 0 && floor < this.floors && this.withinFloorBounds(x, z));
	}

	public boolean withinGridBounds(RoomGridPosition position) {
		return (position.getFloor() >= 0 && position.getFloor() < this.floors && this.withinFloorBounds(position.getX(), position.getZ()));
	}

	public boolean withinFloorBounds(int x, int z) {
		return (x >= 0 && x < this.roomsX && z >= 0 && z < this.roomsZ);
	}

}
