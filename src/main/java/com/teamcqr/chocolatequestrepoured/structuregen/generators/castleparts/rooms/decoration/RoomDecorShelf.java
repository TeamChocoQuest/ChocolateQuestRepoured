package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;

public class RoomDecorShelf extends RoomDecorBase
{
    private static final int SIZE_X = 1;
    private static final int SIZE_Y = 3;
    private static final int SIZE_Z = 1;

    public RoomDecorShelf()
    {
        super(SIZE_X, SIZE_Y, SIZE_Z);
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockToBuild = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.schematic.add(new DecoPlacement(0, 2, 0, blockToBuild));
    }

    public static boolean wouldFit(BlockPos start, HashSet<BlockPos> decoArea, HashMap<BlockPos, IBlockState> decoMap)
    {
        for (int x = 0; x < SIZE_X; x++)
        {
            for (int y = 0; y < SIZE_Y; y++)
            {
                for (int z = 0; z < SIZE_Z; z++)
                {
                    BlockPos pos = start.add(x, y, z);
                    if (!decoArea.contains(pos) || decoMap.containsKey(pos))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void build(BlockPos start, HashMap<BlockPos, IBlockState> decoMap)
    {
        for (DecoPlacement placement : schematic)
        {
            decoMap.put(start.add(placement.offset), placement.block);
        }
    }
}
