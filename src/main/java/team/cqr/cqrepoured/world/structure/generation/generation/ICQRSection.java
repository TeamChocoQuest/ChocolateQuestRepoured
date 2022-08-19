package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

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
