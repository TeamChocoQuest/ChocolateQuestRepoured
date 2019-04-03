package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.RuinGenerator;

public class RuinDungeon extends DefaultSurfaceDungeon {

	@Override
	public IDungeonGenerator getGenerator() {
		return new RuinGenerator();
	}
}
