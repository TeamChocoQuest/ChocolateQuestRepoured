package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SpiralStaircaseBuilder
{
    private BlockPos start;
    private EnumFacing firstSide;

    private int startPattern;

    public SpiralStaircaseBuilder(BlockPos pillarStart, EnumFacing firstStairSide)
    {
        this.start = pillarStart;
        this.firstSide = firstStairSide;
    }

    public boolean isPartOfStairs(BlockPos position)
    {
        return ((Math.abs(position.getX() - start.getX()) <= 1) &&
                (Math.abs(position.getZ() - start.getZ()) <= 1));
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
            return Blocks.STONEBRICK.getDefaultState();
        }

        //The side of the stairs rotates each level from the bottom
        stairSide = rotateFacingNTimesCW(firstSide,position.getY() - start.getY());
        EnumFacing stairFacing = rotateFacingNTimesCW(stairSide, 1);

        switch (stairSide)
        {
            case NORTH:
                if (posX == startX && posZ == startZ - 1)
                {
                    return Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (posX == startX + 1 && (posZ == startZ - 1 || posZ == startZ))
                {
                    return Blocks.STONEBRICK.getDefaultState();
                }
                break;

            case SOUTH:
                if (posX == startX && posZ == startZ + 1)
                {
                    return Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if (posX == startX - 1 && (posZ == startZ + 1 || posZ == startZ))
                {
                    return Blocks.STONEBRICK.getDefaultState();
                }
                break;

            case WEST:
                if (posX == startX - 1 && posZ == startZ)
                {
                    return Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if ((posX == startX || posX == startX - 1) && posZ == startZ - 1)
                {
                    return Blocks.STONEBRICK.getDefaultState();
                }
                break;

            case EAST:
                if (posX == startX + 1 && posZ == startZ)
                {
                    return Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else if ((posX == startX || posX == startX + 1) && posZ == startZ + 1)
                {
                    return Blocks.STONEBRICK.getDefaultState();
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


}
