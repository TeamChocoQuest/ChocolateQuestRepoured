package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorFurnace extends RoomDecorBase
{
    public RoomDecorFurnace()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockType = Blocks.FURNACE.getDefaultState();
        schematic.add(new DecoPlacement(0, 0, 0, blockType));
    }
}
