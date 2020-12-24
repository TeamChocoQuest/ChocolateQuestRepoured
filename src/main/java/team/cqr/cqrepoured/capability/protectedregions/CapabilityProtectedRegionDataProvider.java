package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import team.cqr.cqrepoured.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.util.Reference;

public class CapabilityProtectedRegionDataProvider extends SerializableCapabilityProvider<CapabilityProtectedRegionData> {

	public static final ResourceLocation LOCATION = new ResourceLocation(Reference.MODID, "protected_region_data");

	@CapabilityInject(CapabilityProtectedRegionData.class)
	public static final Capability<CapabilityProtectedRegionData> PROTECTED_REGION_DATA = null;

	public CapabilityProtectedRegionDataProvider(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instance) {
		super(capability, instance);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(CapabilityProtectedRegionData.class, new CapabilityProtectedRegionDataStorage(), () -> new CapabilityProtectedRegionData(null));
	}

	public static CapabilityProtectedRegionDataProvider createProvider(Chunk chunk) {
		return new CapabilityProtectedRegionDataProvider(CapabilityProtectedRegionDataProvider.PROTECTED_REGION_DATA, new CapabilityProtectedRegionData(chunk));
	}

}
