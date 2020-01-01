package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts;

import net.minecraft.world.World;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash: https://github.com/kalgogsmash
 */
public interface ICastlePart {
	void generatePart(World world);

	boolean isTower();

	void setAsTopFloor();

	int getStartLayer();
}
