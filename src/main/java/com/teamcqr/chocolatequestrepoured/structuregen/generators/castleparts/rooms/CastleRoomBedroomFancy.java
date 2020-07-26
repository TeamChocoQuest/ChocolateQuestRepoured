package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

public class CastleRoomBedroomFancy extends CastleRoomGenericBase {
	private EnumDyeColor carpetColor;

	public CastleRoomBedroomFancy(int sideLength, int height, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.BEDROOM_FANCY;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 10);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.TABLE_1x1, 4);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.JUKEBOX, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.FIREPLACE, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.BED, 3);

		List<EnumDyeColor> possibleColors = Arrays.asList(EnumDyeColor.values());
		Collections.shuffle(possibleColors);
		this.carpetColor = possibleColors.get(0);
	}

	@Override
	protected IBlockState getFloorBlock(DungeonCastle dungeon) {
		return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, this.carpetColor);
	}
}
