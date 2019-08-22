package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

public class RoomSelection
{
    private RoomGridPosition gridPosition;
    private boolean populated;
    private boolean reachable;
    private CastleRoom room;

    public RoomSelection(RoomGridPosition gridPosition, CastleRoom room)
    {
        this.gridPosition = gridPosition;
        this.populated = true;
        this.reachable = false;
        this.room = room;
    }

    public RoomSelection(int floor, int x, int z, CastleRoom room)
    {
        this.gridPosition = new RoomGridPosition(floor, x, z);
        this.populated = true;
        this.reachable = false;
        this.room = room;
    }

    public RoomSelection(RoomGridPosition gridPosition)
    {
        this.gridPosition = gridPosition;
        this.populated = false;
        this.reachable = false;
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

    public void setPopulated()
    {
        populated = true;
    }

    public boolean isPopulated()
    {
        return populated;
    }

    public CastleRoom getRoom()
    {
        return room;
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
