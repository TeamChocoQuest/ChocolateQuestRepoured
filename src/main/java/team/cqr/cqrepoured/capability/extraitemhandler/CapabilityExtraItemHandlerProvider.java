package team.cqr.cqrepoured.capability.extraitemhandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityExtraItemHandlerProvider extends SerializableCapabilityProvider<CapabilityExtraItemHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRConstants.MODID, "extra_item_slot");

	public CapabilityExtraItemHandlerProvider(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler defaultValue) {
		super(capability, defaultValue);
	}

	public static CapabilityExtraItemHandlerProvider createProvider(int slots) {
		return new CapabilityExtraItemHandlerProvider(CQRCapabilities.EXTRA_ITEM_HANDLER, new CapabilityExtraItemHandler(slots));
	}

}
