package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorBed extends RoomDecorBlocksBase {
	public RoomDecorBed() {
		super();
	}

	@Override
	protected void makeSchematic() {
		IBlockState head = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
		this.schematic.add(new DecoBlockRotating(0, 0, 0, head, BlockHorizontal.FACING, EnumFacing.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
		IBlockState foot = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
		this.schematic.add(new DecoBlockRotating(0, 0, 1, foot, BlockHorizontal.FACING, EnumFacing.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
	}
}
