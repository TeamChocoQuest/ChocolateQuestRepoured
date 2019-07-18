package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import net.minecraft.init.Blocks;

public class CastleRoomKitchen extends CastleRoomGeneric
{
    public CastleRoomKitchen()
    {
        this.edgeClutter.add(Blocks.FURNACE.getDefaultState());
        this.edgeClutter.add(Blocks.CAULDRON.getDefaultState());
        this.edgeClutter.add(Blocks.WOODEN_SLAB.getDefaultState());
    }
}
