package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public abstract class CastleRoom
{
    public enum RoomPosition
    {
        TOP_LEFT,
        TOP_MID,
        TOP_RIGHT,
        MID_LEFT,
        MID,
        MID_RIGHT,
        BOT_LEFT,
        BOT_MID,
        BOT_RIGHT;

        @Override
        public String toString()
        {
            switch (this)
            {
                case TOP_LEFT: return "TL";
                case TOP_MID: return "TM";
                case TOP_RIGHT: return "TR";
                case MID_LEFT: return "ML";
                case MID: return "MM";
                case MID_RIGHT: return "MR";
                case BOT_LEFT: return "BL";
                case BOT_MID: return "BM";
                case BOT_RIGHT: return "BR";
                default: return "XX";
            }
        }
    }

    private static final String[] roomPositionStrings = {
            "MID_LEFT",
            "MID",
            "MID_RIGHT",
            "BOT_LEFT",
            "BOT_MID",
            "BOT_RIGHT"
    };


    protected BlockPos startPos;
    protected int height;
    protected int sideLength;
    protected RoomPosition position;

    //the counts represent how many roomSizes this room uses in a given direction
    //so for example if countX was 2, the actual x size would be x*roomSize
    protected int countX;
    protected int countY;
    protected int countZ;
    protected boolean buildNorthWall;
    protected boolean buildEastWall;
    protected boolean buildSouthWall;
    protected boolean buildWestWall;

    public CastleRoom(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        this.startPos = startPos;
        this.sideLength = sideLength;
        this.height = height;
        this.position = position;
        determineWalls(this.position);
    }

    public abstract void generate(ArrayList<BlockPlacement> blocks);
    public abstract String getNameShortened();

    private void determineWalls(RoomPosition position)
    {
        buildNorthWall = false;
        buildEastWall = false;
        buildSouthWall = false;
        buildWestWall = false;

        switch (position)
        {
            case TOP_LEFT:
            case TOP_MID:
            case MID_LEFT:
            case MID:
                buildEastWall = true;
                buildSouthWall = true;
                break;
            case TOP_RIGHT:
            case MID_RIGHT:
                buildSouthWall = true;
                break;
            case BOT_LEFT:
            case BOT_MID:
                buildEastWall = true;
                break;
            case BOT_RIGHT:
            default:
                break;
        }
    }

    protected void addWalls(ArrayList<BlockPlacement> blocks)
    {
        IBlockState wallBlock = Blocks.MOSSY_COBBLESTONE.getDefaultState();

        if (buildNorthWall)
        {
            int len = isRightRow() ? sideLength - 1 : sideLength;
            for (int x = 0; x < len; x++)
            {
                for (int y = 0; y < height / 2; y++)
                {
                    BlockPos pos = startPos.add(x, y, 0);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }

            }
        }
        if (buildSouthWall)
        {
            int len = isRightRow() ? sideLength - 1 : sideLength;
            for (int x = 0; x < len; x++)
            {
                for (int y = 0; y < height / 2; y++)
                {
                    BlockPos pos = startPos.add(x, y, sideLength - 1);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
        if (buildWestWall)
        {
            int len = isBottomRow() ? sideLength - 1 : sideLength;
            for (int z = 0; z < len; z++)
            {
                for (int y = 0; y < height / 2; y++)
                {
                    BlockPos pos = startPos.add(0, y, z);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
        if (buildEastWall)
        {
            int len = isBottomRow() ? sideLength - 1 : sideLength;
            for (int z = 0; z < len; z++)
            {
                for (int y = 0; y < height / 2; y++)
                {
                    BlockPos pos = startPos.add(sideLength - 1, y, z);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
    }

    public String getPositionString()
    {
        return position.toString();
    }

    private boolean isBottomRow()
    {
        return (position == RoomPosition.BOT_LEFT ||
                position == RoomPosition.BOT_MID ||
                position == RoomPosition.BOT_RIGHT);
    }

    private boolean isRightRow()
    {
        return (position == RoomPosition.TOP_RIGHT ||
                position == RoomPosition.MID_RIGHT ||
                position == RoomPosition.BOT_RIGHT);
    }
}
