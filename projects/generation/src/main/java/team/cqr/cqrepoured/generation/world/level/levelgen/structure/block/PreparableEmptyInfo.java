package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.init.CQRBlocks;

public class PreparableEmptyInfo extends PreparablePosInfo {

	@Override
	protected void prepareNormal(CQRLevel level, BlockPos pos, DungeonPlacement placement) {

	}

	@Override
	protected void prepareDebug(CQRLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);

		level.setBlockState(transformedPos, CQRBlocks.NULL_BLOCK.get().defaultBlockState());
	}

	public static class Factory implements IFactory<BlockEntity> {

		@Override
		public PreparablePosInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<BlockEntity> blockEntityLazy) {
			return new PreparableEmptyInfo();
		}

	}

	public static class Serializer implements ISerializer<PreparableEmptyInfo> {

		@Override
		public void write(PreparableEmptyInfo preparable, ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			// nothing to write
		}

		@Override
		public PreparableEmptyInfo read(ByteBuf buf, SimplePalette palette, ListTag nbtList) {
			return new PreparableEmptyInfo();
		}

	}

}
