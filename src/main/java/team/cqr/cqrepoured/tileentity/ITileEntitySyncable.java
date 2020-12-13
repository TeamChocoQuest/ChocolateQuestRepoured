package team.cqr.cqrepoured.tileentity;

import net.minecraft.util.ITickable;
import team.cqr.cqrepoured.network.datasync.TileEntityDataManager;

public interface ITileEntitySyncable extends ITickable {

	@Override
	default void update() {
		this.getDataManager().checkIfDirtyAndSync();
	}

	TileEntityDataManager getDataManager();

}
