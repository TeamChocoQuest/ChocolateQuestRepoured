package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

public class PreparableForceFieldNexusInfo extends PreparablePosInfo {

	@Override
	protected void prepareNormal(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		if (placement.protectedRegionBuilder().isEmpty()) {
			level.setBlockState(transformedPos, Blocks.AIR.defaultBlockState());
		} else {
			level.setBlockState(transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.get().defaultBlockState());
			placement.protectedRegionBuilder().get().addBlock(transformedPos);
		}
	}

	@Override
	protected void prepareDebug(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.get().defaultBlockState());
	}

	public static class Factory implements IFactory<TileEntityForceFieldNexus> {

		@Override
		public PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<TileEntityForceFieldNexus> blockEntityLazy) {
			return new PreparableForceFieldNexusInfo();
		}

	}

	public static class Serializer implements ISerializer<PreparableForceFieldNexusInfo> {

		@Override
		public void write(PreparableForceFieldNexusInfo preparable, ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			// nothing to write
		}

		@Override
		public PreparableForceFieldNexusInfo read(ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			return new PreparableForceFieldNexusInfo();
		}

	}

}
