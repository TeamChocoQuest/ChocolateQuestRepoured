package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CastleRoomRoofBossMain extends CastleRoom
{
    private int subRoomsX;
    private int subRoomsZ;

    public CastleRoomRoofBossMain(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ROOF_BOSS_MAIN;
        this.pathable = false;
        this.subRoomsX = 0;
        this.subRoomsZ = 0;
    }

    public void setSubRooms(int x, int z)
    {
        subRoomsX = x;
        subRoomsZ = z;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        BlockPos nwCorner = getNonWallStartPos();
        BlockPos pos;
        IBlockState blockToBuild;

        for (int x = 0; x < 17; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                for (int z = 0; z < 17; z++)
                {
                    blockToBuild = getBlockToBuild(x, y, z);
                    pos = nwCorner.add(x, y, z);

                    world.setBlockState(pos, blockToBuild);
                }
            }
        }
    }

    private IBlockState getBlockToBuild(int x, int y, int z)
    {
        IBlockState blockToBuild = Blocks.AIR.getDefaultState();
        if (y == 0 || y == 7)
        {
            if (floorDesignBlock(x, z))
            {
                blockToBuild = Blocks.STONE.getDefaultState();
            }
            else
            {
                blockToBuild = Blocks.STONEBRICK.getDefaultState();
            }
        }
        else if (x == 0 || z == 0 || x == 16 || z == 16)
        {
            blockToBuild = getOuterEdgeBlock(x, y, z);
        }
        else if (x == 1 || x == 15 || z == 1 || z == 15)
        {
            blockToBuild = getInnerEdgeBlock(x, y, z);
        }

        return blockToBuild;
    }

    @Override
    protected void generateWalls(World world, CastleDungeon dungeon)
    {
        ; //Skip building walls
    }

    private boolean floorDesignBlock(int x, int z)
    {
        final int[][] floorPattern = new int[][]
                {
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
                    {0,0,0,1,1,1,0,0,1,0,0,1,1,1,0,0,0},
                    {0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0},
                    {0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,0},
                    {0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
                    {0,0,1,0,0,1,1,1,0,1,1,1,0,0,1,0,0},
                    {0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0},
                    {0,0,1,0,0,1,1,1,0,1,1,1,0,0,1,0,0},
                    {0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,1,1,1,0,0,1,0,0,1,1,1,0,0,0},
                    {0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
                };

        return checkPatternIndex(x, z, floorPattern);
    }

    private boolean checkPatternIndex(int x, int z, int[][] pattern)
    {
        if (pattern != null && z >= 0 && z <= pattern.length && x >= 0 && x <= pattern[0].length)
        {
            return pattern[x][z] == 1;
        }
        else
        {
            return false;
        }
    }

    private IBlockState getOuterEdgeBlock(int x, int y, int z)
    {
        if (x == 0 || x == 16)
        {
            if (z == 0 || z == 3 || z == 6 || z == 10 || z == 13 || z == 16)
            {
                return Blocks.STONEBRICK.getDefaultState();
            }
            else if (z >= 7 && z <= 9)
            {
                if (y >= 1 && y <= 3)
                {
                    return Blocks.AIR.getDefaultState();
                }
                else if (y == 4)
                {
                    if (z == 7 || z == 9)
                    {
                        EnumFacing doorFrameFacing = (z == 7) ? EnumFacing.WEST : EnumFacing.EAST;
                        return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                                withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).
                                withProperty(BlockStairs.FACING, doorFrameFacing);
                    }
                    else
                    {
                        return Blocks.AIR.getDefaultState();
                    }
                }
            }
            else
            {
                if (y == 6)
                {
                    return Blocks.STONEBRICK.getDefaultState();
                }
                else if (y == 2 || y == 3 || y == 4)
                {
                    return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
                }
                else if (y == 1)
                {
                    EnumFacing windowBotFacing = (x == 0) ? EnumFacing.WEST : EnumFacing.EAST;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).
                            withProperty(BlockStairs.FACING, windowBotFacing);
                }
                else if (y == 5)
                {
                    EnumFacing windowTopFacing = (x == 0) ? EnumFacing.EAST : EnumFacing.WEST;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).
                            withProperty(BlockStairs.FACING, windowTopFacing);
                }
            }
        }
        else if (z == 0 || z == 16)
        {
            if (x == 0 || x == 3 || x == 6 || x == 10 || x == 13 || x == 16)
            {
                return Blocks.STONEBRICK.getDefaultState();
            }
            else if (x >= 7 && x <= 9)
            {
                if (y >= 1 && y <= 3)
                {
                    return Blocks.AIR.getDefaultState();
                }
                else if (y == 4)
                {
                    if (x == 7 || x == 9)
                    {
                        EnumFacing doorFrameFacing = (x == 7) ? EnumFacing.WEST : EnumFacing.EAST;
                        return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                                withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).
                                withProperty(BlockStairs.FACING, doorFrameFacing);
                    }
                    else
                    {
                        return Blocks.AIR.getDefaultState();
                    }
                }
            }
            else
            {
                if (y == 6)
                {
                    return Blocks.STONEBRICK.getDefaultState();
                }
                else if (y == 2 || y == 3 || y == 4)
                {
                    return Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
                }
                else if (y == 1)
                {
                    EnumFacing windowBotFacing = (z == 0) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).
                            withProperty(BlockStairs.FACING, windowBotFacing);
                }
                else if (y == 5)
                {
                    EnumFacing windowTopFacing = (z == 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).
                            withProperty(BlockStairs.FACING, windowTopFacing);
                }
            }
        }

        return Blocks.STONEBRICK.getDefaultState();
    }

    private IBlockState getInnerEdgeBlock(int x, int y, int z)
    {
        final IBlockState chiseledStoneBlock = Blocks.STONEBRICK.getDefaultState().
                withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);

        if (x == 1 || x == 15)
        {
            if (z == 0 || z == 3 || z == 6 || z == 10 || z == 13 || z == 16)
            {
                return chiseledStoneBlock;
            }
            else if (z >= 7 && z <= 9)
            {
                if (y == 3 && (z == 7 || z == 9))
                {
                    return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
                }
                else if (y == 4)
                {
                    return chiseledStoneBlock;
                }
                else if (y == 5 && z == 8)
                {
                    EnumFacing frameTopStairFacing = (x == 1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).
                            withProperty(BlockStairs.FACING, frameTopStairFacing);
                }
            }
        }
        else if (z == 1 || z == 15)
        {
            if (x == 0 || x == 3 || x == 6 || x == 10 || x == 13 || x == 16)
            {
                return chiseledStoneBlock;
            }
            else if (x >= 7 && x <= 9)
            {
                if (y == 3 && (x == 7 || x == 9))
                {
                    return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
                }
                else if (y == 4)
                {
                    return chiseledStoneBlock;
                }
                else if (y == 5 && x == 8)
                {
                    EnumFacing frameTopStairFacing = (z == 1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    return Blocks.STONE_BRICK_STAIRS.getDefaultState().
                            withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).
                            withProperty(BlockStairs.FACING, frameTopStairFacing);
                }
            }
        }

        return Blocks.AIR.getDefaultState();
    }
}
