package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import com.teamcqr.chocolatequestrepoured.util.WeightedRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public abstract class CastleRoomGeneric extends CastleRoom
{

    public CastleRoomGeneric(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
    }
}
