package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorAnvil extends RoomDecorBlocks
{
    public RoomDecorAnvil()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.ANVIL));
    }
}
