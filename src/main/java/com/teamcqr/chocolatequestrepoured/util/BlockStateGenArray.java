package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BlockStateGenArray {
    private IBlockState[][][] blockStateArray;
    private int sidePadding;

    public BlockStateGenArray(int xLength, int yHeight, int zLength, int sidePadding) {
        this.sidePadding = sidePadding;
        this.blockStateArray = new IBlockState[xLength + (sidePadding * 2)][yHeight][zLength + (sidePadding * 2)];
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yHeight; y++) {
                for (int z = 0; z < zLength; z++) {
                    this.blockStateArray[x][y][z] = null;
                }
            }
        }
    }

    public boolean add(int xIndex, int yIndex, int zIndex, IBlockState blockState) {
        return addInternal(xIndex, yIndex, zIndex, blockState, false);
    }

    public boolean add(BlockPos pos, IBlockState blockState) {
        return addInternal(pos.getX(), pos.getY(), pos.getZ(), blockState, false);
    }

    public boolean addOverwrite(int xIndex, int yIndex, int zIndex, IBlockState blockState) {
        return addInternal(xIndex, yIndex, zIndex, blockState, true);
    }

    public boolean addOverwrite(BlockPos pos, IBlockState blockState) {
        return addInternal(pos.getX(), pos.getY(), pos.getZ(), blockState, true);
    }

    private boolean addInternal(int xIndex, int yIndex, int zIndex, IBlockState blockState, boolean overwrite) {
        boolean wroteToArray = false;
        xIndex += sidePadding;
        yIndex += sidePadding;
        zIndex += sidePadding;

        try
        {
            if ((xIndex < blockStateArray.length) && (yIndex < blockStateArray[0].length) && (zIndex < blockStateArray[0][0].length))
            {
                if ((overwrite) || (blockStateArray[xIndex][yIndex][zIndex] == null))
                {
                    blockStateArray[xIndex][yIndex][zIndex] = blockState;
                    wroteToArray = true;
                }
            } else
            {
                System.out.printf("Tried to add a blockstate to gen array @ (%d, %d, %d) but was out of [%d][%d][%d] bounds\n",
                        xIndex, yIndex, zIndex, blockStateArray.length, blockStateArray[0].length, blockStateArray[0][0].length);
            }
        }
        catch (IndexOutOfBoundsException ex) {
            System.out.printf("Tried to add a blockstate to gen array @ (%d, %d, %d) but was out of [%d][%d][%d] bounds\n",
                    xIndex, yIndex, zIndex, blockStateArray.length, blockStateArray[0].length, blockStateArray[0][0].length);
        }

        return wroteToArray;
    }

    public IBlockState[][][] finalizeArray() {
        for (int x = 0; x < blockStateArray.length; x++) {
            for (int y = 0; y < blockStateArray[0].length; y++) {
                for (int z = 0; z < blockStateArray[0][0].length; z++) {
                    if (this.blockStateArray[x][y][z] == null) {
                        this.blockStateArray[x][y][z] = Blocks.AIR.getDefaultState();
                    }
                }
            }
        }

        return blockStateArray;
    }

    public BlockPos getAdjustedStartPosition(BlockPos start) {
        return start.add(sidePadding, 0, sidePadding);
    }
}
