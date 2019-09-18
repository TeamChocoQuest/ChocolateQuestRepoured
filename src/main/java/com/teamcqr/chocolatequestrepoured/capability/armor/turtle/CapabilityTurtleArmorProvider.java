package com.teamcqr.chocolatequestrepoured.capability.armor.turtle;

import com.teamcqr.chocolatequestrepoured.capability.CapabilityProviderCQR;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmorHandler;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilitySpecialArmorStorage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTurtleArmorProvider extends CapabilityProviderCQR<CapabilityTurtleArmor> {

	@CapabilityInject(CapabilityTurtleArmor.class)
	public static final Capability<CapabilityTurtleArmor> CAPABILITY_TURTLE_ARMOR = null;

	public CapabilityTurtleArmorProvider(Capability<CapabilityTurtleArmor> capability, CapabilityTurtleArmor instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityTurtleArmor.class, new CapabilitySpecialArmorStorage(), CapabilityTurtleArmor::new);
		CapabilitySpecialArmorHandler.addCapability(CAPABILITY_TURTLE_ARMOR);
	}

	public static CapabilityTurtleArmorProvider createProvider() {
		return new CapabilityTurtleArmorProvider(CAPABILITY_TURTLE_ARMOR, new CapabilityTurtleArmor());
	}

}
