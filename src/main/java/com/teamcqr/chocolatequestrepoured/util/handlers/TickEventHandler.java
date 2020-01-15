package com.teamcqr.chocolatequestrepoured.util.handlers;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class TickEventHandler {
	
	static int cooldown = 0;
	
	@SubscribeEvent
	public static void onTick(TickEvent event) {
		if(event.side == Side.SERVER && event.phase == TickEvent.Phase.START /*&& Thread.currentThread().getName().equalsIgnoreCase("Server hread")*/) {
			cooldown++;
			if(cooldown > CQRConfig.advanced.tickRateForTasks) {
				cooldown = 0;
				//System.out.println("Executing runnable...");
				Runnable task = Reference.BLOCK_PLACING_THREADS_INSTANCE.getNextTask();
				if(task != null) {
					task.run();
					//System.out.println("Waiting tasks: " + Reference.BLOCK_PLACING_THREADS_INSTANCE.getRemainingTasks());
				}
			}
		}
	}

}
