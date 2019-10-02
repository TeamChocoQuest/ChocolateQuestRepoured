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
            xStart += (towerSize - sideLength) / 2;
            if (alignment == EnumFacing.SOUTH)
            {
                zStart += sideLength - towerSize;
            }
        }
        if (alignment == EnumFacing.WEST || alignment == EnumFacing.EAST)
        {
            zStart += (towerSize - sideLength) / 2;
            if (alignment == EnumFacing.EAST)
            {
                xStart += sideLength - towerSize;
            }
        }
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        BlockPos pos;
        IBlockState blockToBuild;

        for (int x = 0; x < towerSize - 1; x++)
        {
            for (int z = 0; z < towerSize - 1; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    pos = startPos.add(x + xStart, y, z + zStart);

                    if (y == 0)
                    {
                        blockToBuild = Blocks.PLANKS.getDefaultState();
                    }
                }
            }
        }
    }

    @Override
    public String getNameShortened()
    {
        return "TWR";
    }

}
