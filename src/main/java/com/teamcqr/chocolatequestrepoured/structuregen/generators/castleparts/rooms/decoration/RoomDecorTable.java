package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class RoomDecorTable extends RoomDecor
{
    private static final int SIZE_X = 2;
    private static final int SIZE_Y = 2;
    private static final int SIZE_Z = 2;

    public RoomDecorTable()
    {
        super(SIZE_X, SIZE_Y, SIZE_Z);
    }

    @Override
    protected void makeSchematic()
    {
        IBlockState blockType = Blocks.OAK_FENCE.getDefaultState();
        schematic.add(new DecoPlacement(0, 0, 0, blockType));
        schematic.add(new DecoPlacement(1, 0, 0, blockType));
        schematic.add(new DecoPlacement(0, 0, 1, blockType));
        schematic.add(new DecoPlacement(1, 0, 1, blockType));

        blockType = Blocks.WOODEN_PRESSURE_PLATE.getDefaultState();
        schematic.add(new DecoPlacement(0, 0, 0, blockType));
        schematic.add(new DecoPlacement(1, 0, 0, blockType));
        schematic.add(new DecoPlacement(0, 0, 1, blockType));
        schematic.add(new DecoPlacement(1, 0, 1, blockType));
    }
}
