package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.AbandonedGenerator;

public class AbandonedDungeon extends DefaultSurfaceDungeon {
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new AbandonedGenerator();
	}
	

}
