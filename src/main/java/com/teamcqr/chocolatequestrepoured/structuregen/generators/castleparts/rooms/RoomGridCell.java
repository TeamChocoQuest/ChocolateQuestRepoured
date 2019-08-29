package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

public class RoomGridCell
{
    private enum CellState
    {
        UNUSED (0),       //empty and cannot build anything on this space
        BUILDABLE (1),    //empty but able to build on this space
        SELECTED (2),     //selected for building but not filled with a room
        POPULATED (3);    //filled with a room

        private final int value;

        CellState(int value)
        {
            this.value = value;
        }

        private boolean isAtLeast(CellState state)
        {
            return value >= state.value;
        }

        private boolean isLessThan(CellState state)
        {
            return value < state.value;
        }
    }

    private RoomGridPosition gridPosition;
    private CellState state;
    private boolean reachable;
    private boolean partOfMainStruct;
    private CastleRoom room;

    public RoomGridCell(int floor, int x, int z, CastleRoom room)
    {
        this.gridPosition = new RoomGridPosition(floor, x, z);
        this.state = CellState.UNUSED;
        this.reachable = false;
        this.partOfMainStruct = false;
        this.room = room;
    }

    public RoomGridCell(int floor, int x, int z)
    {
        this.gridPosition = new RoomGridPosition(floor, x, z);
        this.state = CellState.UNUSED;
        this.reachable = false;
        this.partOfMainStruct = false;
        this.room = null;
    }

    public RoomGridCell(RoomGridPosition gridPosition, CastleRoom room)
    {
        this.gridPosition = gridPosition;
        this.state = CellState.UNUSED;
        this.reachable = false;
        this.partOfMainStruct = false;
        this.room = room;
    }

    public RoomGridCell(RoomGridPosition gridPosition)
    {
        this.gridPosition = gridPosition;
        this.state = CellState.UNUSED;
        this.reachable = false;
        this.partOfMainStruct = false;
        this.room = null;
    }

    public void setReachable()
    {
        this.reachable = true;
    }

    public boolean isReachable()
    {
        return reachable;
    }

    public void setAsMainStruct()
    {
        partOfMainStruct = true;
    }

    public boolean isMainStruct()
    {
        return partOfMainStruct;
    }

    public void setBuildable()
    {
        if (state.isLessThan(CellState.BUILDABLE))
        {
            state = CellState.BUILDABLE;
        }
    }

    public boolean isBuildable()
    {
        return (state.isAtLeast(CellState.BUILDABLE));
    }

    public void selectForBuilding()
    {
        if (state.isLessThan(CellState.SELECTED))
        {
            state = CellState.SELECTED;
        }
    }

    public boolean isSelectedForBuilding()
    {
        return (state.isAtLeast(CellState.SELECTED));
    }

    public boolean isPopulated()
    {
        return (state.isAtLeast(CellState.POPULATED));
    }

    public boolean needsRoomType()
    {
        return (state == CellState.SELECTED);
    }

    public CastleRoom getRoom()
    {
        return room;
    }

    public void setRoom(CastleRoom room)
    {
        this.room = room;
        state = CellState.POPULATED;
    }

    public RoomGridPosition getGridPosition()
    {
        return gridPosition;
    }

    public int getFloor()
    {
        return this.gridPosition.getFloor();
    }

    public int getGridX()
    {
        return this.gridPosition.getX();
    }

    public int getGridZ()
    {
        return this.gridPosition.getZ();
    }
}
