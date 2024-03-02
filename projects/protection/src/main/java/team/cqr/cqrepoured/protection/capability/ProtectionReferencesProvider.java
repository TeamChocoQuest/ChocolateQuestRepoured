package team.cqr.cqrepoured.protection.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;
import team.cqr.cqrepoured.init.CQRCapabilities;

public class ProtectionReferencesProvider extends SerializableCapabilityProvider<ProtectionReferences> {

	public static final ResourceLocation LOCATION = new ResourceLocation(CQRConstants.MODID, "protected_region_data");

	public ProtectionReferencesProvider(Capability<ProtectionReferences> capability, ProtectionReferences instanceSupplier) {
		super(capability, instanceSupplier);
	}

	public static ProtectionReferencesProvider createProvider(LevelChunk chunk) {
		return new ProtectionReferencesProvider(CQRCapabilities.PROTECTED_REGION_DATA, new ProtectionReferencesImplementation(chunk));
	}

	public static ProtectionReferences get(LevelChunk chunk) {
		return chunk.getCapability(CQRCapabilities.PROTECTED_REGION_DATA)
				.orElseThrow(NullPointerException::new);
	}

}
