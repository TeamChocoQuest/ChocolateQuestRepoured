package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCavern;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonGuardedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonHangingCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdOpen;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonSurface;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVegetatedCave;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public enum EDungeonGenerator {

	CAVERNS(DungeonCavern::new),
	ABANDONED((name, prop) -> null),
	RUIN((name, prop) -> null),
	NETHER_CITY(DungeonNetherCity::new),
	FLOATING_NETHER_CITY(DungeonHangingCity::new),
	TEMPLATE_SURFACE(DungeonSurface::new),
	TEMPLATE_OCEAN_FLOOR(DungeonOceanFloor::new),
	STRONGHOLD(DungeonStrongholdOpen::new),
	CLASSIC_STRONGHOLD(DungeonStrongholdLinear::new),
	GREEN_CAVE(DungeonVegetatedCave::new),
	GUARDED_CASTLE(DungeonGuardedCastle::new),
	CASTLE(DungeonCastle::new),
	VOLCANO(DungeonVolcano::new);

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
