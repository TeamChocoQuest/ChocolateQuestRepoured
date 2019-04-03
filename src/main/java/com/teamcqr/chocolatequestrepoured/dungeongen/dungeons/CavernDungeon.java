package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.CavernGenerator;

public class CavernDungeon extends DungeonBase{

	@Override
	public IDungeonGenerator getGenerator() {
		return new CavernGenerator();
	}
}
