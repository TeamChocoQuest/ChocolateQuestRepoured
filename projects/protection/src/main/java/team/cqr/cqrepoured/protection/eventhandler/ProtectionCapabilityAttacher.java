package team.cqr.cqrepoured.protection.eventhandler;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.protection.capability.ProtectionReferencesProvider;

@EventBusSubscriber(modid = CQRepoured.MODID)
public class ProtectionCapabilityAttacher {

	@SubscribeEvent
	public static void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(ProtectionReferencesProvider.LOCATION, new ProtectionReferencesProvider(event.getObject()));
	}

}
