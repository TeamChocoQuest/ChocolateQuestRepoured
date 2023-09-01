package team.cqr.cqrepoured.tileentity;

import net.minecraft.world.level.block.entity.TickingBlockEntity;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public interface ITileEntitySyncable extends TickingBlockEntity {

	@Override
	default void tick() {
		this.getDataManager().checkIfDirtyAndSync();
	}

	TileEntityDataManager getDataManager();

}
