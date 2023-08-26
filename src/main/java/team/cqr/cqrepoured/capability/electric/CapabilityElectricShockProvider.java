package team.cqr.cqrepoured.capability.electric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.NonNullSupplier;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityElectricShockProvider extends SerializableCapabilityProvider<CapabilityElectricShock> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRConstants.MODID, "entity_electrocute_handler");

	@CapabilityInject(CapabilityElectricShock.class)
	public static final Capability<CapabilityElectricShock> ELECTROCUTE_HANDLER_CQR = null;

	public CapabilityElectricShockProvider(Capability<CapabilityElectricShock> capability, NonNullSupplier<CapabilityElectricShock> instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityElectricShock.class, new CapabilityElectricShockStorage(), () -> new CapabilityElectricShock(null));
	}

	public static CapabilityElectricShockProvider createProvider(LivingEntity entity) {
		return new CapabilityElectricShockProvider(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, () -> new CapabilityElectricShock(entity));
	}

}
