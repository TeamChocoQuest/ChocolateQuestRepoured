package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VolcanoGenerator;

public class VolcanoDungeon extends StrongholdDungeon {

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator();
	}
}
