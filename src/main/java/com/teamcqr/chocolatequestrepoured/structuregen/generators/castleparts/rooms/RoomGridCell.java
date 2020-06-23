package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.CastleMainStructWall;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.EnumCastleDoorType;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.*;

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

        @Override
        public String toString() {
            return text;
        }

        private boolean isAtLeast(CellState state) {
            return this.value >= state.value;
        }

        private boolean isLessThan(CellState state) {
            return this.value < state.value;
        }
    }

    private final RoomGridPosition gridPosition;
    private final BlockPos originOffset;
    private CellState state = CellState.UNUSED;
    private boolean reachable = false;
    private boolean floorHasLanding = false;
    private boolean partOfMainStruct = false;
    private CastleRoomBase room = null;
    private HashSet<RoomGridCell> connectedCells; // all cells near this one that have the same type
    private HashSet<RoomGridCell> pathableCells; // cells on the same floor that are potentially reachable
    private boolean isBossArea = false;
    private HashMap<EnumFacing, RoomGridCell> adjacentCells = new HashMap<>();
    private HashMap<EnumFacing, CastleMainStructWall> walls = new HashMap<>();

    public RoomGridCell(int floor, int x, int z, int roomWidth, int floorHeight) {
        this.gridPosition = new RoomGridPosition(floor, x, z);
        this.originOffset = calculateOriginOffset(roomWidth, floorHeight);
        this.connectedCells = new HashSet<>();
        this.pathableCells = new HashSet<>();
    }

    private BlockPos calculateOriginOffset(int roomWidth, int floorHeight)
    {
        int xOffset = 1 + (gridPosition.getX() * (roomWidth + 1));
        int zOffset = 1 + (gridPosition.getZ()  * (roomWidth + 1));
        int yOffset = gridPosition.getFloor() * floorHeight;
        return new BlockPos(xOffset, yOffset, zOffset);
    }

    public BlockPos getOriginOffset() {
        return originOffset;
    }

    public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
        if (this.isPopulated() && this.room != null) {
            room.setRoomOrigin(getOriginOffset());
            room.registerWalls(this.walls);
            room.generate(castleOrigin, genArray, dungeon);
        }
    }

    public void registerAdjacentCell(RoomGridCell cell, EnumFacing directionOfCell) {
        adjacentCells.put(directionOfCell, cell);
    }

    public Optional<RoomGridCell> getAdjacentCell(EnumFacing direction)
    {
        if (adjacentCells.containsKey(direction))
        {
            return Optional.of(adjacentCells.get(direction));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void registerAdjacentWall(CastleMainStructWall wall, EnumFacing directionOfWall) {
        walls.put(directionOfWall, wall);
    }

    public Optional<CastleMainStructWall> getAdjacentWall(EnumFacing direction)
    {
        if (walls.containsKey(direction))
        {
            return Optional.of(walls.get(direction));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setAllLinkedReachable(List<RoomGridCell> unreachableCells, List<RoomGridCell> reachableCells) {
        this.reachable = true;

        for (RoomGridCell linkedCell : this.getConnectedCellsCopy()) {
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

    public boolean isMainStruct() {
        return this.partOfMainStruct;
    }

    public void setBuildable() {
        if (this.state.isLessThan(CellState.BUILDABLE)) {
            this.state = CellState.BUILDABLE;
        }
    }

    public boolean isBuildable() {
        return (this.state.isAtLeast(CellState.BUILDABLE));
    }

    public boolean isNotSelected() {
        return (this.state.isLessThan(CellState.SELECTED));
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

    public CastleRoomBase getRoom() {
        return this.room;
    }

    public void setRoom(CastleRoomBase room) {
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

    public void setBossRoomCell() {
        this.isBossArea = true;
    }

    public boolean isBossArea() {
        return this.isBossArea;
    }

    public void connectToCell(RoomGridCell cell) {
        this.connectedCells.add(cell);
    }

    public void connectToCells(Collection<RoomGridCell> cell) {
        this.connectedCells.addAll(cell);
    }

    public void setConnectedCells(HashSet<RoomGridCell> cells) {
        this.connectedCells = new HashSet<>(cells);
    }

    public HashSet<RoomGridCell> getConnectedCellsCopy() {
        return new HashSet<>(this.connectedCells); // return a copy
    }

    public boolean isConnectedToCell(RoomGridCell cell) {
        return this.connectedCells.contains(cell);
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

    public void addDoorOnSideCentered(EnumFacing side, EnumCastleDoorType type, Random random) {
        if (walls.containsKey(side)) {
            walls.get(side).addDoorCentered(type, random);
        }
    }

    public void addDoorOnSideRandomOffset(EnumFacing side, EnumCastleDoorType type, Random random) {
        if (walls.containsKey(side)) {
            walls.get(side).addDoorRandomOffset(type, random);
        }
    }

    public void addOuterWall(EnumFacing side) {
        if (walls.containsKey(side)) {
            walls.get(side).enable();
            walls.get(side).setAsOuterWall();
        }
    }

    public void addRoofEdgeWall(EnumFacing side) {
        if (walls.containsKey(side)) {
            walls.get(side).enable();
            walls.get(side).setAsOuterWall();
            walls.get(side).setAsRoofEdge();
        }
    }

    public void addInnerWall(EnumFacing side) {
        if (walls.containsKey(side)) {
            walls.get(side).enable();
            walls.get(side).setAsInnerWall();
        }
    }

    public void removeWall(EnumFacing side) {
        if (walls.containsKey(side)) {
            walls.get(side).disable(); //don't actually delete the wall just don't build it
        }
    }

    public void copyRoomPropertiesToConnectedCells() {
        ArrayList<CastleRoomBase> connectedRooms = new ArrayList<>();
        connectedCells.stream().filter(RoomGridCell::isPopulated).forEach(c -> connectedRooms.add(c.getRoom()));
        for (CastleRoomBase connectedRoom : connectedRooms) {
            connectedRoom.copyPropertiesOf(this.room);
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
