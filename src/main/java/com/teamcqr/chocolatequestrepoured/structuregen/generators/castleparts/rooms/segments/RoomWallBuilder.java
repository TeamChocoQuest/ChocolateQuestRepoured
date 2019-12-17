package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class RoomWallBuilder
{
    protected BlockPos wallStart;
    protected WallOptions options;
    protected int doorStart = 0;
    protected int doorWidth = 0;
    protected int doorHeight = 0;
    protected int length;
    protected int height;
    protected EnumFacing side;
    protected Random random;

    public RoomWallBuilder(BlockPos wallStart, int height, int length, WallOptions options, EnumFacing side)
    {
        this.height = height;
        this.length = length;
        this.options = options;
        this.side = side;

        this.wallStart = wallStart;

        if (options.hasDoor())
        {
            this.doorStart = options.getDoor().getOffset();
            this.doorWidth = options.getDoor().getWidth();
            this.doorHeight = options.getDoor().getHeight();
        }
    }

    public void generate(World world, CastleDungeon dungeon)
    {
        BlockPos pos;
        IBlockState blockToBuild;

        EnumFacing iterDirection;

        if (side.getAxis() == EnumFacing.Axis.X)
        {
            iterDirection = EnumFacing.SOUTH;
        }
        else
        {
            iterDirection = EnumFacing.EAST;
        }

        for (int i = 0; i < length; i++)
        {
            for (int y = 0; y < height; y++)
            {
                pos = wallStart.offset(iterDirection, i).offset(EnumFacing.UP, y);
                blockToBuild = getBlockToBuild(pos, dungeon);
                world.setBlockState(pos, blockToBuild);
            }
        }
    }

    protected IBlockState getBlockToBuild(BlockPos pos, CastleDungeon dungeon)
    {
        if (options.hasWindow())
        {
            return getBlockWindowBasicGlass(pos, dungeon);
        }
        else if (options.hasDoor())
        {
            return getDoorBlock(pos, dungeon);
        }
        else
        {
            return dungeon.getWallBlock().getDefaultState();
        }
    }

    protected IBlockState getDoorBlock(BlockPos pos, CastleDungeon dungeon)
    {
        switch (options.getDoor().getType())
        {
            case AIR:
                return getBlockDoorAir(pos, dungeon);

            case STANDARD:
                return getBlockDoorStandard(pos, dungeon);

            case FENCE_BORDER:
                return getBlockDoorFenceBorder(pos, dungeon);

            case STAIR_BORDER:
                return getBlockDoorStairBorder(pos, dungeon);

            default:
                return dungeon.getWallBlock().getDefaultState();
        }
    }

    private IBlockState getBlockDoorAir(BlockPos pos, CastleDungeon dungeon)
    {
        IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
        int y = pos.getY() - wallStart.getY();
        int dist = getLengthPoint(pos);

        if (withinDoorWidth(dist))
        {
            if (y == 0)
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
            else if (y < doorHeight)
            {
                blockToBuild = Blocks.AIR.getDefaultState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorStairBorder(BlockPos pos, CastleDungeon dungeon)
    {
        IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
        final int y = pos.getY() - wallStart.getY();
        final int dist = getLengthPoint(pos);
        final int halfPoint = doorStart + (doorWidth / 2);

        if (withinDoorWidth(dist))
        {
            if (y == 0)
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
            else if (dist == halfPoint || dist == halfPoint - 1)
            {
                if (y >= 1 && y <= 3)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                }
                else if (y == 4)
                {
                    blockToBuild = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
                }
            }
            else if (dist == halfPoint + 1 || dist == halfPoint - 2)
            {
                EnumFacing stairFacing;

                if (side == EnumFacing.WEST || side == EnumFacing.SOUTH)
                {
                    stairFacing = (dist == halfPoint - 2) ? side.rotateY() : side.rotateYCCW();
                }
                else
                {
                    stairFacing = (dist == halfPoint - 2) ? side.rotateYCCW() : side.rotateY();
                }


                IBlockState stairBase = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);

                if (y == 1)
                {
                    blockToBuild = stairBase;
                }
                else if (y == 2 || y == 3)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                }
                else if (y == 4)
                {
                    blockToBuild = stairBase.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
                }
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorFenceBorder(BlockPos pos, CastleDungeon dungeon)
    {
        IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
        final int y = pos.getY() - wallStart.getY();
        final int dist = getLengthPoint(pos);
        final int halfPoint = doorStart + (doorWidth / 2);

        if (withinDoorWidth(dist))
        {
            if (y == 0)
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
            else if (dist == halfPoint || dist == halfPoint - 1)
            {
                if (y == 1 || y == 2)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                }
                else if (y == 3)
                {
                    blockToBuild = Blocks.OAK_FENCE.getDefaultState();
                }
            }
            else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < doorHeight))
            {
                blockToBuild = Blocks.OAK_FENCE.getDefaultState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorStandard(BlockPos pos, CastleDungeon dungeon)
    {
        IBlockState blockToBuild = dungeon.getWallBlock().getDefaultState();
        final int y = pos.getY() - wallStart.getY();
        final int dist = getLengthPoint(pos);
        final int halfPoint = doorStart + (doorWidth / 2);

        if (withinDoorWidth(dist))
        {
            if (y == 0)
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
            else if ((dist == halfPoint || dist == halfPoint - 1))
            {
                if (y == 1 || y == 2)
                {
                    BlockDoor.EnumDoorHalf half = (y == 1) ? BlockDoor.EnumDoorHalf.LOWER : BlockDoor.EnumDoorHalf.UPPER;
                    BlockDoor.EnumHingePosition hinge;

                    if (side == EnumFacing.WEST || side == EnumFacing.SOUTH)
                    {
                        hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT;
                    }
                    else
                    {
                        hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT;
                    }

                    blockToBuild = Blocks.OAK_DOOR.getDefaultState().
                            withProperty(BlockDoor.HALF, half).
                            withProperty(BlockDoor.FACING, side).
                            withProperty(BlockDoor.HINGE, hinge);
                }
                else if (y == 3)
                {
                    blockToBuild = Blocks.PLANKS.getDefaultState();
                }

            }
            else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < doorHeight))
            {
                blockToBuild = Blocks.PLANKS.getDefaultState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockWindowBasicGlass(BlockPos pos, CastleDungeon dungeon)
    {
        int y = pos.getY() - wallStart.getY();
        int dist = getLengthPoint(pos);

        if ((y == 3 || y == 4) && (dist == length / 2))
        {
            return Blocks.GLASS_PANE.getDefaultState();
        }
        else
        {
            return dungeon.getWallBlock().getDefaultState();
        }
    }

    private IBlockState getBlockWindowBasicBars(BlockPos pos, CastleDungeon dungeon)
    {
        int y = pos.getY();
        int dist = getLengthPoint(pos);

        if ((y == 3 || y == 4) && (dist == length / 2))
        {
            return Blocks.IRON_BARS.getDefaultState();
        }
        else
        {
            return dungeon.getWallBlock().getDefaultState();
        }
    }

    /*
     * Whether to build a door or window is usually determined by how far along the wall we are.
     * This function gets the relevant length along the wall based on if we are a horizontal
     * wall or a vertical wall.
     */
    protected int getLengthPoint(BlockPos pos)
    {
        if (side.getAxis() == EnumFacing.Axis.X)
        {
            return pos.getZ() - wallStart.getZ();
        } else
        {
            return pos.getX() - wallStart.getX();
        }
    }

    protected boolean withinDoorWidth(int value)
    {
        int relativeToDoor = value - doorStart;
        return (relativeToDoor >= 0 && relativeToDoor < doorWidth);
    }
}
