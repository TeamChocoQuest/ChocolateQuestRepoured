package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.HashSet;
import java.util.List;

import net.minecraft.util.EnumFacing;

public class RoomGridCell {
	private enum CellState {
		UNUSED(0, "Unused"), // empty and cannot build anything on this space
		BUILDABLE(1, "Buildable"), // empty but able to build on this space
		SELECTED(2, "Selected"), // selected for building but not filled with a room
		POPULATED(3, "Populated"); // filled with a room

		private final int value;
		private final String text;

		CellState(int value, String text) {
			this.value = value;
			this.text = text;
		}

		private boolean isAtLeast(CellState state) {
			return this.value >= state.value;
		}

		private boolean isLessThan(CellState state) {
			return this.value < state.value;
		}
	}

	private RoomGridPosition gridPosition;
	private CellState state = CellState.UNUSED;
	private boolean reachable = false;
	private boolean floorHasLanding = false;
	private boolean partOfMainStruct = false;
	private CastleRoom room;
	private boolean narrow;
	private HashSet<RoomGridCell> linkedCells; // cells that are connected to this room (no walls between)
	private HashSet<RoomGridCell> pathableCells; // cells on the same floor that are potentially reachable
	private boolean isBossArea = false;

	public RoomGridCell(int floor, int x, int z, CastleRoom room) {
		this.room = room;
		this.gridPosition = new RoomGridPosition(floor, x, z);
		this.linkedCells = new HashSet<>();
		this.pathableCells = new HashSet<>();
	}

	public void setAllLinkedReachable(List<RoomGridCell> unreachableCells, List<RoomGridCell> reachableCells) {
		this.reachable = true;

		for (RoomGridCell linkedCell : this.getLinkedCellsCopy()) {
			linkedCell.setReachable();
			unreachableCells.remove(linkedCell);

			// TODO: This would be easier and faster with a hashset
			if (!reachableCells.contains(linkedCell)) {
				reachableCells.add(linkedCell);
			}
		}
	}

	public void setReachable() {
		this.reachable = true;
	}

	public boolean isReachable() {
		return this.reachable;
	}

	public void setAsMainStruct() {
		this.partOfMainStruct = true;
	}

	public boolean isMainStruct() {
		return this.partOfMainStruct;
	}

	public void setNarrow() {
		this.narrow = true;
	}

	public boolean isNarrow() {
		return this.narrow;
	}

	public void setBuildable() {
		if (this.state.isLessThan(CellState.BUILDABLE)) {
			this.state = CellState.BUILDABLE;
		}
	}

	public boolean isBuildable() {
		return (this.state.isAtLeast(CellState.BUILDABLE));
	}

	public void selectForBuilding() {
		if (this.state.isLessThan(CellState.SELECTED)) {
			this.state = CellState.SELECTED;
		}
	}

	public boolean isSelectedForBuilding() {
		return (this.state.isAtLeast(CellState.SELECTED));
	}

	public boolean isPopulated() {
		return (this.state.isAtLeast(CellState.POPULATED));
	}

	// Returns true if this room is selected to build but has not been populated with a room
	public boolean needsRoomType() {
		return (this.state == CellState.SELECTED);
	}

	public boolean isValidPathStart() {
		return !this.isReachable() && this.isPopulated() && this.room.isPathable();
	}

	public boolean isValidPathDestination() {
		return this.isReachable() && this.isPopulated() && this.room.isPathable();
	}

	public boolean isValidHallwayRoom() {
		return this.needsRoomType() && !this.isBossArea;
	}

	public double distanceTo(RoomGridCell destCell) {
		int distX = Math.abs(this.getGridX() - destCell.getGridX());
		int distZ = Math.abs(this.getGridZ() - destCell.getGridZ());
		return (Math.hypot(distX, distZ));
	}

	public CastleRoom getRoom() {
		return this.room;
	}

	public void setRoom(CastleRoom room) {
		this.room = room;
		this.state = CellState.POPULATED;
	}

	public boolean reachableFromSide(EnumFacing side) {
		if (this.room != null) {
			return this.room.reachableFromSide(side);
		} else {
			return true;
		}
	}

	public RoomGridPosition getGridPosition() {
		return this.gridPosition;
	}

	public int getFloor() {
		return this.gridPosition.getFloor();
	}

	public int getGridX() {
		return this.gridPosition.getX();
	}

	public int getGridZ() {
		return this.gridPosition.getZ();
	}

	public void setAsBossArea() {
		this.isBossArea = true;
	}

	public boolean isBossArea() {
		return this.isBossArea;
	}

	public void linkToCell(RoomGridCell cell) {
		this.linkedCells.add(cell);
	}

	public void setLinkedCells(HashSet<RoomGridCell> cells) {
		this.linkedCells = new HashSet<>(cells);
	}

	public HashSet<RoomGridCell> getLinkedCellsCopy() {
		return new HashSet<>(this.linkedCells); // return a copy
	}

	public boolean isLinkedToCell(RoomGridCell cell) {
		return this.linkedCells.contains(cell);
	}

	public void addPathableCells(HashSet<RoomGridCell> cells) {
		this.pathableCells.addAll(cells);
	}

	public HashSet<RoomGridCell> getPathableCellsCopy() {
		return new HashSet<>(this.pathableCells);
	}

	public boolean isOnFloorWithLanding() {
		return this.floorHasLanding;
	}

	private void setHasLanding() {
		this.floorHasLanding = true;
	}

	public void setLandingForAllPathableCells() {
		for (RoomGridCell cell : this.pathableCells) {
			cell.setHasLanding();
		}
	}

	@Override
	public String toString() {
		String roomStr = (this.getRoom() == null) ? "null" : this.getRoom().toString();
		return String.format("RoomGridCell{%s, state=%s, room=%s}", this.gridPosition.toString(), this.state.toString(), roomStr);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RoomGridCell)) {
			return false;
		}
		RoomGridCell cell = (RoomGridCell) obj;
		return (this.gridPosition == cell.gridPosition && this.state == cell.state && this.room == cell.room);
	}

	@Override
	public int hashCode() {
		// Use just the gridPosition as a hash so we can keep sets of cells with only one cell per position
		return this.gridPosition.hashCode();
	}
}
