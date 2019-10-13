package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import net.minecraft.util.EnumFacing;

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

    public RoomGridPosition move(EnumFacing direction)
    {
        int floor = this.getFloor();
        int x = this.getX();
        int z = this.getZ();

        switch (direction)
        {
            case NORTH:
                z--;
                break;
            case SOUTH:
                z++;
                break;
            case WEST:
                x--;
                break;
            case EAST:
                x++;
                break;
            case UP:
                floor++;
                break;
            case DOWN:
                floor--;
                break;
            default:
                break;
        }

        return new RoomGridPosition(floor, x, z);
    }
}
