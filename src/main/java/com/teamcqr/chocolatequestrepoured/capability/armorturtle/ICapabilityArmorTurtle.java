package com.teamcqr.chocolatequestrepoured.capability.armorturtle;

import net.minecraft.entity.EntityLivingBase;

public interface ICapabilityArmorTurtle {

	public void applyContinousEffect(EntityLivingBase entity);

	public void applyEffect(EntityLivingBase entity);

	public int getCooldown();

	public void setCooldown(int cooldown);

}
