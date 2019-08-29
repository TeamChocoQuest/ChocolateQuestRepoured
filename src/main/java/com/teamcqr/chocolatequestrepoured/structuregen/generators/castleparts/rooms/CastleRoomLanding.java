package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomLanding extends CastleRoom
{
    private int openingWidth;
    private int openingSeparation;
    private int stairZ;
    private EnumFacing stairStartSide;

    public CastleRoomLanding(BlockPos startPos, int sideLength, int height, RoomPosition position, CastleRoomStaircase stairsBelow)
    {
        super(startPos, sideLength, height, position);
        this.roomType = RoomType.LANDING;
        this.openingWidth = stairsBelow.getUpperStairWidth();
        this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
        this.openingSeparation = stairsBelow.getCenterStairWidth();
        this.stairStartSide = stairsBelow.getDoorSide();
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild;
        for (int x = 0; x < sideLength - 1; x++)
        {
            for (int z = 0; z < sideLength - 1; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    if (y == 0)
                    {
                        if (z > stairZ)
                        {
                            blockToBuild = Blocks.STONEBRICK.getDefaultState();
                        }
                        else if (x < openingWidth || ((x >= openingSeparation + openingWidth) && (x < openingSeparation + openingWidth * 2)))
                        {
                            if (z == stairZ)
                            {
                                EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.SOUTH, getNumYRotationsFromStartToEndFacing(EnumFacing.SOUTH, stairStartSide));
                                blockToBuild = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                            }
                        }
                        else
                        {
                            blockToBuild = Blocks.STONEBRICK.getDefaultState();
                        }
                    }

                    blocks.add(new BlockPlacement(getRotatedPlacement(x, y, z, stairStartSide), blockToBuild));
                }
            }
        }
    }

    @Override
    public String getNameShortened()
    {
        return "LAN";
    }
}
