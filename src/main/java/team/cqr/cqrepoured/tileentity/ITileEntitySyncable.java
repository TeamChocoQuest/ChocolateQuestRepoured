package team.cqr.cqrepoured.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public interface ITileEntitySyncable extends ITickableTileEntity {

	@Override
	default void tick() {
		this.getDataManager().checkIfDirtyAndSync();
	}

	TileEntityDataManager getDataManager();

}
