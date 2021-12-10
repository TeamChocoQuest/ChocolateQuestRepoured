package team.cqr.cqrepoured.capability.electric;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;

public class CapabilityElectricShockProvider extends SerializableCapabilityProvider<CapabilityElectricShock> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(CQRMain.MODID, "entity_electrocute_handler");

	@CapabilityInject(CapabilityElectricShock.class)
	public static final Capability<CapabilityElectricShock> ELECTROCUTE_HANDLER_CQR = null;

	public CapabilityElectricShockProvider(Capability<CapabilityElectricShock> capability, CapabilityElectricShock instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityElectricShock.class, new CapabilityElectricShockStorage(), () -> new CapabilityElectricShock(null));
	}

	public static CapabilityElectricShockProvider createProvider(EntityLivingBase entity) {
		return new CapabilityElectricShockProvider(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, new CapabilityElectricShock(entity));
	}

}
