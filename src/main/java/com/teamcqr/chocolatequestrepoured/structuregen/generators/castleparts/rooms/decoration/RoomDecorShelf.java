package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorShelf extends RoomDecor
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
}
