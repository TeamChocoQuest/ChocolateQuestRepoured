package team.cqr.cqrepoured.init;

import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.capability.pathtool.CapabilityPathProvider;

public class CQRCapabilities {

	public static void registerCapabilities() {
		CapabilityCooldownHandlerProvider.register();
		CapabilityExtraItemHandlerProvider.register();
		CapabilityDynamicCrownProvider.register();
		CapabilityPathProvider.register();
	}

}
