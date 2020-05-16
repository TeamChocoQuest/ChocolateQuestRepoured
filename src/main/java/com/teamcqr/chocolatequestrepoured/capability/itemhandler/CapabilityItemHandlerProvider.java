package com.teamcqr.chocolatequestrepoured.capability.itemhandler;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityItemHandlerProvider extends CapabilityProviderCQR<IItemHandler> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "item_stack_handler");

	public CapabilityItemHandlerProvider(Capability<IItemHandler> capability, IItemHandler instance) {
		super(capability, instance);
	}

	public static CapabilityItemHandlerProvider createProvider(int slots) {
		return new CapabilityItemHandlerProvider(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new ItemStackHandler(slots));
	}

}
