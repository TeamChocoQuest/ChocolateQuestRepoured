package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorBrewingStand extends RoomDecorBase
{
    public RoomDecorBrewingStand()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockType = Blocks.BREWING_STAND.getDefaultState();
        schematic.add(new DecoPlacement(0, 0, 0, blockType));
    }
}
