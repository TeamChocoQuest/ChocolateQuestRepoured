package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.world.World;

/**
 * Copyright (c) 01.07.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public interface ICastleAddon {
	public void generate(BlockStateGenArray genArray, CastleDungeon dungeon);
}
