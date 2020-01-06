package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.EnumRoomDecor;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBedroom extends CastleRoomGeneric {
	private EnumDyeColor carpetColor;

	public CastleRoomBedroom(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.BEDROOM;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(EnumRoomDecor.NONE, 4);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.TORCH, 2);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.SHELF, 1);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.TABLE_SM, 2);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.FIREPLACE, 1);
		this.decoSelector.registerEdgeDecor(EnumRoomDecor.BED, 2);

		List<EnumDyeColor> possibleColors = Arrays.asList(EnumDyeColor.values());
		Collections.shuffle(possibleColors);
		this.carpetColor = possibleColors.get(0);
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		super.generateRoom(world, dungeon);
	}

	@Override
	protected IBlockState getFloorBlock(CastleDungeon dungeon) {
		return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, this.carpetColor);
	}
}
