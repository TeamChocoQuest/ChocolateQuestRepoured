package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public interface ITileEntitySyncable<T extends BlockEntity> extends BlockEntityTicker<T> {

	@Override
	default void tick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
		this.getDataManager().checkIfDirtyAndSync();
	}

	TileEntityDataManager getDataManager();

}
