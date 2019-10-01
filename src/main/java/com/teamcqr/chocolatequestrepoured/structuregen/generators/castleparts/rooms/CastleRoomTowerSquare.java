package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomTowerSquare extends CastleRoom
{
    private static final int MIN_SIZE = 5;
    private EnumFacing alignment;
    private int towerSize;
    private int xStart;
    private int zStart;

    public CastleRoomTowerSquare(BlockPos startPos, int sideLength, int height, EnumFacing alignment, int towerSize)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.STAIRCASE;
        this.alignment = alignment;
        this.towerSize = towerSize;

        if (alignment == EnumFacing.NORTH || alignment == EnumFacing.SOUTH)
        {
            xStart += ((sideLength / 2) - (towerSize - 2));
            if (alignment == EnumFacing.SOUTH)
            {
                zStart += sideLength - towerSize;
            }
        }
        if (alignment == EnumFacing.WEST || alignment == EnumFacing.EAST)
        {
            zStart += ((sideLength / 2) - (towerSize - 2));
            if (alignment == EnumFacing.EAST)
            {
                xStart += sideLength - towerSize;
            }
        }
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild;
        for (int x = xStart; x < towerSize; x++)
        {
            for (int z = zStart; z < towerSize; z++)
            {
                blockToBuild = Blocks.BRICK_BLOCK.getDefaultState();
                blocks.add(new BlockPlacement(startPos.add(x, 0, z), blockToBuild));
                blocks.add(new BlockPlacement(startPos.add(x, height, z), blockToBuild));
            }
        }
    }

    @Override
    public String getNameShortened()
    {
        return "TWR";
    }

    private void buildFloorBlock(int x, int z, ArrayList<BlockPlacement> blocks)
    {
        IBlockState blockToBuild = Blocks.STONEBRICK.getDefaultState();
        blocks.add(new BlockPlacement(startPos.add(x, 0, z), blockToBuild));
    }
}
