package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;
import java.util.Random;

public class RoomWalls
{
    private EnumMap<EnumFacing, WallOptions> walls;

    public RoomWalls()
    {
        this.walls = new EnumMap<EnumFacing, WallOptions>(EnumFacing.class);
    }

    public void addOuter(EnumFacing side)
    {
        walls.put(side, new WallOptions(true));
    }

    public void addInner(EnumFacing side)
    {
        walls.put(side, new WallOptions(false));
    }

    public DoorPlacement addCenteredDoor(int wallLength, EnumFacing side)
    {
        if (walls.containsKey(side))
        {
            int offset = (wallLength - DoorPlacement.DEFAULT_WIDTH) / 2;
            DoorPlacement door = new DoorPlacement(offset, side);
            walls.get(side).addDoor(door);
            return door;
        }
        return null;
    }

    public DoorPlacement addRandomDoor(Random random, int wallLength, EnumFacing side)
    {
        if (walls.containsKey(side))
        {
            int offset = 1 + random.nextInt(wallLength - DoorPlacement.DEFAULT_WIDTH - 1);
            DoorPlacement door = new DoorPlacement(offset, side);
            walls.get(side).addDoor(door);
            return door;
        }
        return null;
    }

    public boolean hasWallOnSide(EnumFacing side)
    {
        return walls.containsKey(side);
    }

    public boolean hasDoorOnside(EnumFacing side)
    {
        if (walls.containsKey(side))
        {
            return walls.get(side).hasDoor();
        }
        else
        {
            return false;
        }
    }

    public WallOptions getOptionsForSide(EnumFacing side)
    {
        return walls.get(side);
    }

    public void removeWall(EnumFacing side)
    {
        walls.remove(side);
    }
}
