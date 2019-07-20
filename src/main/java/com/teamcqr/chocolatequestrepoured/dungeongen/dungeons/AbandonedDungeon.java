package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.AbandonedGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.IDungeonGenerator;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
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
