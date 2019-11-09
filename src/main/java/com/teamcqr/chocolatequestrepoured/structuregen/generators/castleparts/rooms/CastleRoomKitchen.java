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
        this.defaultFloor = true;

        this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 5);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 3);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.TABLE_SM, 2);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.CRAFTING_TABLE, 1);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.FURNACE, 1);
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        super.generateRoom(blocks);
    }
}
