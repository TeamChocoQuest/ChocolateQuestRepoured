package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.StrongholdGenerator;

public class StrongholdDungeon extends DungeonBase {
	
	private File stairFolder;
	private File bossRoomFolder;
	private File CorridorNSFolder;
	private File CorridorEWFolder;

	public StrongholdDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdGenerator();
	}
}
