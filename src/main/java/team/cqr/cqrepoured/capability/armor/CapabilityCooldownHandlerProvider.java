package team.cqr.cqrepoured.capability.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityCooldownHandlerProvider extends SerializableCapabilityProvider<CapabilityArmorCooldown> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRConstants.MODID, "item_cooldown_handler");

	public CapabilityCooldownHandlerProvider(Capability<CapabilityArmorCooldown> capability, CapabilityArmorCooldown defaultValue) {
		super(capability, defaultValue);
	}

	public static CapabilityCooldownHandlerProvider createProvider() {
		return new CapabilityCooldownHandlerProvider(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, new CapabilityCooldownHandler());
	}

}
