package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;

public interface ICQRSection {

	@Nullable
	BlockState getBlockState(BlockPos pos);

	default void setBlockState(BlockPos pos, @Nullable BlockState state) {
		this.setBlockState(pos, state, null);
	}

	void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<BlockEntity> blockEntityCallback);

	@Nullable
    FluidState getFluidState(BlockPos pos);

	@Nullable
    BlockEntity getBlockEntity(BlockPos pos);

	void addEntity(Entity entity);

}
