package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomArmory extends CastleRoomGeneric
{
    public CastleRoomArmory(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ALCHEMY_LAB;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;
        this.defaultFloor = true;

        this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 4);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.TORCH, 2);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 3);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.ANVIL, 2);
        this.decoSelector.registerEdgeDecor(EnumRoomDecor.ARMOR_STAND, 3);
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        super.generateRoom(world, dungeon);
    }

    @Override
    public int[] getChestIDs()
    {
        return new int[] {ELootTable.CQ_EQUIPMENT.getID()};
    }
}
