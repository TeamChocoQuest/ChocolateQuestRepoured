package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.ClassicNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.FloatingNetherCity;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.GuardedCastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdOpenDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum EDungeonGenerator {

	CAVERNS(CavernDungeon::new),
	ABANDONED((name, prop) -> null),
	RUIN((name, prop) -> null),
	NETHER_CITY(ClassicNetherCity::new),
	FLOATING_NETHER_CITY(FloatingNetherCity::new),
	TEMPLATE_SURFACE(DefaultSurfaceDungeon::new),
	TEMPLATE_OCEAN_FLOOR(DungeonOceanFloor::new),
	STRONGHOLD(StrongholdLinearDungeon::new),
	CLASSIC_STRONGHOLD(StrongholdOpenDungeon::new),
	GREEN_CAVE((name, prop) -> null),
	GUARDED_CASTLE(GuardedCastleDungeon::new),
	CASTLE(CastleDungeon::new),
	VOLCANO(VolcanoDungeon::new);

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
