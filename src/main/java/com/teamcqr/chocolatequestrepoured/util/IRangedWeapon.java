package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IRangedWeapon {

	public default int getCooldown() {
		return 60;
	}

	public default void shoot(World world, Entity shooter, Entity target) {
		this.shoot(world, shooter, target.posX, target.posY + (double) target.height * 0.5D, target.posX);
	}

	public void shoot(World world, Entity shooter, double x, double y, double z);

}
