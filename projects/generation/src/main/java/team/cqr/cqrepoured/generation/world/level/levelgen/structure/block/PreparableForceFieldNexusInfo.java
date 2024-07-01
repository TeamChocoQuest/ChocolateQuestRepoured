package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.generation.init.CQRBlocks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class PreparableForceFieldNexusInfo implements IBlockInfo {

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		if (placement.protectedRegionBuilder().isEmpty()) {
			level.setBlockState(transformedPos, Blocks.AIR.defaultBlockState());
		} else {
			level.setBlockState(transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.get().defaultBlockState());
			placement.protectedRegionBuilder().get().addBlock(transformedPos);
		}
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.get().defaultBlockState());
	}

	public static class Factory implements IBlockInfoFactory<TileEntityForceFieldNexus> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<TileEntityForceFieldNexus> blockEntitySupplier) {
			return new PreparableForceFieldNexusInfo();
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableForceFieldNexusInfo> {

		@Override
		public void write(PreparableForceFieldNexusInfo forceFieldNexusInfo, DataOutput out, SimplePalette palette) throws IOException {
			// nothing to write
		}

		@Override
		public PreparableForceFieldNexusInfo read(DataInput in, SimplePalette palette) throws IOException {
			return new PreparableForceFieldNexusInfo();
		}

	}

}
