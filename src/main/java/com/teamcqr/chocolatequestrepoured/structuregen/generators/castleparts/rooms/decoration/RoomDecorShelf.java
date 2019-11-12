package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorShelf extends RoomDecorBlocks
{
    public RoomDecorShelf()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockToBuild = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.schematic.add(new DecoBlockOffset(0, 2, 0, blockToBuild));
        this.schematic.add(new DecoBlockOffset(1, 2, 0, blockToBuild));

        blockToBuild = Blocks.AIR.getDefaultState();
        this.schematic.add(new DecoBlockOffset(0, 1, 0, blockToBuild));
        this.schematic.add(new DecoBlockOffset(1, 1, 0, blockToBuild));
        this.schematic.add(new DecoBlockOffset(0, 0, 0, blockToBuild));
        this.schematic.add(new DecoBlockOffset(1, 0, 0, blockToBuild));

    }
}
