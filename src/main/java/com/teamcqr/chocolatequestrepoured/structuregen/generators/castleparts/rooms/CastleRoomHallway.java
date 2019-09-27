package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomHallway extends CastleRoom
{
    public enum Alignment
    {
        VERTICAL (0),
        HORIZONTAL (1);

        private final int value;

        private Alignment(int value)
        {
            this.value = value;
        }

        private boolean requiresDoor(EnumFacing side)
        {
            if (this.value == 0)
            {
                return (side == EnumFacing.WEST || side == EnumFacing.EAST);
            }
            else
            {
                return (side == EnumFacing.NORTH || side == EnumFacing.SOUTH);
            }
        }
    }



    private Alignment alignment;

    public CastleRoomHallway(BlockPos startPos, int sideLength, int height, Alignment alignment)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.HALLWAY;
        this.alignment = alignment;
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        for (int z = 0; z < (hasWallOnSide(EnumFacing.SOUTH) ? sideLength - 1 : sideLength); z++)
        {
            for (int x = 0; x < (hasWallOnSide(EnumFacing.EAST) ? sideLength - 1 : sideLength); x++)
            {
                BlockPos pos = startPos.add(x, 0, z);
                blocks.add(new BlockPlacement(pos, Blocks.WOOL.getDefaultState()));
            }
        }
        generateWalls(blocks);
    }

    @Override
    public void addWall(EnumFacing side, boolean force)
    {
        if (force || alignment.requiresDoor(side))
        {
            this.walls.add(side);
        }
    }

    @Override
    public String getNameShortened()
    {
        return "HLL";
    }
}