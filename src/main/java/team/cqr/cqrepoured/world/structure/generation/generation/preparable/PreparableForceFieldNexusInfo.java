package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.ICQRLevel;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableForceFieldNexusInfo extends PreparablePosInfo {

	@Override
	protected void prepareNormal(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		if (placement.getProtectedRegionBuilder() == null) {
			level.setBlockState(transformedPos, Blocks.AIR.defaultBlockState());
		} else {
			level.setBlockState(transformedPos, CQRBlocks.FORCE_FIELD_NEXUS.get().defaultBlockState());
			placement.getProtectedRegionBuilder().addBlock(transformedPos);
		}
	}

	@Override
	protected void prepareDebug(ICQRLevel level, BlockPos pos, DungeonPlacement placement) {
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
		public void write(PreparableForceFieldNexusInfo preparable, ByteBuf buf, BlockStatePalette palette, ListTag nbtList) {
			// nothing to write
		}

		@Override
		public PreparableForceFieldNexusInfo read(ByteBuf buf, BlockStatePalette palette, ListTag nbtList) {
			return new PreparableForceFieldNexusInfo();
		}

	}

}
