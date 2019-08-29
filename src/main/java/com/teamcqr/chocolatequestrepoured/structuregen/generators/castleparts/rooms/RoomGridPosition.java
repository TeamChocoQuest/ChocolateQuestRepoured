package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

public class RoomGridPosition
{
    private int floor;
    private int x;
    private int z;

    public RoomGridPosition(int floor, int x, int z)
    {
        this.floor = floor;
        this.x = x;
        this.z = z;
    }

    public int getFloor()
    {
        return floor;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }
}
