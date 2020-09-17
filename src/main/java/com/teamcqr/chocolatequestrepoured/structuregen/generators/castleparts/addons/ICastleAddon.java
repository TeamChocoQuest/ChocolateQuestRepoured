package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

/**
 * Copyright (c) 01.07.2019 Developed by KalgogSmash: https://github.com/kalgogsmash
 */
public interface ICastleAddon {
	void generate(BlockStateGenArray genArray, DungeonRandomizedCastle dungeon);
}
