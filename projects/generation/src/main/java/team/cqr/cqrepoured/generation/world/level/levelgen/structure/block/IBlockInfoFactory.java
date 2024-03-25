package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public interface IBlockInfoFactory<T extends BlockEntity> {

	default IBlockInfo create(Level level, BlockPos pos, BlockState blockState) {
		return this.create(level, pos, blockState, LazyOptional.of(blockState.hasBlockEntity() ? () -> level.getBlockEntity(pos) : null)
				.cast());
	}

	IBlockInfo create(Level level, BlockPos pos, BlockState blockState, LazyOptional<T> blockEntitySupplier);

	@Nullable
	static CompoundTag writeBlockEntityToNBT(@Nullable BlockEntity blockEntity) {
		if (blockEntity == null) {
			return null;
		}
		return blockEntity.saveWithoutMetadata();
	}

}
