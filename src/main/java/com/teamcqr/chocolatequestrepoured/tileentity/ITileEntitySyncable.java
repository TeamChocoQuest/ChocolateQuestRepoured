package com.teamcqr.chocolatequestrepoured.tileentity;

import com.teamcqr.chocolatequestrepoured.network.datasync.TileEntityDataManager;

import net.minecraft.util.ITickable;

public interface ITileEntitySyncable extends ITickable {

	@Override
	default void update() {
		this.getDataManager().checkIfDirtyAndSync();
	}

	TileEntityDataManager getDataManager();

}
