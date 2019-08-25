package com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomHallway extends CastleRoom
{
    public CastleRoomHallway(BlockPos startPos, int sideLength, int height, RoomPosition position, boolean vertical)
    {
        super(startPos, sideLength, height, position);
        this.roomType = RoomType.HALLWAY;
        if (vertical)
        {
            buildNorthWall = position.isTop();
            buildSouthWall = position.isBottom();
        }
        else
        {
            buildWestWall = position.isLeft();
            buildEastWall = position.isRight();
        }
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        for (int z = 0; z < (buildSouthWall ? sideLength - 1 : sideLength); z++)
        {
            for (int x = 0; x < (buildEastWall ? sideLength - 1 : sideLength); x++)
            {
                BlockPos pos = startPos.add(x, 0, z);
                blocks.add(new BlockPlacement(pos, Blocks.WOOL.getDefaultState()));
            }
        }
        generateWalls(blocks);
    }

    @Override
    public String getNameShortened()
    {
        return "HLL";
    }
}
