package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomLandingDirected extends CastleRoom
{
    private int openingWidth;
    private int openingSeparation;
    private int stairZ;
    private EnumFacing stairStartSide;

    public CastleRoomLandingDirected(BlockPos startPos, int sideLength, int height, CastleRoomStaircaseDirected stairsBelow)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.LANDING_DIRECTED;
        this.openingWidth = stairsBelow.getUpperStairWidth();
        this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
        this.openingSeparation = stairsBelow.getCenterStairWidth();
        this.stairStartSide = stairsBelow.getDoorSide();
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
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
                            blockToBuild = dungeon.getFloorBlock().getDefaultState();
                        }
                        else if (x < openingWidth || ((x >= openingSeparation + openingWidth) && (x < openingSeparation + openingWidth * 2)))
                        {
                            if (z == stairZ)
                            {
                                EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.SOUTH, getNumYRotationsFromStartToEndFacing(EnumFacing.SOUTH, stairStartSide));
                                blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                            }
                        }
                        else
                        {
                            blockToBuild = dungeon.getFloorBlock().getDefaultState();
                        }
                    }

                    world.setBlockState(getRotatedPlacement(x, y, z, stairStartSide), blockToBuild);
                }
            }
        }
    }

    @Override
    public boolean canBuildDoorOnSide(EnumFacing side)
    {
        //Really only works on this side, could add logic to align the doors for other sides later
        return (side == stairStartSide);
    }

    @Override
    public boolean reachableFromSide(EnumFacing side)
    {
        return (side == stairStartSide || side == stairStartSide.getOpposite());
    }
}
