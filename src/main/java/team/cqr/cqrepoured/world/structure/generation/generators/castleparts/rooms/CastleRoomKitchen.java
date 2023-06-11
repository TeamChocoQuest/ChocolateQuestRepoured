//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.core.BlockPos;
//import team.cqr.cqrepoured.init.CQRLoottables;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.RoomDecorTypes;
//
//import java.util.Random;
//
//public class CastleRoomKitchen extends CastleRoomGenericBase {
//	public CastleRoomKitchen(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.KITCHEN;
//		this.maxSlotsUsed = 2;
//		this.defaultCeiling = true;
//		this.defaultFloor = true;
//
//		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 4);
//		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 3);
//		this.decoSelector.registerEdgeDecor(RoomDecorTypes.TABLE_1x1, 2);
//		this.decoSelector.registerEdgeDecor(RoomDecorTypes.CRAFTING_TABLE, 1);
//		this.decoSelector.registerEdgeDecor(RoomDecorTypes.FURNACE, 1);
//
//		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 20);
//		this.decoSelector.registerMidDecor(RoomDecorTypes.TABLE_2x2, 1);
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		super.generateRoom(castleOrigin, genArray, dungeon);
//	}
//
//	@Override
//	public ResourceLocation[] getChestIDs() {
//		return new ResourceLocation[] { CQRLoottables.CHESTS_FOOD };
//	}
//}
