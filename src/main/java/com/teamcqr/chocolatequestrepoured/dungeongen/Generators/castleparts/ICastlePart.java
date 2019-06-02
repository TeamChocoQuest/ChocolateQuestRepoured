package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import net.minecraft.world.World;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public interface ICastlePart
{
    void generatePart(World world, int x, int y, int z, int sizeX, int sizeZ, int roomSize, int floors);
}
