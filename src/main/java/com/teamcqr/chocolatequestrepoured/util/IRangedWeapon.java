package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IRangedWeapon {

	public default int getCooldown() {
		return 60;
	}

	public SoundEvent getShootSound();

	public void shoot(World world, EntityLivingBase shooter, Entity target, EnumHand hand);

}
