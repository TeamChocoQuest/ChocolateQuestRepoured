package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VolcanoGenerator;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoDungeon extends StrongholdDungeon {
	
	private boolean buildStairwell = true;
	private int minHeight = 100;
	private int maxHeight = 130;
	private double steepness = 0.075D;
	private double lavaChance = 0.005D;
	private double magmaChance = 0.1;
	private int innerRadius = 6;

	public VolcanoDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator();
	}
}
