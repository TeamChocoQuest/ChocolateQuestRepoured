//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.block.SlabBlock;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//
//public class RoomDecorShelf extends RoomDecorBlocksBase {
//	public RoomDecorShelf() {
//		super();
//	}
//
//	@Override
//	protected void makeSchematic() {
//		this.schematic.add(new DecoBlockBase(0, 2, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(SlabBlock.HALF, SlabBlock.EnumBlockHalf.BOTTOM), BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(1, 2, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(SlabBlock.HALF, SlabBlock.EnumBlockHalf.BOTTOM), BlockStateGenArray.GenerationPhase.MAIN));
//
//		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(1, 1, 0, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
//		this.schematic.add(new DecoBlockBase(1, 0, 0, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
//
//	}
//}
