package com.teamcqr.chocolatequestrepoured.capability.armor;

import net.minecraft.item.ItemArmor;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public abstract class CapabilitySpecialArmor {

	protected Class<? extends ItemArmor> itemClass;
	protected int cooldown;

	public CapabilitySpecialArmor(Class<? extends ItemArmor> itemClass) {
		this.itemClass = itemClass;
	}

	public Class<? extends ItemArmor> getItemClass() {
		return this.itemClass;
	}

	public abstract void onLivingUpdateEvent(LivingUpdateEvent event);

	public abstract void onLivingDamageEvent(LivingDamageEvent event);

	public int getCooldown() {
		return this.cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void reduceCooldown() {
		if (this.onCooldown()) {
			this.cooldown--;
		}
	}

	public boolean onCooldown() {
		return this.cooldown > 0;
	}

}
