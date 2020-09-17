package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;

import net.minecraft.world.World;

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
		if (DUNGEON_GENERATOR_THREADS.containsKey(this.dim)) {
			DUNGEON_GENERATOR_THREADS.get(this.dim).remove(this);
		}
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

}
