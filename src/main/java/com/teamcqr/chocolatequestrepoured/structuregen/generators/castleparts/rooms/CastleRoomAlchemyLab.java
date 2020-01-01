package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomAlchemyLab extends CastleRoomGeneric
{
    public CastleRoomAlchemyLab(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ALCHEMY_LAB;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;
        this.defaultFloor = true;

        this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 4);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.TORCH, 2);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 2);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.CAULDRON, 1);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.BREW_STAND, 1);
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        super.generateRoom(world, dungeon);
    }
}
