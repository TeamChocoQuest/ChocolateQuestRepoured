package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;

import java.util.ArrayList;

public abstract class CastleRoom
{
    public enum RoomPosition
    {
        TOP_LEFT,
        TOP_MID,
        TOP_RIGHT,
        LEFT_MID,
        MID,
        RIGHT_MID,
        BOT_LEFT,
        BOT_MID,
        BOT_RIGHT
    }
    private int startX;
    private int startY;
    private int startZ;
    private int roomSize;

    //the counts represent how many roomSizes this room uses in a given direction
    //so for example if countX was 2, the actual x size would be x*roomSize
    private int countX;
    private int countY;
    private int countZ;
    private boolean buildNorthWall;
    private boolean buildEastWall;
    private boolean buildSouthWall;
    private boolean buildWestWall;

    public abstract void generate(ArrayList<BlockPlacement> blocks);
}
