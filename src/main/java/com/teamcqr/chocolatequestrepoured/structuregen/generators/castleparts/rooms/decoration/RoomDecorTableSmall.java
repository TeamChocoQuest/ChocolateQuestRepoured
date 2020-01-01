package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

public class RoomDecorTableSmall extends RoomDecorBlocks
{
    public RoomDecorTableSmall()
    {
        super();
    }

    @Override
    protected void makeSchematic()
    {
        schematic.add(new DecoBlockOffset(0, 0, 0, ModBlocks.TABLE_OAK));
    }

}
