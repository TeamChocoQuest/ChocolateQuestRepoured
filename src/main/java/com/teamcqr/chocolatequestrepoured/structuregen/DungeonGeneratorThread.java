package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class DungeonGeneratorThread extends Thread {

	private static final Int2ObjectMap<DungeonGeneratorThread> DUNGEON_GENERATOR_THREADS = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());
	private final BlockingQueue<GeneratorInfo> queue = new LinkedBlockingQueue<>();
	private static final GeneratorInfo STOP = new GeneratorInfo(null);

	private static class GeneratorInfo {
		private final AbstractDungeonGenerator<?> dungeonGenerator;

		public GeneratorInfo(AbstractDungeonGenerator<?> dungeonGenerator) {
			this.dungeonGenerator = dungeonGenerator;
		}
	}

	private DungeonGeneratorThread() {
		this.setName("CQR Dungeon Generator Thread");
	}

	@Override
	public void run() {
		while (true) {
			try {
				GeneratorInfo generatorInfo = this.queue.take();

				if (generatorInfo == STOP) {
					break;
				}

				generatorInfo.dungeonGenerator.generate(false);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void stopAndWait() {
		try {
			this.queue.put(STOP);
			this.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static boolean add(AbstractDungeonGenerator<?> dungeonGenerator) {
		if (dungeonGenerator == null) {
			return false;
		}

		DungeonGeneratorThread dungeonGeneratorThread = DUNGEON_GENERATOR_THREADS.get(dungeonGenerator.getWorld().provider.getDimension());

		if (dungeonGeneratorThread == null) {
			return false;
		}

		return dungeonGeneratorThread.queue.offer(new GeneratorInfo(dungeonGenerator));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		int dim = event.getWorld().provider.getDimension();

		if (DUNGEON_GENERATOR_THREADS.containsKey(dim)) {
			return;
		}

		DungeonGeneratorThread dungeonGeneratorThread = new DungeonGeneratorThread();
		DUNGEON_GENERATOR_THREADS.put(dim, dungeonGeneratorThread);
		dungeonGeneratorThread.start();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldUnloadEvent(WorldEvent.Unload event) {
		int dim = event.getWorld().provider.getDimension();
		DungeonGeneratorThread dungeonGeneratorThread = DUNGEON_GENERATOR_THREADS.get(dim);

		if (dungeonGeneratorThread == null) {
			return;
		}

		dungeonGeneratorThread.stopAndWait();
		DUNGEON_GENERATOR_THREADS.remove(dim);
	}

}
