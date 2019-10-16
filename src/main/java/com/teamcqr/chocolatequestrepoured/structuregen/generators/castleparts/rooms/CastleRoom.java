package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons.CastleAddonDoor;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public abstract class CastleRoom
{
    public enum RoomType
    {
        NONE(0, "None", false),
        HALLWAY(1, "Hallway", false),
        KITCHEN(2, "Kitchen", false),
        STAIRCASE_DIRECTED(3, "Directed Stairs", true),
        STAIRCASE_SPIRAL(4, "Spiral Stairs", true),
        LANDING_DIRECTED(5, "Directed Landing", true),
        LANDING_SPIRAL(6, "Spiral Landng", true),
        TOWER_SQUARE(7, "Square Tower", false);

        private final int index;
        private final String name;
        private final boolean partOfStairs;

        RoomType(int indexIn, String nameIn, boolean partOfStairsIn)
        {
            this.index = indexIn;
            this.name = nameIn;
            this.partOfStairs = partOfStairsIn;
        }

        public boolean isPartOfStairs()
        {
            return this.partOfStairs;
        }
    }

    BlockPos startPos;
    protected int height;
    protected int sideLength;

    //The following variables are used for rooms that build blocks in a smaller area than the
    //actual room occupies (such as towers). For most room types they will be not be changed from
    //the values set in the default constructor.
    protected int buildLength; //actual length of constructed part of room
    protected int offsetX; //x offset from startPos that actual room starts
    protected int offsetZ; //z offset from startPos that actual room starts

    //The counts represent how many roomSizes this room uses in a given direction
    //so for example if countX was 2, the actual x size would be x*roomSize
    protected int countX;
    protected int countY;
    protected int countZ;

    protected int maxSlotsUsed = 1; //Max number of contiguous room grid slots this can occupy

    protected ArrayList<CastleAddonDoor> doors;
    protected RoomType roomType = RoomType.NONE;
    protected Random random = new Random();

    protected HashSet<EnumFacing> walls;
    protected HashSet<EnumFacing> roofEdges;
    protected HashSet<EnumFacing> doorSides;

    public CastleRoom(BlockPos startPos, int sideLength, int height)
    {
        this.startPos = startPos;
        this.sideLength = sideLength;
        this.offsetX = 0;
        this.offsetZ = 0;
        this.buildLength = this.sideLength;
        this.height = height;
        this.doors = new ArrayList<>();
        this.walls = new HashSet<>();
        this.roofEdges = new HashSet<>();
        this.doorSides = new HashSet<>();
    }

    public void generate(ArrayList<BlockPlacement> blocks)
    {
        generateRoom(blocks);
        generateWalls(blocks);
        buildRoofEdges(blocks);
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

    public void addDoorOnSideRandom(EnumFacing side)
    {
        if (!doorSides.contains(side))
        {
            doorSides.add(side);

            final int DOOR_WIDTH = 4;
            if (side == EnumFacing.SOUTH && hasWallOnSide(EnumFacing.SOUTH))
            {
                int xOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
                doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ() + sideLength - 1, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
            }
            else if (side == EnumFacing.EAST && hasWallOnSide(EnumFacing.EAST))
            {
                int zOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
                doors.add(new CastleAddonDoor(startPos.getX() + sideLength - 1, startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
            }
            if (side == EnumFacing.NORTH && hasWallOnSide(EnumFacing.NORTH))
            {
                int xOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
                doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ(), DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
            }
            else if (side == EnumFacing.WEST && hasWallOnSide(EnumFacing.WEST))
            {
                int zOffset = random.nextInt(sideLength - 1 - DOOR_WIDTH);
                doors.add(new CastleAddonDoor(startPos.getX(), startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
            }
        }
    }

    public void addDoorOnSideCentered(EnumFacing side)
    {
        doorSides.add(side);

        final int DOOR_WIDTH = 4;
        if (side == EnumFacing.SOUTH && hasWallOnSide(EnumFacing.SOUTH))
        {
            int xOffset = (sideLength - DOOR_WIDTH) / 2;
            doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ() + sideLength - 1, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
        }
        else if (side == EnumFacing.EAST && hasWallOnSide(EnumFacing.EAST))
        {
            int zOffset = (sideLength - DOOR_WIDTH) / 2;
            doors.add(new CastleAddonDoor(startPos.getX() + sideLength - 1, startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
        }
        if (side == EnumFacing.NORTH && hasWallOnSide(EnumFacing.NORTH))
        {
            int xOffset = (sideLength - DOOR_WIDTH) / 2;
            doors.add(new CastleAddonDoor(startPos.getX() + xOffset, startPos.getY() + 1, startPos.getZ(), DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, true));
        }
        else if (side == EnumFacing.WEST && hasWallOnSide(EnumFacing.WEST))
        {
            int zOffset = (sideLength - DOOR_WIDTH) / 2;
            doors.add(new CastleAddonDoor(startPos.getX(), startPos.getY() + 1, startPos.getZ() + zOffset, DOOR_WIDTH, 3, CastleAddonDoor.DoorType.FENCE_SPRUCE_BORDER, false));
        }
    }

    protected void generateWalls(ArrayList<BlockPlacement> blocks)
    {
        IBlockState wallBlock = Blocks.STONEBRICK.getDefaultState();
        int hgt = height;

        if (walls.contains(EnumFacing.NORTH))
        {
            for (int x = 0; x < buildLength; x++)
            {
                for (int y = 0; y < hgt; y++)
                {
                    BlockPos pos = startPos.add(x + offsetX, y, offsetZ);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }

            }
        }
        if (walls.contains(EnumFacing.SOUTH))
        {
            for (int x = 0; x < buildLength; x++)
            {
                for (int y = 0; y < hgt; y++)
                {
                    BlockPos pos = startPos.add(x + offsetX, y, buildLength + offsetZ - 1);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
        if (walls.contains(EnumFacing.WEST))
        {
            for (int z = 0; z < buildLength; z++)
            {
                for (int y = 0; y < hgt; y++)
                {
                    BlockPos pos = startPos.add(offsetX, y, z + offsetZ);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
        if (walls.contains(EnumFacing.EAST))
        {
            for (int z = 0; z < buildLength; z++)
            {
                for (int y = 0; y < hgt; y++)
                {
                    BlockPos pos = startPos.add(buildLength + offsetX - 1, y, z + offsetZ);
                    blocks.add(new BlockPlacement(pos, wallBlock));
                }
            }
        }
    }

    public void addWall(EnumFacing side, boolean force)
    {
        walls.add(side);
    }

    public void removeWall(EnumFacing side)
    {
        walls.remove(side);
    }

    public boolean hasDoorOnSide(EnumFacing side)
    {
        return doorSides.contains(side);
    }

    public boolean hasWallOnSide(EnumFacing side)
    {
        return (walls.contains(side));
    }

    public void addRoofEdge(EnumFacing side)
    {
        roofEdges.add(side);
    }

    public boolean canBuildDoorOnSide(EnumFacing side)
    {
        return hasWallOnSide(side);
    }

    public boolean reachableFromSide(EnumFacing side)
    {
        return true;
    }

    public boolean hasRoofEdgeOnSide(EnumFacing side)
    {
        return (roofEdges.contains(side));
    }

    public boolean isTower()
    {
        return false;
    }

    protected void buildRoofEdges(ArrayList<BlockPlacement> blocks)
    {
        IBlockState wallBlock = Blocks.STONEBRICK.getDefaultState();
        int len = buildLength;

        if (hasRoofEdgeOnSide(EnumFacing.NORTH))
        {
            for (int x = 0; x < len; x++)
            {
                BlockPos pos = startPos.add(x + offsetX, height, offsetZ);
                blocks.add(new BlockPlacement(pos, wallBlock));
                if (shouldBuildCrenellation(len, x))
                {
                    blocks.add(new BlockPlacement(pos.up(), wallBlock));
                }
            }
        }
        if (hasRoofEdgeOnSide(EnumFacing.SOUTH))
        {
            for (int x = 0; x < len; x++)
            {
                BlockPos pos = startPos.add(x + offsetX, height, offsetZ + buildLength - 1);
                blocks.add(new BlockPlacement(pos, wallBlock));
                if (shouldBuildCrenellation(len, x))
                {
                    blocks.add(new BlockPlacement(pos.up(), wallBlock));
                }
            }
        }
        if (hasRoofEdgeOnSide(EnumFacing.WEST))
        {
            for (int z = 0; z < len; z++)
            {
                BlockPos pos = startPos.add(offsetX, height, z + offsetZ);
                blocks.add(new BlockPlacement(pos, wallBlock));
                if (shouldBuildCrenellation(len, z))
                {
                    blocks.add(new BlockPlacement(pos.up(), wallBlock));
                }
            }
        }
        if (hasRoofEdgeOnSide(EnumFacing.EAST))
        {
            for (int z = 0; z < len; z++)
            {
                BlockPos pos = startPos.add(offsetX + buildLength - 1, height, z + offsetZ);
                blocks.add(new BlockPlacement(pos, wallBlock));
                if (shouldBuildCrenellation(len, z))
                {
                    blocks.add(new BlockPlacement(pos.up(), wallBlock));
                }
            }
        }
    }

    private boolean shouldBuildCrenellation(int wallLength, int index)
    {
        return (index == 0 || index == wallLength - 1 || index % 2 == 0);
    }

    public RoomType getRoomType()
    {
        return roomType;
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
