package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonGridCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonHangingCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonTemplateSurface;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVegetatedCave;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public enum EDungeonGenerator {

	TEMPLATE_SURFACE(DungeonTemplateSurface::new),
	RANDOMIZED_CASTLE(DungeonRandomizedCastle::new),
	VOLCANO(DungeonVolcano::new),
	GRID_CITY(DungeonGridCity::new),
	HANGING_CITY(DungeonHangingCity::new),
	LINEAR_STRONGHOLD(DungeonStrongholdLinear::new),
	VEGETATED_CAVE(DungeonVegetatedCave::new),
	
	CAVERNS((name, prop) -> null),
	ABANDONED((name, prop) -> null),
	RUIN((name, prop) -> null),
	OPEN_STRONGHOLD((name, prop) -> null),
	GUARDED_CASTLE((name, prop) -> null);

	private IDungeonGenerator generator;

	EDungeonGenerator(IDungeonGenerator generator) {
		this.generator = generator;
	}

	public DungeonBase createDungeon(String name, Properties prop) {
		return this.generator.createDungeon(name, prop);
	}

	public static EDungeonGenerator getDungeonGenerator(String toTest) {
		try {
			return EDungeonGenerator.valueOf(toTest.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@FunctionalInterface
	private static interface IDungeonGenerator {
		public DungeonBase createDungeon(String name, Properties prop);
	}

}
