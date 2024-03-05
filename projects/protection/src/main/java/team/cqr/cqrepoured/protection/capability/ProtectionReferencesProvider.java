package team.cqr.cqrepoured.protection.capability;

import net.minecraft.nbt.LongArrayTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.capability.SerializableCapabilityProvider;

public class ProtectionReferencesProvider extends SerializableCapabilityProvider<ProtectionReferences, LongArrayTag> {

	public static final Capability<ProtectionReferences> PROTECTION_REFERENCES = CapabilityManager.get(new CapabilityToken<>() {});
	public static final ResourceLocation LOCATION = new ResourceLocation(CQRepoured.MODID, "protection_references");

	public ProtectionReferencesProvider(LevelChunk chunk) {
		super(PROTECTION_REFERENCES, new ProtectionReferences(chunk));
	}

	public static ProtectionReferences get(LevelChunk chunk) {
		return chunk.getCapability(PROTECTION_REFERENCES)
				.orElseThrow(NullPointerException::new);
	}

}
