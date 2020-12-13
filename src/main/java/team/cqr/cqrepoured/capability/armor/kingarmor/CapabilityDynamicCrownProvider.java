package team.cqr.cqrepoured.capability.armor.kingarmor;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityDynamicCrownProvider extends SerializableCapabilityProvider<CapabilityDynamicCrown> {

	@CapabilityInject(CapabilityDynamicCrown.class)
	public static final Capability<CapabilityDynamicCrown> DYNAMIC_CROWN = null;

	public CapabilityDynamicCrownProvider(Capability<CapabilityDynamicCrown> capability, CapabilityDynamicCrown instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityDynamicCrown.class, new CapabilityDynamicCrownStorage(), CapabilityDynamicCrown::new);
	}

	public static CapabilityDynamicCrownProvider createProvider() {
		return new CapabilityDynamicCrownProvider(DYNAMIC_CROWN, new CapabilityDynamicCrown());
	}

}
