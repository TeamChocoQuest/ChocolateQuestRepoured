package team.cqr.cqrepoured.capability.armor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityCooldownHandlerHelper {

	public static int getCooldown(LivingEntity entity, Item item) {
		LazyOptional<CapabilityCooldownHandler> lOpCap = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		if(lOpCap.isPresent()) {
			return lOpCap.resolve().get().getCooldown(item);
		}
		return 0;
	}

	public static void setCooldown(LivingEntity entity, Item item, int cooldown) {
		LazyOptional<CapabilityCooldownHandler> lOptCap = entity.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		if(lOptCap.isPresent()) {
			lOptCap.resolve().get().setCooldown(item, cooldown);
		}
	}

	public static boolean onCooldown(LivingEntity entity, Item item) {
		return getCooldown(entity, item) > 0;
	}

}
