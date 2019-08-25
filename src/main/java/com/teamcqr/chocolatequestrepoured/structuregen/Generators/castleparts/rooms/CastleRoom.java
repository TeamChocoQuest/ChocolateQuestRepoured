package com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.addons.CastleAddonDoor;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Random;

public abstract class CastleRoom
{
    public enum RoomType
    {
        NONE,
        HALLWAY,
        KITCHEN,
        STAIRCASE,
        LANDING
    }

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

        public static final RoomPosition[] VALUES = new RoomPosition[6];

        public boolean isOnSide(EnumFacing facing)
        {
            if (facing == EnumFacing.NORTH)
            {
                return isTop();
            }
            else if (facing == EnumFacing.EAST)
            {
                return isRight();
            }
            else if (facing == EnumFacing.SOUTH)
            {
                return isBottom();
            }
            else if (facing == EnumFacing.WEST)
            {
                return isLeft();
            }
            return false;
        }

        public boolean isTop()
        {
            return (this == TOP_LEFT || this == TOP_MID || this == TOP_RIGHT);
        }

        public boolean isLeft()
        {
            return (this == TOP_LEFT || this == MID_LEFT || this == BOT_LEFT);
        }
        public boolean isBottom()
        {
            return (this == BOT_LEFT || this == BOT_MID || this == BOT_RIGHT);
        }

        public boolean isRight()
        {
            return (this == TOP_RIGHT || this == MID_RIGHT || this == BOT_RIGHT);
        }

        public boolean isOuter()
        {
            return (this != MID);
        }

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
    protected int maxSlotsUsed = 1;
    protected boolean buildNorthWall;
    protected boolean buildEastWall;
    protected boolean buildSouthWall;
    protected boolean buildWestWall;
    protected ArrayList<CastleAddonDoor> doors;
    protected RoomType roomType = RoomType.NONE;
    protected Random random = new Random();

    public CastleRoom(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        this.startPos = startPos;
        this.sideLength = sideLength;
        this.height = height;
        this.position = position;
        this.doors = new ArrayList<>();
        determineWalls(this.position);
    }

    public void generate(ArrayList<BlockPlacement> blocks)
    {
        generateRoom(blocks);
        generateWalls(blocks);
        generateDoors(blocks);
    }

    public abstract void generateRoom(ArrayList<BlockPlacement> blocks);
    public abstract String getNameShortened();

    private void generateDoors(ArrayList<BlockPlacement> blocks)
    {
        for (CastleAddonDoor door : doors)
        {
            door.generate(blocks);
        }
    }

    private void determineWalls(RoomPosition position)
    {
        buildNorthWall = false;
        buildWestWall = false;
        buildSouthWall = true;
        buildEastWall = true;

        if (position.isTop())
        {
            buildNorthWall = true;
        }
        if (position.isLeft())
        {
            buildWestWall = true;
        }
    }

    public void disableWallOnSide(EnumFacing side)
    {
        if (side == EnumFacing.NORTH)
        {
            buildNorthWall = false;
        }
        else if (side == EnumFacing.EAST)
        {
            buildEastWall = false;
        }
        else if (side == EnumFacing.SOUTH)
        {
            buildSouthWall = false;
        }
        else if (side == EnumFacing.WEST)
        {
            buildWestWall = false;
        }
    }

    public void addDoorOnSide(EnumFacing side)
    {
        final int DOOR_WIDTH = 4;
        if (side == EnumFacing.SOUTH && buildSouthWall)
        {
            int xOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
            doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ() + sideLength - 1, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
        }
        else if (side == EnumFacing.EAST && buildEastWall)
        {
            int zOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
            doors.add(new CastleAddonDoor(startPos.getX() + sideLength - 1, startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
        }
        if (side == EnumFacing.NORTH && buildNorthWall)
        {
            int xOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
            doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ(), DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
        }
        else if (side == EnumFacing.WEST && buildWestWall)
        {
            int zOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
            doors.add(new CastleAddonDoor(startPos.getX(), startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
        }
    }

    protected void generateWalls(ArrayList<BlockPlacement> blocks)
    {
        IBlockState wallBlock = Blocks.MOSSY_COBBLESTONE.getDefaultState();
        int len = sideLength;

        if (buildNorthWall)
        {
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

    public void removeWall(EnumFacing side)
    {
        if (side == EnumFacing.NORTH)
        {
            buildNorthWall = false;
        }
        else if (side == EnumFacing.SOUTH)
        {
            buildSouthWall = false;
        }
        else if (side == EnumFacing.EAST)
        {
            buildEastWall = false;
        }
        else if (side == EnumFacing.WEST)
        {
            buildWestWall = false;
        }
    }

    public String getPositionString()
    {
        return position.toString();
    }

    public RoomType getRoomType()
    {
        return roomType;
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

    private void roomDecoTable(BlockPos pos, ArrayList<BlockPlacement> blocks)
    {
        IBlockState legBlock = Blocks.OAK_FENCE.getDefaultState();
        IBlockState topBlock = Blocks.WOODEN_SLAB.getDefaultState();
        topBlock = topBlock.withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
        blocks.add(new BlockPlacement(pos, legBlock));
        blocks.add(new BlockPlacement(pos.add(1, 0, 0), legBlock));
        blocks.add(new BlockPlacement(pos.add(0, 0, 1), legBlock));
        blocks.add(new BlockPlacement(pos.add(1, 0, 1), legBlock));

        blocks.add(new BlockPlacement(pos.add(0, 1, 0), topBlock));
        blocks.add(new BlockPlacement(pos.add(1, 1, 0), topBlock));
        blocks.add(new BlockPlacement(pos.add(0, 1, 1), topBlock));
        blocks.add(new BlockPlacement(pos.add(1, 1, 1), topBlock));
    }

    protected BlockPos getRotatedPlacement(int x, int y, int z, EnumFacing rotation)
    {
        switch (rotation)
        {
            case EAST:
                return startPos.add(z, y, sideLength - 2 - x);
            case WEST:
                return startPos.add(sideLength - 2 - z, y, x);
            case NORTH:
                return startPos.add(sideLength - 2 - x, y, sideLength - 2 - z);
            case SOUTH:
            default:
                return startPos.add(x, y, z);
        }
    }

    protected int getNumYRotationsFromStartToEndFacing(EnumFacing start, EnumFacing end)
    {
        int rotations = 0;
        if (start.getAxis().isHorizontal() && end.getAxis().isHorizontal())
        {
            while (start != end)
            {
                start = start.rotateY();
                rotations++;
            }
        }
        return rotations;
    }

    protected EnumFacing rotateFacingNTimesAboutY(EnumFacing facing, int n)
    {
        for (int i = 0; i < n; i++)
        {
            facing = facing.rotateY();
        }
        return facing;
    }
}
