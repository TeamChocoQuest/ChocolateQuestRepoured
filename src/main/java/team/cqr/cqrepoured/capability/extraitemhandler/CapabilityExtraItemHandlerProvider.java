package team.cqr.cqrepoured.capability.extraitemhandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullSupplier;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityExtraItemHandlerProvider extends SerializableCapabilityProvider<CapabilityExtraItemHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRMain.MODID, "extra_item_slot");

	@CapabilityInject(CapabilityExtraItemHandler.class)
	public static final Capability<CapabilityExtraItemHandler> EXTRA_ITEM_HANDLER = null;

	public CapabilityExtraItemHandlerProvider(Capability<CapabilityExtraItemHandler> capability, NonNullSupplier<CapabilityExtraItemHandler> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityExtraItemHandler.class, new CapabilityExtraItemHandlerStorage(), CapabilityExtraItemHandler::new);
	}

	public static CapabilityExtraItemHandlerProvider createProvider(int slots) {
		return new CapabilityExtraItemHandlerProvider(EXTRA_ITEM_HANDLER, () -> new CapabilityExtraItemHandler(slots));
	}

}
