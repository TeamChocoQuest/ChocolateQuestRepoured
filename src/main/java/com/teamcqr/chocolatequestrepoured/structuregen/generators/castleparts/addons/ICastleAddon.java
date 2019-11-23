package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Copyright (c) 01.07.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public interface ICastleAddon
{
    public void generate(World world, CastleDungeon dungeon);
}
