package com.teamcqr.chocolatequestrepoured.structuregen;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class DungeonGenerationHandlerEvent {

	@SubscribeEvent
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		DungeonGenerationHandler.handleWorldLoad(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldSaveEvent(WorldEvent.Save event) {
		DungeonGenerationHandler.handleWorldSave(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		DungeonGenerationHandler.handleWorldUnload(event.getWorld());
	}

	@SubscribeEvent
	public static void onWorldTickEvent(WorldTickEvent event) {
		if (event.phase == Phase.START) {
			DungeonGenerationHandler.handleWorldTick(event.world);
		}
	}

}
