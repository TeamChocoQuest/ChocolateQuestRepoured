package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SpiralStaircaseBuilder
{
    private static final int STAIR_WIDTH = 2;

    private BlockPos start;
    private EnumFacing firstSide;
    private Block platformBlock;
    private Block stairBlock;

    public SpiralStaircaseBuilder(BlockPos pillarStart, EnumFacing firstStairSide, Block platformBlock, Block stairBlock)
    {
        this.start = pillarStart;
        this.firstSide = firstStairSide;
        this.platformBlock = platformBlock;
        this.stairBlock = stairBlock;
    }

    //returns true if a position is within this staircase, meaning that it is within
    //the 3x3 grid of the spiral and at or above the starting Y
    public boolean isPartOfStairs(BlockPos position)
    {
        return ((Math.abs(position.getX() - start.getX()) <= STAIR_WIDTH) &&
                (Math.abs(position.getZ() - start.getZ()) <= STAIR_WIDTH) &&
                (position.getY() >= start.getY()));
    }

    public IBlockState getBlock(BlockPos position)
    {
        EnumFacing stairSide;
        int startX = start.getX();
        int startZ = start.getZ();
        int posX = position.getX();
        int posZ = position.getZ();

        if (position.getX() == start.getX() && position.getZ() == start.getZ())
        {
            return platformBlock.getDefaultState();
        }

        //The side of the stairs rotates each level from the bottom
        stairSide = rotateFacingNTimesCW(firstSide,Math.abs(position.getY() - start.getY()));
        EnumFacing stairFacing = rotateFacingNTimesCW(stairSide, 1);

        switch (stairSide)
        {
            case NORTH:
                if (posX == startX && inBoundsNoZero(posZ, startZ, -STAIR_WIDTH))
                {
                    return stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (inBoundsNoZero(posX, startX, STAIR_WIDTH) && inBoundsWithZero(posZ, startZ, -STAIR_WIDTH))
                {
                    return platformBlock.getDefaultState();
                }
                break;

            case SOUTH:
                if (posX == startX && inBoundsNoZero(posZ, startZ, STAIR_WIDTH))
                {
                    return stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (inBoundsNoZero(posX, startX, -STAIR_WIDTH) && inBoundsWithZero(posZ, startZ, STAIR_WIDTH))
                {
                    return platformBlock.getDefaultState();
                }
                break;

            case WEST:
                if (inBoundsNoZero(posX, startX, -STAIR_WIDTH) && posZ == startZ)
                {
                    return stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (inBoundsWithZero(posX, startX, -STAIR_WIDTH) && inBoundsNoZero(posZ, startZ, -STAIR_WIDTH))
                {
                    return platformBlock.getDefaultState();
                }
                break;

            case EAST:
                if (inBoundsNoZero(posX, startX, STAIR_WIDTH) && posZ == startZ)
                {
                    return stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (inBoundsWithZero(posX, startX, STAIR_WIDTH) && inBoundsNoZero(posZ, startZ, STAIR_WIDTH))
                {
                    return platformBlock.getDefaultState();
                }
                break;
            default:
                break;
        }
        return Blocks.AIR.getDefaultState();
    }

    private EnumFacing rotateFacingNTimesCW(EnumFacing facing, int n)
    {
        n = n % 4; //cap at 0-3 rotations, any more is redundant
        while(n != 0)
        {
            facing = facing.rotateY();
            n--;
        }

        return facing;
    }

    private boolean inBoundsNoZero(int pos, int start, int distance)
    {
        int diff = pos - start;
        if (distance > 0)
        {
            return (diff > 0 && diff <= distance);
        }
        else
        {
            return (diff < 0 && diff >= distance);
        }
    }

    private boolean inBoundsWithZero(int pos, int start, int distance)
    {
        int diff = pos - start;
        if (distance > 0)
        {
            return (diff >= 0 && diff <= distance);
        }
        else
        {
            return (diff <= 0 && diff >= distance);
        }
    }
}
