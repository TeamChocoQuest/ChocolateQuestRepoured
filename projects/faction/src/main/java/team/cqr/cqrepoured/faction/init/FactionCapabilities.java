package team.cqr.cqrepoured.faction.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import team.cqr.cqrepoured.faction.capability.IFactionRelationCapability;

public class FactionCapabilities {

	public static final Capability<IFactionRelationCapability> FACTION_RELATION = CapabilityManager.get(new CapabilityToken<>() {});
	
}
