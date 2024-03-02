package team.cqr.cqrepoured.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import team.cqr.cqrepoured.capability.armor.CapabilityArmorCooldown;
import team.cqr.cqrepoured.capability.armor.attachment.ICapabilityArmorAttachment;
import team.cqr.cqrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import team.cqr.cqrepoured.capability.electric.IElectricShockCapability;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.capability.itemhandler.CapabilitySerializableItemHandler;
import team.cqr.cqrepoured.capability.pathtool.ICapabilityPath;
import team.cqr.cqrepoured.protection.capability.CapabilityProtectedRegionData;

public class CQRCapabilities {

	public static void registerCapabilities() {
		CapabilityDynamicCrownProvider.register();
		//CapabilityPathProvider.register();
	}
	
	public static final Capability<IFactionRelationCapability> FACTION_RELATION = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<IElectricShockCapability> ELECTRIC_SPREAD = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CapabilityExtraItemHandler> EXTRA_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CapabilityArmorCooldown> CAPABILITY_ITEM_COOLDOWN_CQR = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CapabilitySerializableItemHandler> SERIALIZABLE_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CapabilityProtectedRegionData> PROTECTED_REGION_DATA = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<ICapabilityPath> PATH = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<ICapabilityArmorAttachment> ARMOR_ATTACHMENT = CapabilityManager.get(new CapabilityToken<>() {});

}
