package com.teamcqr.chocolatequestrepoured.init;

import com.teamcqr.chocolatequestrepoured.capability.armor.slime.CapabilitySlimeArmorProvider;
import com.teamcqr.chocolatequestrepoured.capability.armor.turtle.CapabilityTurtleArmorProvider;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.capability.structureselector.CapabilityStructureSelectorProvider;

public class ModCapabilities {

	public static void registerCapabilities() {
		CapabilityTurtleArmorProvider.register();
		CapabilitySlimeArmorProvider.register();
		CapabilityExtraItemHandlerProvider.register();
		CapabilityStructureSelectorProvider.register();
	}

}
