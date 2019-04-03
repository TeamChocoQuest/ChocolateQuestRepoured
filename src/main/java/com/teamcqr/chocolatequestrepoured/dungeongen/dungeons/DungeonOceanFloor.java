package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.OceanFloorGenerator;

public class DungeonOceanFloor extends DefaultSurfaceDungeon {

	@Override
	public IDungeonGenerator getGenerator() {
		return new OceanFloorGenerator();
	}
}
