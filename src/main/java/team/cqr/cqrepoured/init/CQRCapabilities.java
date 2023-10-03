package team.cqr.cqrepoured.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.capability.electric.IElectricShockCapability;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.capability.protectedregions.CapabilityProtectedRegionDataProvider;

public class CQRCapabilities {

	public static void registerCapabilities() {
		CapabilityCooldownHandlerProvider.register();
		CapabilityDynamicCrownProvider.register();
		//CapabilityPathProvider.register();
		CapabilityProtectedRegionDataProvider.register();
		CapabilityElectricShockProvider.register();
	}
	
	public static final Capability<IFactionRelationCapability> FACTION_RELATION = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<IElectricShockCapability> ELECTRIC_SPREAD = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CapabilityExtraItemHandler> EXTRA_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});

}
