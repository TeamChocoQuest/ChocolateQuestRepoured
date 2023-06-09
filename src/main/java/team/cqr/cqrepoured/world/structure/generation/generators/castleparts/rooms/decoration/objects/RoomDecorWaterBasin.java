//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.world.level.block.BlockState;
//import net.minecraft.world.level.block.BlockStoneBrick;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.StairsBlock;
//import net.minecraft.util.Direction;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//
//public class RoomDecorWaterBasin extends RoomDecorBlocksBase {
//	public RoomDecorWaterBasin() {
//		super();
//	}
//
//	@Override
//	protected void makeSchematic() {
//		final BlockState chiseledStone = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
//		final BlockState stairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();
//
//		this.schematic.add(new DecoBlockBase(0, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockRotating(1, 0, 0, stairs, StairsBlock.FACING, Direction.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(2, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
//
//		this.schematic.add(new DecoBlockRotating(0, 0, 1, stairs, StairsBlock.FACING, Direction.EAST, BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(1, 0, 1, Blocks.WATER.getDefaultState(), BlockStateGenArray.GenerationPhase.POST));
//		this.schematic.add(new DecoBlockRotating(2, 0, 1, stairs, StairsBlock.FACING, Direction.WEST, BlockStateGenArray.GenerationPhase.MAIN));
//
//		this.schematic.add(new DecoBlockBase(0, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockRotating(1, 0, 2, stairs, StairsBlock.FACING, Direction.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(2, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
//	}
//}
