package team.cqr.cqrepoured.capability.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;

public class CapabilityCooldownHandlerHelper {

	public static int getCooldown(LivingEntity entity, Item item) {
		CapabilityCooldownHandler icapability = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		return icapability.getCooldown(item);
	}

	public static void setCooldown(LivingEntity entity, Item item, int cooldown) {
		CapabilityCooldownHandler icapability = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		icapability.setCooldown(item, cooldown);
	}

	public static boolean onCooldown(LivingEntity entity, Item item) {
		return getCooldown(entity, item) > 0;
	}

}
