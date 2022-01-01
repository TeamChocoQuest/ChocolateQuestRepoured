package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.RoomDecorTypes;

public class CastleRoomBedroomFancy extends CastleRoomGenericBase {
	private DyeColor carpetColor;

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

		List<DyeColor> possibleColors = Arrays.asList(DyeColor.values());
		Collections.shuffle(possibleColors);
		this.carpetColor = possibleColors.get(0);
	}

	@Override
	protected BlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
		return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, this.carpetColor);
	}
}
