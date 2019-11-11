package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;
import java.util.Random;

public class RoomWalls
{
    //The wall settings for this room
    private EnumMap<EnumFacing, WallOptions> walls;

    //A map of adjacent room doors that lead into this room
    private EnumMap<EnumFacing, DoorPlacement> adjacentDoors;

    public RoomWalls()
    {
        this.walls = new EnumMap<EnumFacing, WallOptions>(EnumFacing.class);
        this.adjacentDoors = new EnumMap<EnumFacing, DoorPlacement>(EnumFacing.class);
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
            DoorPlacement door = new DoorPlacement(offset, DoorPlacement.DEFAULT_WIDTH, DoorPlacement.DEFAULT_HEIGHT);
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
            DoorPlacement door = new DoorPlacement(offset, DoorPlacement.DEFAULT_WIDTH, DoorPlacement.DEFAULT_HEIGHT);
            walls.get(side).addDoor(door);
            return door;
        }
        return null;
    }

    public boolean hasWallOnSide(EnumFacing side)
    {
        return walls.containsKey(side);
    }

    public boolean hasDoorOnSide(EnumFacing side)
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

    public DoorPlacement getDoorOnSide(EnumFacing side)
    {
        if (walls.containsKey(side) && walls.get(side).hasDoor())
        {
            return walls.get(side).getDoor();
        }
        else
        {
            return null;
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

    public void registerAdjacentDoor(EnumFacing side, DoorPlacement door)
    {
        adjacentDoors.put(side, door);
    }

    public boolean adjacentRoomHasDoorOnSide(EnumFacing side)
    {
        return adjacentDoors.containsKey(side);
    }

    public DoorPlacement getAdjacentDoor(EnumFacing side)
    {
        return adjacentDoors.get(side);
    }
}
