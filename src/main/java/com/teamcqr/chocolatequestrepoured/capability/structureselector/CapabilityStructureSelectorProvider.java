package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityStructureSelectorProvider extends CapabilityProviderCQR<CapabilityStructureSelector> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Reference.MODID, "structure_selector");

	@CapabilityInject(CapabilityStructureSelector.class)
	public static final Capability<CapabilityStructureSelector> STRUCTURE_SELECTOR = null;

	public CapabilityStructureSelectorProvider(Capability<CapabilityStructureSelector> capability, CapabilityStructureSelector instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityStructureSelector.class, new CapabilityStructureSelectorStorage(), CapabilityStructureSelector::new);
	}

	public static CapabilityStructureSelectorProvider createProvider(ItemStack stack) {
		return new CapabilityStructureSelectorProvider(STRUCTURE_SELECTOR, new CapabilityStructureSelector(stack));
	}

}
