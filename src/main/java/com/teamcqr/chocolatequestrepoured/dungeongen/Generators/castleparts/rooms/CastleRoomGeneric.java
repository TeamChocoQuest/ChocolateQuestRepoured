package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public abstract class CastleRoomGeneric extends CastleRoom
{
    protected ArrayList<IBlockState> edgeClutter;
    protected ArrayList<IBlockState> centerClutter;

    public CastleRoomGeneric(BlockPos startPos, int sideLength, int height, RoomPosition position)
    {
        super(startPos, sideLength, height, position);
        this.edgeClutter = new ArrayList<>();
        this.centerClutter = new ArrayList<>();
    }

    public void generate(ArrayList<BlockPlacement> blocks)
    {
        return;
    }
}
