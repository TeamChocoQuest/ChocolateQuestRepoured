package com.teamcqr.chocolatequestrepoured.capability;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

public class CapabilityDungeonPlacer implements ICapabilityDungeonPlacer {

	protected DungeonBase dungeon;

	@Override
	public DungeonBase getDungeon() {
		return dungeon;
	}

	@Override
	public void setDungeon(DungeonBase dungeon) {
		this.dungeon = dungeon;
	}

}
