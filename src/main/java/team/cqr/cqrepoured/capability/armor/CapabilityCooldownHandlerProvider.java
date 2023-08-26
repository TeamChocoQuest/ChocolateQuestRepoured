package team.cqr.cqrepoured.capability.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullSupplier;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityCooldownHandlerProvider extends SerializableCapabilityProvider<CapabilityCooldownHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRConstants.MODID, "item_cooldown_handler");

	@CapabilityInject(CapabilityCooldownHandler.class)
	public static final Capability<CapabilityCooldownHandler> CAPABILITY_ITEM_COOLDOWN_CQR = null;

	public CapabilityCooldownHandlerProvider(Capability<CapabilityCooldownHandler> capability, NonNullSupplier<CapabilityCooldownHandler> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityCooldownHandler.class, new CapabilityCooldownHandlerStorage(), CapabilityCooldownHandler::new);
	}

	public static CapabilityCooldownHandlerProvider createProvider() {
		return new CapabilityCooldownHandlerProvider(CAPABILITY_ITEM_COOLDOWN_CQR, CapabilityCooldownHandler::new);
	}

}
