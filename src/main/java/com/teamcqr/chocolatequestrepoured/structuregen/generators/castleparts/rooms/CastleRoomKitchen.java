package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomKitchen extends CastleRoomGeneric
{
    public CastleRoomKitchen(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.KITCHEN;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;

        this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 5);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.TABLE_S, 3);
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        //build the floor
        for (int z = 0; z < sideLength - 1; z++)
        {
            for (int x = 0; x < sideLength - 1; x++)
            {
                blocks.add(new BlockPlacement(startPos.add( x, 0, z), Blocks.PLANKS.getDefaultState()));
            }
        }

        super.generateRoom(blocks);
    }
}
