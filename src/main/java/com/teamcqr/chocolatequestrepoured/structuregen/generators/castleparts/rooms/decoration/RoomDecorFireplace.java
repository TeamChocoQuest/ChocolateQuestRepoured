package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorFireplace extends RoomDecorBlocks
{
    public RoomDecorFireplace()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.BRICK_BLOCK));
        this.schematic.add(new DecoBlockOffset(1, 0, 0, Blocks.NETHERRACK));
        this.schematic.add(new DecoBlockOffset(2, 0, 0, Blocks.BRICK_BLOCK));

        this.schematic.add(new DecoBlockOffset(0, 1, 0, Blocks.BRICK_BLOCK));
        this.schematic.add(new DecoBlockOffset(1, 1, 0, Blocks.FIRE));
        this.schematic.add(new DecoBlockOffset(2, 1, 0, Blocks.BRICK_BLOCK));

        this.schematic.add(new DecoBlockOffset(0, 2, 0, Blocks.BRICK_BLOCK));
        this.schematic.add(new DecoBlockOffset(1, 2, 0, Blocks.BRICK_BLOCK));
        this.schematic.add(new DecoBlockOffset(2, 2, 0, Blocks.BRICK_BLOCK));

        this.schematic.add(new DecoBlockOffset(1, 3, 0, Blocks.BRICK_BLOCK));
        this.schematic.add(new DecoBlockOffset(1, 4, 0, Blocks.BRICK_BLOCK));
    }
}
