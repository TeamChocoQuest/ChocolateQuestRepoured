package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWallBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.RoomWalls;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public abstract class CastleRoom
{
    public enum RoomType
    {
        NONE("None", false),
        WALKABLE_ROOF("Walkablke Roof", false),
        HALLWAY("Hallway", false),
        KITCHEN("Kitchen", false),
        STAIRCASE_DIRECTED("Directed Stairs", true),
        STAIRCASE_SPIRAL("Spiral Stairs", true),
        LANDING_DIRECTED("Directed Landing", true),
        LANDING_SPIRAL("Spiral Landing", true),
        TOWER_SQUARE("Square Tower", false),
        ALCHEMY_LAB("Alchemy Lab", false),
        BEDROOM("Bedroom", false),
        ARMORY("Armory", false);

        private final String name;
        private final boolean partOfStairs;

        RoomType(String nameIn, boolean partOfStairsIn)
        {
            this.name = nameIn;
            this.partOfStairs = partOfStairsIn;
        }

        public boolean isPartOfStairs()
        {
            return this.partOfStairs;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    protected BlockPos startPos;
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

    protected boolean isTower = false;
    protected boolean pathable = true;

    protected RoomType roomType = RoomType.NONE;
    protected boolean defaultCeiling = false;
    protected boolean defaultFloor = false;
    protected Random random = new Random();

    protected RoomWalls walls;
    protected HashSet<BlockPos> decoMap;
    protected HashSet<BlockPos> decoArea;

    public CastleRoom(BlockPos startPos, int sideLength, int height)
    {
        this.startPos = startPos;
        this.sideLength = sideLength;
        this.offsetX = 0;
        this.offsetZ = 0;
        this.buildLength = this.sideLength;
        this.height = height;
        this.walls = new RoomWalls();
        this.decoMap = new HashSet<>();
        this.decoArea = new HashSet<>();
    }

    public void generate(World world, CastleDungeon dungeon)
    {
        generateRoom(world, dungeon);
        generateWalls(world, dungeon);

        if (defaultFloor)
        {
            generateDefaultFloor(world, dungeon);
        }
        if (defaultCeiling)
        {
            generateDefaultCeiling(world, dungeon);
        }
    }

    protected abstract void generateRoom(World world, CastleDungeon dungeon);

    public void decorate(World world, CastleDungeon dungeon)
    {
        ; //Default is no decoration
    }

    protected void generateWalls(World world, CastleDungeon dungeon)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            if (walls.hasWallOnSide(side))
            {
                BlockPos buildPos = getbuildPosition();
                RoomWallBuilder builder = new RoomWallBuilder(buildPos, height, buildLength, walls.getOptionsForSide(side), side);
                builder.generate(world, dungeon);
            }
        }
    }

    public boolean canBuildDoorOnSide(EnumFacing side)
    {
        return true;
    }

    public boolean canBuildInnerWallOnSide(EnumFacing side) { return true; }

    public boolean reachableFromSide(EnumFacing side)
    {
        return true;
    }

    public boolean isTower()
    {
        return isTower;
    }

    public boolean isPathable()
    {
        return pathable;
    }

    protected void generateDefaultCeiling(World world, CastleDungeon dungeon)
    {
        for (int z = 0; z < buildLength - 1; z++)
        {
            for (int x = 0; x < buildLength - 1; x++)
            {
                world.setBlockState(startPos.add( x, height - 1, z), dungeon.getWallBlock().getDefaultState());
            }
        }
    }

    protected void generateDefaultFloor(World world, CastleDungeon dungeon)
    {
        BlockPos pos = getNonWallStartPos();

        for (int z = 0; z < getDecorationLengthZ(); z++)
        {
            for (int x = 0; x < getDecorationLengthX(); x++)
            {
                world.setBlockState(pos.add( x, 0, z), dungeon.getFloorBlock().getDefaultState());
            }
        }
    }

    public RoomType getRoomType()
    {
        return roomType;
    }

    public BlockPos getRoofStartPosition()
    {
        return startPos.add(offsetX, height, offsetZ);
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

    protected BlockPos getbuildPosition()
    {
        return startPos.add(offsetX, 0, offsetZ);
    }

    public boolean hasWallOnSide(EnumFacing side) { return walls.hasWallOnSide(side); }

    public boolean hasDoorOnSide(EnumFacing side)
    {
        return walls.hasDoorOnSide(side);
    }

    public DoorPlacement addDoorOnSideCentered(EnumFacing side)
    {
        return walls.addCenteredDoor(buildLength, side);
    }
    public DoorPlacement addDoorOnSideRandom(Random random, EnumFacing side)
    {
        return walls.addRandomDoor(random, buildLength, side);
    }

    public void addOuterWall(EnumFacing side)
    {
        walls.addOuter(side);
    }

    public void addInnerWall(EnumFacing side)
    {
        walls.addInner(side);
    }

    public void removeWall(EnumFacing side)
    {
        walls.removeWall(side);
    }

    public void registerAdjacentRoomDoor(EnumFacing side, DoorPlacement door)
    {
        walls.registerAdjacentDoor(side, door);
    }

    protected void setupDecoration(World world)
    {
        decoArea = new HashSet<>(getDecorationArea());
        setDoorAreasToAir(world);
    }

    protected void setDoorAreasToAir(World world)
    {
        BlockPos toAdd;
        BlockPos topLeft = getDecorationStartPos();
        int xStart = topLeft.getX();
        int zStart = topLeft.getZ();
        int xEnd = xStart + (getDecorationLengthX() - 1);
        int zEnd = zStart + (getDecorationLengthZ() - 1);
        int yStart = topLeft.getY();

        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            DoorPlacement placement = null;

            if (walls.hasDoorOnSide(side))
            {
                placement = walls.getDoorOnSide(side);
            }
            else if (walls.adjacentRoomHasDoorOnSide(side))
            {
                placement = walls.getAdjacentDoor(side);
            }

            if (placement != null)
            {
                final int doorStart;
                final int doorEnd;
                final int yEnd = yStart + placement.getHeight() - 1;

                if (side.getAxis() == EnumFacing.Axis.Z)
                {
                    doorStart = startPos.getX() + placement.getOffset();
                    doorEnd = doorStart + placement.getWidth() - 1;

                    int z;
                    if (side == EnumFacing.NORTH)
                    {
                        z = zStart;
                    }
                    else //SOUTH
                    {
                        z = zEnd;
                    }
                    for (int x = doorStart; x <= doorEnd; x++)
                    {
                        for (int y = yStart; y < yEnd; y++)
                        {
                            toAdd = new BlockPos(x, y, z);
                            world.setBlockState(toAdd, Blocks.AIR.getDefaultState());
                            decoMap.add(toAdd);
                        }
                    }
                }
                else
                {
                    doorStart = startPos.getZ() + placement.getOffset();
                    doorEnd = doorStart + placement.getWidth() - 1;

                    int x;
                    if (side == EnumFacing.WEST)
                    {
                        x = xStart;
                    }
                    else //SOUTH
                    {
                        x = xEnd;
                    }
                    for (int z = doorStart; z <= doorEnd; z++)
                    {
                        for (int y = yStart; y < yEnd; y++)
                        {
                            toAdd = new BlockPos(x, y, z);
                            world.setBlockState(toAdd, Blocks.AIR.getDefaultState());
                            decoMap.add(toAdd);
                        }
                    }
                }
            }
        }
    }

    /*
     * Get a list of blocks that make up the decoratable edge of the room.
     * Decoratable edge positions are adjacent to a wall but not in front of a door.
     */
    protected ArrayList<BlockPos> getDecorationEdge(EnumFacing side)
    {
        //First get all blocks that are not occupied by walls
        ArrayList<BlockPos> result = getDecorationFirstLayer();

        BlockPos topLeft = getDecorationStartPos();
        final int xStart = topLeft.getX();
        final int zStart = topLeft.getZ();
        final int xEnd = xStart + (getDecorationLengthX() - 1);
        final int zEnd = zStart + (getDecorationLengthZ() - 1);

        if (side == EnumFacing.NORTH)
        {
            result.removeIf(p -> p.getZ() != zStart);
            result.sort(Comparator.comparingInt(BlockPos::getX));
        }
        else if (side == EnumFacing.SOUTH)
        {
            result.removeIf(p -> p.getZ() != zEnd);
            result.sort(Comparator.comparingInt(BlockPos::getX).reversed());
        }
        else if (side == EnumFacing.WEST)
        {
            result.removeIf(p -> p.getX() != xStart);
            result.sort(Comparator.comparingInt(BlockPos::getZ));
        }
        else if (side == EnumFacing.EAST)
        {
            result.removeIf(p -> p.getX() != xEnd);
            result.sort(Comparator.comparingInt(BlockPos::getZ).reversed());
        }

        result.removeIf(p -> decoMap.contains(p)); //Remove block if it is occupied already

        return result;
    }

    /*
    * Get a 1-height square of block positions that represents the lowest y position
    * of a room that can be decorated. In other words, the layer just above the floor
    * that is not already occupied by walls.
     */
    protected ArrayList<BlockPos> getDecorationFirstLayer()
    {
        ArrayList<BlockPos> result = getDecorationArea();

        if (!result.isEmpty())
        {
            BlockPos lowerUpperLeft = result.get(0);
            result.removeIf(p -> p.getY() != lowerUpperLeft.getY());
        }

        return result;
    }

    protected ArrayList<BlockPos> getDecorationArea()
    {
        ArrayList<BlockPos> result = new ArrayList<>();
        BlockPos start = getDecorationStartPos();

        for (int x = 0; x < getDecorationLengthX(); x++)
        {
            for (int y = 0; y < getDecorationLengthY(); y++)
            {
                for (int z = 0; z < getDecorationLengthZ(); z++)
                {
                    result.add(start.add(x, y, z));
                }
            }
        }

        return result;
    }

    protected BlockPos getDecorationStartPos()
    {
        return getNonWallStartPos().up(); //skip the floor
    }

    protected BlockPos getNonWallStartPos()
    {
        BlockPos result = startPos;

        if (walls.hasWallOnSide(EnumFacing.NORTH))
        {
            result = result.south();
        }
        if (walls.hasWallOnSide(EnumFacing.WEST))
        {
            result = result.east();
        }

        return result;
    }

    protected int getDecorationLengthX()
    {
        int result = buildLength;

        if (walls.hasWallOnSide(EnumFacing.WEST))
        {
            --result;
        }
        if (walls.hasWallOnSide(EnumFacing.EAST))
        {
            --result;
        }

        return result;
    }

    protected int getDecorationLengthZ()
    {
        int result = buildLength;

        if (walls.hasWallOnSide(EnumFacing.NORTH))
        {
            --result;
        }
        if (walls.hasWallOnSide(EnumFacing.SOUTH))
        {
            --result;
        }

        return result;
    }

    protected int getDecorationLengthY()
    {
        int result = height - 1; //Remove one for the floor tiles

        if (defaultCeiling)
        {
            --result;
        }

        return result;
    }

    public int[] getChestIDs()
    {
        return null;
    }

    protected int getSpawnerCount()
    {
        return 2;
    }

    public int getOffsetX()
    {
        return offsetX;
    }

    public int getOffsetZ()
    {
        return offsetZ;
    }

    public int getBuildLength()
    {
        return buildLength;
    }

    @Override
    public String toString()
    {
        return roomType.name;
    }
}
