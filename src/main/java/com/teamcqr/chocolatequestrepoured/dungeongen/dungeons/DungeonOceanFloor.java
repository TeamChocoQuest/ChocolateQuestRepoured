package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.OceanFloorGenerator;

public class DungeonOceanFloor extends DefaultSurfaceDungeon {

	public DungeonOceanFloor(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new OceanFloorGenerator();
	}
}
