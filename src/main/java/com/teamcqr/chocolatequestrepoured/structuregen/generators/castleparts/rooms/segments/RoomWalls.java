package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;

public class RoomWalls
{
    private EnumMap<EnumFacing, WallOptions> walls;

    private RoomWalls()
    {
        this.walls = new EnumMap<EnumFacing, WallOptions>(EnumFacing.class);
    }

    public void addOuter(EnumFacing side)
    {
        walls.put(side, new WallOptions(false));
    }

    public void addInner(EnumFacing side)
    {
        walls.put(side, new WallOptions(true));
    }

    public void addCenteredDoor(EnumFacing side)
    {
        if (walls.containsKey(side))
        {
            walls.get(side).addDoorCentered();
        }
    }

    public void addRandomDoor(EnumFacing side)
    {
        if (walls.containsKey(side))
        {
            walls.get(side).addDoorRandom();
        }
    }

    public boolean hasWallOnSide(EnumFacing side)
    {
        return walls.containsKey(side);
    }

    public WallOptions getOptionsForSide(EnumFacing side)
    {
        return walls.get(side);
    }
}
