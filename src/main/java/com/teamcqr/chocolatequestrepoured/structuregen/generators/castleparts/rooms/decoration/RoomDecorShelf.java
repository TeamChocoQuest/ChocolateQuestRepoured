package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;

public class RoomDecorShelf extends RoomDecorBase
{
    public RoomDecorShelf()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockToBuild = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.schematic.add(new DecoPlacement(0, 2, 0, blockToBuild));
        this.schematic.add(new DecoPlacement(1, 2, 0, blockToBuild));

        blockToBuild = Blocks.AIR.getDefaultState();
        this.schematic.add(new DecoPlacement(0, 1, 0, blockToBuild));
        this.schematic.add(new DecoPlacement(1, 1, 0, blockToBuild));
        this.schematic.add(new DecoPlacement(0, 0, 0, blockToBuild));
        this.schematic.add(new DecoPlacement(1, 0, 0, blockToBuild));

    }
}
