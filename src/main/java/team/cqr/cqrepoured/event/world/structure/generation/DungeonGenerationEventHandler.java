package team.cqr.cqrepoured.event.world.structure.generation;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonGenerationManager;

@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonGenerationEventHandler {

	@SubscribeEvent(receiveCanceled = true)
	public static void onWorldCreatedEvent(WorldEvent.CreateSpawnPosition event) {
		DungeonDataManager.onWorldLoad(event.getWorld());
		DungeonGenerationManager.onWorldLoad(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		if (event.getWorld().isRemote) {
			return;
		}
		DungeonDataManager.onWorldLoad(event.getWorld());
		DungeonGenerationManager.onWorldLoad(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldSaveEvent(WorldEvent.Save event) {
		DungeonDataManager.onWorldSave(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) {
			return;
		}
		DungeonGenerationManager.onWorldUnload(event.getWorld());
		DungeonDataManager.onWorldUnload(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == Phase.START) {
			DungeonGenerationManager.onWorldTick(event.world);
		}
	}

}
