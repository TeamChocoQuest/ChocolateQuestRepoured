package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.RuinGenerator;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class RuinDungeon extends DefaultSurfaceDungeon {

	public RuinDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new RuinGenerator();
	}
}
