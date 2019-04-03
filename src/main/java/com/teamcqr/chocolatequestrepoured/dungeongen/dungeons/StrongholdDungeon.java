package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.StrongholdGenerator;

public class StrongholdDungeon extends DungeonBase {

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdGenerator();
	}
}
