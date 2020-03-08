package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomArmory extends CastleRoomGeneric {
	public CastleRoomArmory(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.ARMORY;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 3);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.TORCH, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 3);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ANVIL, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ARMOR_STAND, 1);

		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 10);
		this.decoSelector.registerMidDecor(RoomDecorTypes.TABLE_2x2, 1);
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		super.generateRoom(world, dungeon);
	}

	@Override
	public int[] getChestIDs() {
		return new int[] { ELootTable.CQ_EQUIPMENT.ordinal() };
	}
}
