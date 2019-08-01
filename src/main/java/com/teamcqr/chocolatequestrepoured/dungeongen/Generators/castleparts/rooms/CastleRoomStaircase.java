package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomStaircase extends CastleRoom
{
    private EnumFacing doorSide;

    public CastleRoomStaircase(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        super(startPos, sideLength, height, position);
        this.roomType = RoomType.STAIRCASE;
        this.doorSide = EnumFacing.NORTH;
    }

    @Override
    public void generate(ArrayList<BlockPlacement> blocks)
    {
        return;
    }

    @Override
    public String getNameShortened()
    {
        return "STR";
    }

    private void setDoorSide(EnumFacing side)
    {
        this.doorSide = side;
    }
}
