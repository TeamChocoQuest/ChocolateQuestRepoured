package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class DungeonGeneratorThread extends Thread {

	private static final Map<Integer, List<DungeonGeneratorThread>> DUNGEON_GENERATOR_THREADS = Collections.synchronizedMap(new HashMap<>());
	private final int dim;
	private final AbstractDungeonGenerator<?> dungeonGenerator;

	public DungeonGeneratorThread(AbstractDungeonGenerator<?> dungeonGenerator) {
		this.setName("CQR Dungeon Generator Thread");
		this.dim = dungeonGenerator.getWorld().provider.getDimension();
		this.dungeonGenerator = dungeonGenerator;
	}

	@Override
	public synchronized void start() {
		DUNGEON_GENERATOR_THREADS.computeIfAbsent(this.dim, key -> Collections.synchronizedList(new ArrayList<>())).add(this);
		super.start();
	}

	@Override
	public void run() {
		this.dungeonGenerator.generate(DungeonDataManager.DungeonSpawnType.DUNGEON_GENERATION, false);
		DUNGEON_GENERATOR_THREADS.get(this.dim).remove(this);
	}

	public static boolean isDungeonGeneratorThreadRunning(World world) {
		return isDungeonGeneratorThreadRunning(world.provider.getDimension());
	}

	public static boolean isDungeonGeneratorThreadRunning(int dim) {
		if (!DUNGEON_GENERATOR_THREADS.containsKey(dim)) {
			return false;
		}
		return !DUNGEON_GENERATOR_THREADS.get(dim).isEmpty();
	}

	public static void waitForRunningDungeonGeneratorThreads(World world) {
		waitForRunningDungeonGeneratorThreads(world.provider.getDimension());
	}

	public static void waitForRunningDungeonGeneratorThreads(int dim) {
		if (!DUNGEON_GENERATOR_THREADS.containsKey(dim)) {
			return;
		}
		for (DungeonGeneratorThread dungeonGeneratorThread : DUNGEON_GENERATOR_THREADS.get(dim)) {
			try {
				dungeonGeneratorThread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		waitForRunningDungeonGeneratorThreads(event.getWorld());
	}

}
