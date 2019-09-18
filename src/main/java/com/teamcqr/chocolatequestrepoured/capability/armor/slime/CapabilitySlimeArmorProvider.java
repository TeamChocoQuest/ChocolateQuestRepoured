package com.teamcqr.chocolatequestrepoured.capability.armor.slime;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmorHandler;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmorStorage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitySlimeArmorProvider extends CapabilityProviderCQR<CapabilitySlimeArmor> {

	@CapabilityInject(CapabilitySlimeArmor.class)
	public static final Capability<CapabilitySlimeArmor> CAPABILITY_SLIME_ARMOR = null;

	public CapabilitySlimeArmorProvider(Capability<CapabilitySlimeArmor> capability, CapabilitySlimeArmor instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilitySlimeArmor.class, new CapabilitySpecialArmorStorage(), CapabilitySlimeArmor::new);
		CapabilitySpecialArmorHandler.addCapability(CAPABILITY_SLIME_ARMOR);
	}

	public static CapabilitySlimeArmorProvider createProvider() {
		return new CapabilitySlimeArmorProvider(CAPABILITY_SLIME_ARMOR, new CapabilitySlimeArmor());
	}

}
