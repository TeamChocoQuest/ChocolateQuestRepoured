package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;

public class CastleRoomGeneric extends CastleRoom
{
    protected ArrayList<IBlockState> edgeClutter;
    protected ArrayList<IBlockState> centerClutter;

    public void generate(ArrayList<BlockPlacement> blocks)
    {
        return;
    }
}
