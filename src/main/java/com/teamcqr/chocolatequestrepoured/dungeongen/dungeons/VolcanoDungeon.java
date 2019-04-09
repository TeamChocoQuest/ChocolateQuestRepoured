package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VolcanoGenerator;

public class VolcanoDungeon extends StrongholdDungeon {

	public VolcanoDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator();
	}
}
