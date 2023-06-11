package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface ICQRSection {

	@Nullable
	BlockState getBlockState(BlockPos pos);

	default void setBlockState(BlockPos pos, @Nullable BlockState state) {
		this.setBlockState(pos, state, null);
	}

	void setBlockState(BlockPos pos, @Nullable BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback);

	@Nullable
	FluidState getFluidState(BlockPos pos);

	@Nullable
	TileEntity getBlockEntity(BlockPos pos);

	void addEntity(Entity entity);

}
