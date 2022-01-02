package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public final class DungeonGenerationManager {

	private static final Map<World, DungeonGenerationManager> INSTANCES = new ConcurrentHashMap<>();

	private final Queue<GeneratableDungeon> dungeonGeneratorList = new ConcurrentLinkedQueue<>();
	private final World world;

	private DungeonGenerationManager(World world) {
		this.world = world;
	}

	public static void handleWorldLoad(World world) {
		if (world.isClientSide) {
			return;
		}
		INSTANCES.computeIfAbsent(world, DungeonGenerationManager::new);
	}

	public static void handleWorldUnload(World world) {
		generateScheduledDungeons(world);
		INSTANCES.remove(world);
	}

	public static void generateScheduledDungeons(World world) {
		INSTANCES.computeIfPresent(world, (k, v) -> {
			v.generateScheduledDungeons();
			return v;
		});
	}

	public static void generate(World world, GeneratableDungeon generatableDungeon, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (dungeon != null) {
			DungeonDataManager.addDungeonEntry(world, dungeon, generatableDungeon.getPos(), spawnType);
		}

		INSTANCES.get(world).dungeonGeneratorList.add(generatableDungeon);
	}

	public static void generateNow(World world, GeneratableDungeon generatableDungeon, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (dungeon != null) {
			DungeonDataManager.addDungeonEntry(world, dungeon, generatableDungeon.getPos(), spawnType);
		}

		boolean logCascadingWorldGeneration = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		generatableDungeon.generate(world);
		ForgeModContainer.logCascadingWorldGeneration = logCascadingWorldGeneration;
	}

	private void generateScheduledDungeons() {
		GeneratableDungeon generatbleDungeon;
		while ((generatbleDungeon = this.dungeonGeneratorList.poll()) != null) {
			generatbleDungeon.generate(this.world);
		}
	}

}
