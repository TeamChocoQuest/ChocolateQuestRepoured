package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomStaircaseDirected extends CastleRoom
{
    private static final int PLATFORM_LENGTH = 2;
    private EnumFacing doorSide;
    private int numRotations;
    private int upperStairWidth;
    private int upperStairLength;
    private int centerStairWidth;
    private int centerStairLength;

    public CastleRoomStaircaseDirected(BlockPos startPos, int sideLength, int height, EnumFacing doorSide)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.STAIRCASE_DIRECTED;
        this.doorSide = doorSide;
        this.numRotations = getNumYRotationsFromStartToEndFacing(EnumFacing.SOUTH, this.doorSide);
        this.defaultCeiling = false;

        upperStairWidth = 0;

        //Determine the width of the center stairs and the two upper side stairs. Find the largest possible
        //side width such that the center width is still greater than or equal to the length of each side.
        do
        {
            upperStairWidth++;
            centerStairWidth = (sideLength - 1) - upperStairWidth * 2;
        } while ((centerStairWidth - 2) >= (upperStairWidth + 1));

        //Each stair section should cover half the ascent
        upperStairLength = height / 2;
        centerStairLength = height + 1 - upperStairLength; //center section will either be same length or 1 more
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild;
        for (int x = 0; x < sideLength - 1; x++)
        {
            for (int z = 0; z < sideLength - 1; z++)
            {
                buildFloorBlock(x, z, blocks);

                if (z < 2)
                {
                    buildPlatform(x, z, blocks);
                }
                else if (((x < upperStairWidth) || (x >= centerStairWidth + upperStairWidth)) && z < upperStairLength + PLATFORM_LENGTH)
                {
                    buildUpperStair(x, z, blocks);
                }
                else if (((x >= upperStairWidth) || (x < centerStairWidth + upperStairWidth)) && z <= centerStairLength + PLATFORM_LENGTH)
                {
                    buildLowerStair(x, z, blocks);
                }
            }
        }
    }

    @Override
    public String getNameShortened()
    {
        return "STR";
    }

    public void setDoorSide(EnumFacing side)
    {
        this.doorSide = side;
    }

    public int getUpperStairEndZ()
    {
        return (upperStairLength);
    }

    public int getUpperStairWidth()
    {
        return upperStairWidth;
    }

    public int getCenterStairWidth()
    {
        return centerStairWidth;
    }

    public EnumFacing getDoorSide()
    {
        return doorSide;
    }

    private void buildFloorBlock(int x, int z, ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild = Blocks.STONEBRICK.getDefaultState();
        blocks.add(new BlockPlacement(startPos.add(x, 0, z), blockToBuild));
    }

    private void buildUpperStair(int x, int z, ArrayList<BlockPlacement> blocks)
    {
        int stairHeight = centerStairLength + (z - PLATFORM_LENGTH);
        EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.SOUTH, numRotations);
        IBlockState blockToBuild;
        for (int y = 1; y < height; y++)
        {
            if (y < stairHeight)
            {
                blockToBuild = Blocks.STONEBRICK.getDefaultState();
            }
            else if (y == stairHeight)
            {
                blockToBuild = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
            }
            else
            {
                blockToBuild = Blocks.AIR.getDefaultState();
            }
            blocks.add(new BlockPlacement(getRotatedPlacement(x, y, z, this.doorSide), blockToBuild));
        }
    }

    private void buildLowerStair(int x, int z, ArrayList<BlockPlacement> blocks)
    {
        int stairHeight = centerStairLength - (z - PLATFORM_LENGTH + 1);
        EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.NORTH, numRotations);
        IBlockState blockToBuild;
        for (int y = 1; y < height; y++)
        {
            if (y < stairHeight)
            {
                blockToBuild = Blocks.STONEBRICK.getDefaultState();
            }
            else if (y == stairHeight)
            {
                blockToBuild = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
            }
            else
            {
                blockToBuild = Blocks.AIR.getDefaultState();
            }
            blocks.add(new BlockPlacement(getRotatedPlacement(x, y, z, this.doorSide), blockToBuild));
        }
    }

    private void buildPlatform(int x, int z, ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild;
        int platformHeight = centerStairLength; //the stair length is also the platform height

        for (int y = 1; y < height; y++)
        {
            if (y < platformHeight)
            {
                blockToBuild = Blocks.STONEBRICK.getDefaultState();
            }
            else
            {
                blockToBuild =  Blocks.AIR.getDefaultState();
            }
            blocks.add(new BlockPlacement(getRotatedPlacement(x, y, z, this.doorSide), blockToBuild));
        }
    }

    @Override
    public boolean canBuildDoorOnSide(EnumFacing side)
    {
        return (side == doorSide);
    }

    @Override
    public boolean reachableFromSide(EnumFacing side)
    {
        return (side == doorSide);
    }
}
