package team.cqr.cqrepoured.structuregen.generators.castleparts.addons;

import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;

/**
 * Copyright (c) 01.07.2019 Developed by KalgogSmash: https://github.com/kalgogsmash
 */
public interface ICastleAddon {
	void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon);
}
