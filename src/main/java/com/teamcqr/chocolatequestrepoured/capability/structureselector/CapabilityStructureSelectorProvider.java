package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityStructureSelectorProvider extends CapabilityProviderCQR<CapabilityStructureSelector> {

	@CapabilityInject(CapabilityStructureSelector.class)
	public static final Capability<CapabilityStructureSelector> STRUCTURE_SELECTOR = null;

	public CapabilityStructureSelectorProvider(Capability<CapabilityStructureSelector> capability, CapabilityStructureSelector instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityStructureSelector.class, new CapabilityStructureSelectorStorage(), CapabilityStructureSelector::new);
	}

	public static CapabilityStructureSelectorProvider createProvider() {
		return new CapabilityStructureSelectorProvider(STRUCTURE_SELECTOR, new CapabilityStructureSelector());
	}

}
