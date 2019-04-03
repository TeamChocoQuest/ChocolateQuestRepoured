package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VillageGenerator;

public class VillageDungeon extends DungeonBase {

	@Override
	public IDungeonGenerator getGenerator() {
		return new VillageGenerator();
	}
}
