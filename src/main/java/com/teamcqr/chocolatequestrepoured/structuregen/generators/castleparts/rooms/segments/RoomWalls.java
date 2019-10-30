package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.EnumMap;

public class RoomWalls
{
    private EnumMap<EnumFacing, WallOptions> walls;

    public RoomWalls()
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

    public void generate(BlockPos startPos, int height, ArrayList<BlockPlacement> blocks)
    {

    }


}
