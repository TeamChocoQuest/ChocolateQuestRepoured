package team.cqr.cqrepoured.capability.armor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityCooldownHandlerHelper {

	public static int getCooldown(LivingEntity entity, Item item) {
		LazyOptional<CapabilityArmorCooldown> lOpCap = entity.getCapability(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		if(lOpCap.isPresent()) {
			return lOpCap.resolve().get().getCooldown(item);
		}
		return 0;
	}

	public static void setCooldown(LivingEntity entity, Item item, int cooldown) {
		LazyOptional<CapabilityArmorCooldown> lOptCap = entity.getCapability(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, null);
		if(lOptCap.isPresent()) {
			lOptCap.resolve().get().setCooldown(item, cooldown);
		}
	}

	public static boolean onCooldown(LivingEntity entity, Item item) {
		return getCooldown(entity, item) > 0;
	}

}
