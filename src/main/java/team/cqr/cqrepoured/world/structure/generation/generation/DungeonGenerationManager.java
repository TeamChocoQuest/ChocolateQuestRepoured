package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public final class DungeonGenerationManager {

	private static final Map<World, DungeonGenerationManager> INSTANCES = new HashMap<>();

	private final ExecutorService executor = Executors.newSingleThreadExecutor(task -> new Thread(task, "CQR Dungeon Preparation Thread"));
	private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
	private boolean stopped;

	public static void onWorldLoad(World world) {
		INSTANCES.computeIfAbsent(world, k -> new DungeonGenerationManager());
	}

	public static void onWorldUnload(World world) {
		INSTANCES.get(world).stop();
		INSTANCES.remove(world);
	}

	public static void onWorldTick(World world) {
		INSTANCES.get(world).runScheduledTasks();
	}

	private void stop() {
		this.stopped = true;

		this.executor.shutdown();
		try {
			if (!this.executor.awaitTermination(10, TimeUnit.SECONDS)) {
				this.executor.shutdownNow();
				if (!this.executor.awaitTermination(10, TimeUnit.SECONDS)) {
					CQRMain.logger.error("Couldn't shutdown dungeon preparation executor!");
				}
			}
		} catch (InterruptedException e) {
			this.executor.shutdownNow();
			Thread.currentThread().interrupt();
		}

		this.runScheduledTasks();
	}

	private void runScheduledTasks() {
		Runnable task;
		while ((task = this.tasks.poll()) != null) {
			task.run();
		}
	}

	public static void generate(World world, GeneratableDungeon.Builder builder, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		generate(world, () -> builder.build(world), dungeon, spawnType);
	}

	public static void generate(World world, Supplier<GeneratableDungeon> builder, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		INSTANCES.get(world)._generate(world, builder, dungeon, spawnType);
	}

	private void _generate(World world, Supplier<GeneratableDungeon> builder, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (shouldGenerateDungeonImmediately(world)) {
			generate(world, builder.get(), dungeon, spawnType);
		} else {
			this.executor.execute(() -> {
				try {
					GeneratableDungeon generatable = builder.get();
					this.tasks.add(() -> generate(world, generatable, dungeon, spawnType));
				} catch (Throwable e) {
					this.tasks.add(() -> {
						throw new CompletionException(e);
					});
				}
			});
		}
	}

	private static void generate(World world, GeneratableDungeon generatableDungeon, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (dungeon != null) {
			DungeonDataManager.addDungeonEntry(world, dungeon, generatableDungeon.getPos(), spawnType);
		}

		boolean logCascadingWorldGeneration = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		try {
			generatableDungeon.generate(world);
		} finally {
			ForgeModContainer.logCascadingWorldGeneration = logCascadingWorldGeneration;
		}
	}

	private boolean shouldGenerateDungeonImmediately(World world) {
		if (this.stopped) {
			return true;
		}
		if (((SpawnpointGenerationHandler) world).isGeneratingDelayedChunks()) {
			return true;
		}
		if (((PortalGenerationHandler) world).isGeneratingDestinationChunks()) {
			return true;
		}
		if (world.playerEntities.isEmpty()) {
			return true;
		}
		return !CQRConfig.advanced.multithreadedDungeonPreparation;
	}

}
