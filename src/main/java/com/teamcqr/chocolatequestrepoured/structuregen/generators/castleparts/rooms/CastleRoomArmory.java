package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomArmory extends CastleRoomGeneric
{
    public CastleRoomArmory(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.ALCHEMY_LAB;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;
        this.defaultFloor = true;

        this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 5);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 5);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.ANVIL, 2);
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        super.generateRoom(blocks);
    }
}
