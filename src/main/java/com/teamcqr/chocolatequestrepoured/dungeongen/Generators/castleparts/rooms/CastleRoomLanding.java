package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomLanding extends CastleRoom
{
    public CastleRoomLanding(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        super(startPos, sideLength, height, position);
        this.roomType = RoomType.LANDING;
    }

    @Override
    public void generate(ArrayList<BlockPlacement> blocks)
    {
        return;
    }

    @Override
    public String getNameShortened()
    {
        return "LAN";
    }
}
