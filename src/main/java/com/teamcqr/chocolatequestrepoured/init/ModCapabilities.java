package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.capability.pathtool.CapabilityPathToolProvider;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.CapabilityStructureSelectorProvider;

public class ModCapabilities {

	public static void registerCapabilities() {
		CapabilityCooldownHandlerProvider.register();
		CapabilityExtraItemHandlerProvider.register();
		CapabilityStructureSelectorProvider.register();
		CapabilityPathToolProvider.register();
		CapabilityDynamicCrownProvider.register();
	}

}
