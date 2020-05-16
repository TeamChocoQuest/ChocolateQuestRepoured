package com.teamcqr.chocolatequestrepoured.capability.extraitemhandler;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityExtraItemHandlerProvider extends CapabilityProviderCQR<CapabilityExtraItemHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "extra_item_slot");

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
