package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomStaircase extends CastleRoom
{
    public CastleRoomStaircase(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        super(startPos, sideLength, height, position);
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
}
