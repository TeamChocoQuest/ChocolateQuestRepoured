package com.teamcqr.chocolatequestrepoured.capability.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;

public class CapabilityCooldownHandlerHelper {

	public static int getCooldown(EntityLivingBase entity, Item item) {
		CapabilityCooldownHandler icapability = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		return icapability.getCooldown(item);
	}

	public static void setCooldown(EntityLivingBase entity, Item item, int cooldown) {
		CapabilityCooldownHandler icapability = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		icapability.setCooldown(item, cooldown);
	}

	public static boolean onCooldown(EntityLivingBase entity, Item item) {
		return getCooldown(entity, item) > 0;
	}

}
