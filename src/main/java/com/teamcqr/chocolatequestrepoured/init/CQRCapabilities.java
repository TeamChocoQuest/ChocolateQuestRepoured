package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;

public class CQRCapabilities {

	public static void registerCapabilities() {
		CapabilityCooldownHandlerProvider.register();
		CapabilityExtraItemHandlerProvider.register();
		CapabilityDynamicCrownProvider.register();
	}

}
