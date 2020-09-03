package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@EventBusSubscriber(modid = Reference.MODID)
public class DungeonGenerationEventHandler {

	@SubscribeEvent
	public static void onWorldCreatedEvent(WorldEvent.CreateSpawnPosition event) {
		DungeonGenerationManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		DungeonGenerationManager.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldSaveEvent(WorldEvent.Save event) {
		DungeonGenerationManager.handleWorldSave(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		DungeonGenerationManager.handleWorldUnload(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == Phase.START) {
			DungeonGenerationManager.handleWorldTick(event.world);
		}
	}

}
