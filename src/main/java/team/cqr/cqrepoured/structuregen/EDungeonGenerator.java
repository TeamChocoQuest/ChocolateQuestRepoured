package team.cqr.cqrepoured.structuregen;

import java.util.Properties;

import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonGridCity;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonGuardedCastle;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonHangingCity;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonStrongholdOpen;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonTemplateSurface;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonVegetatedCave;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonVolcano;

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

	OPEN_STRONGHOLD(DungeonStrongholdOpen::new),
	GUARDED_CASTLE(DungeonGuardedCastle::new),

	ABANDONED((name, prop) -> null),
	RUIN((name, prop) -> null);

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
		DungeonBase createDungeon(String name, Properties prop);
	}

}
