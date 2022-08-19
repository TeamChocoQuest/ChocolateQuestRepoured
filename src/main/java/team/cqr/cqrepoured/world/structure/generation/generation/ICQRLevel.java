package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface ICQRLevel {

	long getSeed();

	default void setBlockState(BlockPos pos, BlockState state) {
		this.setBlockState(pos, state, null);
	}

	void setBlockState(BlockPos pos, BlockState state, @Nullable Consumer<TileEntity> blockEntityCallback);

	void addEntity(Entity entity);

}
