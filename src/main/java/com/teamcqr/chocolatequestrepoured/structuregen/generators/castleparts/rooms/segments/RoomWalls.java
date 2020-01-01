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
        this.walls = new EnumMap<>(EnumFacing.class);
        this.adjacentDoors = new EnumMap<>(EnumFacing.class);
    }

    public void addOuter(EnumFacing side)
    {
        walls.put(side, new WallOptions(true));
    }

    public void addInner(EnumFacing side)
    {
        walls.put(side, new WallOptions(false));
    }

    public DoorPlacement addCenteredDoor(Random random, int wallLength, EnumFacing side, EnumCastleDoorType type)
    {

        if (type == EnumCastleDoorType.RANDOM)
        {
            type = EnumCastleDoorType.getRandomRegularType(random);
        }

        int offset = (wallLength - type.getWidth()) / 2;

        return addDoorWithOffset(side, offset, type);
    }

    public DoorPlacement addRandomDoor(Random random, int wallLength, EnumFacing side, EnumCastleDoorType type)
    {
        if (type == EnumCastleDoorType.RANDOM)
        {
            type = EnumCastleDoorType.getRandomRegularType(random);
        }

        int offset = 1 + random.nextInt(wallLength - type.getWidth() - 1);

        return addDoorWithOffset(side, offset, type);
    }

    private DoorPlacement addDoorWithOffset(EnumFacing side, int offset, EnumCastleDoorType type)
    {
        if (walls.containsKey(side))
        {
            DoorPlacement door = new DoorPlacement(offset, type);
            walls.get(side).addDoor(door);
            return door;
        }
        else
        {
            return null;
        }
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
