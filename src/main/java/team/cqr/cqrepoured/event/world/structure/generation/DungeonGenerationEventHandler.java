package team.cqr.cqrepoured.event.world.structure.generation;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonGenerationManager;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonGenerationEventHandler {

	@SubscribeEvent
	public static void onWorldCreatedEvent(WorldEvent.CreateSpawnPosition event) {
		DungeonGenerationManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		DungeonGenerationManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		DungeonGenerationHelper.onWorldUnloadEvent(event.getWorld());
		DungeonPreparationExecutor.onWorldUnloadEvent(event.getWorld());
		DungeonGenerationManager.handleWorldUnload(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == Phase.START) {
			DungeonGenerationManager.generateScheduledDungeons(event.world);
		}
	}

}
