package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomKitchen extends CastleRoomGeneric {
	public CastleRoomKitchen(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.KITCHEN;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 3);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.TORCH, 2);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 3);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.TABLE_SM, 2);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.CRAFTING_TABLE, 1);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.FURNACE, 1);
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		super.generateRoom(world, dungeon);
	}

	@Override
	public int[] getChestIDs() {
		return new int[] { ELootTable.CQ_FOOD.getID() };
	}
}
