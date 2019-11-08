package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorTableSmall extends RoomDecorBase
{
    public RoomDecorTableSmall()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockType = Blocks.OAK_FENCE.getDefaultState();
        schematic.add(new DecoPlacement(0, 0, 0, blockType));

        blockType = Blocks.WOODEN_PRESSURE_PLATE.getDefaultState();
        schematic.add(new DecoPlacement(0, 1, 0, blockType));
    }

}
