package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorTableSmall extends RoomDecorBlocks
{
    public RoomDecorTableSmall()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.OAK_FENCE));

        schematic.add(new DecoBlockOffset(0, 1, 0, Blocks.WOODEN_PRESSURE_PLATE));
    }

}
