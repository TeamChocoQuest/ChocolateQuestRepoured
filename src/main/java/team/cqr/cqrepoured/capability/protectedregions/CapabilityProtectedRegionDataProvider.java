package team.cqr.cqrepoured.capability.protectedregions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class CapabilityProtectedRegionDataProvider extends SerializableCapabilityProvider<CapabilityProtectedRegionData> {

	public static final ResourceLocation LOCATION = new ResourceLocation(CQRConstants.MODID, "protected_region_data");

	public CapabilityProtectedRegionDataProvider(Capability<CapabilityProtectedRegionData> capability, CapabilityProtectedRegionData instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static CapabilityProtectedRegionDataProvider createProvider(LevelChunk chunk) {
		return new CapabilityProtectedRegionDataProvider(CQRCapabilities.PROTECTED_REGION_DATA, new CapabilityProtectedRegionDataImplementation(chunk));
	}

	public static CapabilityProtectedRegionData get(LevelChunk chunk) {
		return chunk.getCapability(CQRCapabilities.PROTECTED_REGION_DATA)
				.orElseThrow(NullPointerException::new);
	}

}
