package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IRangedWeapon {

	public void shoot(World world, EntityLivingBase shooter, Entity target, EnumHand hand);

	public SoundEvent getShootSound();

	public double getRange();

	public int getCooldown();

	public int getChargeTicks();

}
