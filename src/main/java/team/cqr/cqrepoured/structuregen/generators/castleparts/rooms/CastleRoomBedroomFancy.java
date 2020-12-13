package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;

public class CastleRoomBedroomFancy extends CastleRoomGenericBase {
	private EnumDyeColor carpetColor;

	public CastleRoomBedroomFancy(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
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
	protected IBlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
		return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, this.carpetColor);
	}
}
