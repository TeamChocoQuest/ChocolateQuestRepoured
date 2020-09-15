package com.teamcqr.chocolatequestrepoured.structuregen;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class TestThread extends Thread {

	//@Meldexun: Please stop checking in changes that contain reflection without the obfuscated name included!!
	public static final ReflectionField<Boolean> FINDING_SPAWN_POINT = new ReflectionField<>(World.class, "field_72987_B", "findingSpawnPoint");
	private static final Map<Integer, TestThread> INSTANCES = new ConcurrentHashMap<>();
	private final WorldServer world;
	private final Queue<ChunkPos> chunkList = new ConcurrentLinkedQueue<>();

	private TestThread(WorldServer world) {
		this.world = world;
	}

	public static void add(WorldServer world, int chunkX, int chunkZ) {
		TestThread test = INSTANCES.computeIfAbsent(world.provider.getDimension(), key -> new TestThread(world));
		test.chunkList.add(new ChunkPos(chunkX, chunkZ));
		if (!test.isAlive()) {
			test.start();
		}
	}

	@Override
	public void run() {
		while (Boolean.TRUE.equals(FINDING_SPAWN_POINT.get(this.world))) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}

		ChunkPos chunkPos;
		while ((chunkPos = this.chunkList.poll()) != null) {
			long worldSeed = this.world.getSeed();
			Random fmlRandom = new Random(worldSeed);
			long xSeed = fmlRandom.nextLong() >> 2 + 1L;
			long zSeed = fmlRandom.nextLong() >> 2 + 1L;
			long chunkSeed = (xSeed * chunkPos.x + zSeed * chunkPos.z) ^ worldSeed;
			fmlRandom.setSeed(chunkSeed);
			CQRMain.DUNGEON_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, this.world, this.world.getChunkProvider().chunkGenerator, this.world.getChunkProvider());
			CQRMain.WALL_GENERATOR.generate(fmlRandom, chunkPos.x, chunkPos.z, this.world, this.world.getChunkProvider().chunkGenerator, this.world.getChunkProvider());
		}

		INSTANCES.remove(this.world.provider.getDimension());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldLoadEvent(WorldEvent.Unload event) {
		while (INSTANCES.containsKey(event.getWorld().provider.getDimension())) {
			// wait
		}
	}

}
