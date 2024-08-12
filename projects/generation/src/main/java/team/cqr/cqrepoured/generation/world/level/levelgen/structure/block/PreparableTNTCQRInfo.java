package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.blocks.block.CQRTNTBlock;
import team.cqr.cqrepoured.blocks.init.CQRBlocksBlocks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;

public class PreparableTNTCQRInfo implements IBlockInfo {

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocksBlocks.CQR_TNT.get().defaultBlockState().setValue(CQRTNTBlock.HIDDEN, true));
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocksBlocks.CQR_TNT.get().defaultBlockState().setValue(CQRTNTBlock.HIDDEN, false));
	}

	public static class Factory implements IBlockInfoFactory<BlockEntity> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<BlockEntity> blockEntitySupplier) {
			return new PreparableTNTCQRInfo();
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableTNTCQRInfo> {

		@Override
		public void write(PreparableTNTCQRInfo cqrTntInfo, DataOutput out, SimplePalette palette) throws IOException {
			// nothing to write
		}

		@Override
		public PreparableTNTCQRInfo read(DataInput in, SimplePalette palette) throws IOException {
			return new PreparableTNTCQRInfo();
		}

	}

}
