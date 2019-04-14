package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.StrongholdGenerator;

public class StrongholdDungeon extends DungeonBase {
	
	private File stairFolder;
	private File bossRoomFolder;
	private File entranceStairFolder;
	private File entranceBuildingFolder;
	private File roomFolder;

	//Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon
	
	public StrongholdDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdGenerator();
	}
}
