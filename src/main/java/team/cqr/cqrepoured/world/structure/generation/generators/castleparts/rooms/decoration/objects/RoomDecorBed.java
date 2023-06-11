//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.world.level.block.BedBlock;
//import net.minecraft.world.level.block.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.HorizontalBlock;
//import net.minecraft.util.Direction;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//
//public class RoomDecorBed extends RoomDecorBlocksBase {
//	public RoomDecorBed() {
//		super();
//	}
//
//	@Override
//	protected void makeSchematic() {
//		BlockState head = Blocks.BED.getDefaultState().withProperty(BedBlock.PART, BedBlock.EnumPartType.HEAD);
//		this.schematic.add(new DecoBlockRotating(0, 0, 0, head, HorizontalBlock.FACING, Direction.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
//		BlockState foot = Blocks.BED.getDefaultState().withProperty(BedBlock.PART, BedBlock.EnumPartType.FOOT);
//		this.schematic.add(new DecoBlockRotating(0, 0, 1, foot, HorizontalBlock.FACING, Direction.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
//	}
//}
