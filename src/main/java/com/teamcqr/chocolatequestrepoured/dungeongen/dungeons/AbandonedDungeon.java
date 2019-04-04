package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.AbandonedGenerator;

public class AbandonedDungeon extends DefaultSurfaceDungeon {
	
	public AbandonedDungeon(File configFile) {
		super(configFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new AbandonedGenerator();
	}
	

}
