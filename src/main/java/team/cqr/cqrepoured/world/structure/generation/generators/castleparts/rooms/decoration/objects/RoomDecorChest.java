//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.ChestBlock;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.ChestTileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBase;
//
//import java.util.Set;
//
//public class RoomDecorChest extends RoomDecorBlocksBase {
//	public RoomDecorChest() {
//		super();
//	}
//
//	@Override
//	protected void makeSchematic() {
//		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.CHEST.getDefaultState(), ChestBlock.FACING, Direction.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
//	}
//
//	@Override
//	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, Direction side, Set<BlockPos> decoMap) {
//		// super.build(world, genArray, room, dungeon, start, side, decoMap);
//
//		ResourceLocation[] chestIDs = room.getChestIDs();
//		if (chestIDs != null && chestIDs.length > 0) {
//			Block chestBlock = Blocks.CHEST;
//			BlockState state = this.schematic.get(0).getState(side);
//			ChestTileEntity chest = (ChestTileEntity) chestBlock.createTileEntity(world, state);
//			if (chest != null) {
//				ResourceLocation resLoc = chestIDs[genArray.getRandom().nextInt(chestIDs.length)];
//				if (resLoc != null) {
//					long seed = WorldDungeonGenerator.getSeed(world, start.getX() + start.getY(), start.getZ() + start.getY());
//					chest.setLootTable(resLoc, seed);
//				}
//				CompoundNBT nbt = chest.writeToNBT(new CompoundNBT());
//				genArray.addBlockState(start, state, nbt, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.HIGH);
//				decoMap.add(start);
//			}
//		} else {
//			// CQRMain.logger.warn("Placed a chest but could not find a loot table for Room Type {}", room.getRoomType());
//			// TODO fix rooms having no chests (or is this intended?)
//		}
//	}
//}
