package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.addons;

import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;

/**
 * Copyright (c) 01.07.2019 Developed by KalgogSmash: https://github.com/kalgogsmash
 */
public interface ICastleAddon {
	void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon);
}
