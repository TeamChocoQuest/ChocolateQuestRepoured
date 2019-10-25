package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleAddonWall implements ICastleAddon
{
    public enum WindowType
    {
        NONE,
        BASIC_GLASS,
        BASIC_BARS,
    }

    private WindowType windowType;
    private EnumFacing side;
    private BlockPos startPos; //the lowest, farthest west and farthest north point on the wall
    private int length;
    private int height;

    public CastleAddonWall(WindowType windowType, EnumFacing side, BlockPos startPos, int length, int height)
    {
        this.windowType = windowType;
        this.side = side;
        this.startPos = startPos;
        this.length = length;
        this.height = height;
    }

    @Override
    public void generate(ArrayList<BlockPlacement> blocks)
    {
        BlockPos pos = startPos;
        IBlockState blockToBuild = Blocks.STONEBRICK.getDefaultState();

        EnumFacing iterDirection;

        if (side.getAxis() == EnumFacing.Axis.X)
        {
            iterDirection = EnumFacing.EAST;
        }
        else
        {
            iterDirection = EnumFacing.SOUTH;
        }

        for (int i = 0; i < length; i++)
        {
            for (int y = 0; y < height; y++)
            {
                pos = startPos.offset(iterDirection, i).offset(EnumFacing.UP, y);
                blockToBuild = getBlockToBuild(pos);
                blocks.add(new BlockPlacement(pos, blockToBuild));
            }
        }
    }

    private IBlockState getBlockToBuild(BlockPos pos)
    {
        IBlockState blockToBuild;

        switch(windowType)
        {
            case BASIC_GLASS:
                blockToBuild = getBlockBasicGlass(pos);
                break;
            case BASIC_BARS:
                blockToBuild = getBlockBasicBars(pos);
                break;
            case NONE:
            default:
                blockToBuild = Blocks.STONEBRICK.getDefaultState();
                break;
        }

        return blockToBuild;
    }

    private IBlockState getBlockBasicGlass(BlockPos pos)
    {
        int y = pos.getY();
        int dist = getLengthPoint(pos);

        if ((y == 3 || y == 4) && (dist == length / 2))
        {
            return Blocks.GLASS_PANE.getDefaultState();
        }
        else
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
    }

    private IBlockState getBlockBasicBars(BlockPos pos)
    {
        int y = pos.getY();
        int dist = getLengthPoint(pos);

        if ((y == 3 || y == 4) && (dist == length / 2))
        {
            return Blocks.IRON_BARS.getDefaultState();
        }
        else
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
    }

    /*
    * Whether to build a window is usually determined by how far along the wall we are.
    * This function gets the relevant length along the wall based on if we are a horizontal
    * wall or a vertical wall.
    */
    private int getLengthPoint(BlockPos pos)
    {
        if (side.getAxis() == EnumFacing.Axis.X)
        {
            return pos.getX();
        }
        else
        {
            return pos.getZ();
        }
    }
}
