package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class CastleRoomArmory extends CastleRoomGenericBase {
	public CastleRoomArmory(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.ARMORY;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 5);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 3);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ANVIL, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ARMOR_STAND, 1);

		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 20);
		this.decoSelector.registerMidDecor(RoomDecorTypes.TABLE_2x2, 1);
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		super.generateRoom(castleOrigin, genArray, dungeon);
	}

	@Override
	public ResourceLocation[] getChestIDs() {
		return new ResourceLocation[] { CQRLoottables.CHESTS_EQUIPMENT };
	}
}
