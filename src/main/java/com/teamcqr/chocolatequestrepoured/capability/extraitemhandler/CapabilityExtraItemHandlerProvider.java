package com.teamcqr.chocolatequestrepoured.capability.extraitemhandler;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityExtraItemHandlerProvider extends CapabilityProviderCQR<CapabilityExtraItemHandler> {

	@CapabilityInject(CapabilityExtraItemHandler.class)
	public static final Capability<CapabilityExtraItemHandler> EXTRA_ITEM_HANDLER = null;

	public CapabilityExtraItemHandlerProvider(Capability<CapabilityExtraItemHandler> capability, CapabilityExtraItemHandler instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityExtraItemHandler.class, new CapabilityExtraItemHandlerStorage(), CapabilityExtraItemHandler::new);
	}

	public static CapabilityExtraItemHandlerProvider createProvider(int slots) {
		return new CapabilityExtraItemHandlerProvider(EXTRA_ITEM_HANDLER, new CapabilityExtraItemHandler(slots));
	}

}
