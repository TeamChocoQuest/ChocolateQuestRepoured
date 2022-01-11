package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Nullable;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public final class DungeonGenerationManager {

	private static final Map<IWorld, DungeonGenerationManager> INSTANCES = new ConcurrentHashMap<>();

	private final Queue<GeneratableDungeon> dungeonGeneratorList = new ConcurrentLinkedQueue<>();
	private final IWorld world;

	private DungeonGenerationManager(IWorld world) {
		this.world = world;
	}

	public static void handleWorldLoad(IWorld world) {
		if (world.isClientSide()) {
			return;
		}
		INSTANCES.computeIfAbsent(world, DungeonGenerationManager::new);
	}

	public static void handleWorldUnload(IWorld world) {
		generateScheduledDungeons(world);
		INSTANCES.remove(world);
	}

	public static void generateScheduledDungeons(IWorld world) {
		INSTANCES.computeIfPresent(world, (k, v) -> {
			v.generateScheduledDungeons();
			return v;
		});
	}

	public static void generate(IWorld world, GeneratableDungeon generatableDungeon, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (dungeon != null) {
			DungeonDataManager.addDungeonEntry(world, dungeon, generatableDungeon.getPos(), spawnType);
		}

		INSTANCES.get(world).dungeonGeneratorList.add(generatableDungeon);
	}

	public static void generateNow(IWorld world, GeneratableDungeon generatableDungeon, @Nullable DungeonBase dungeon, DungeonSpawnType spawnType) {
		if (dungeon != null) {
			DungeonDataManager.addDungeonEntry(world, dungeon, generatableDungeon.getPos(), spawnType);
		}

		boolean logCascadingWorldGeneration = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		if(world instanceof World) {
			generatableDungeon.generate((World)world);
		} else {
			CQRMain.logger.error("IWorld instanceof somehow is not an instance of World!");
		}
		ForgeModContainer.logCascadingWorldGeneration = logCascadingWorldGeneration;
	}

	private void generateScheduledDungeons() {
		GeneratableDungeon generatbleDungeon;
		while ((generatbleDungeon = this.dungeonGeneratorList.poll()) != null) {
			if(this.world instanceof World) {
				generatbleDungeon.generate((World)this.world);
			} else {
				CQRMain.logger.error("IWorld instanceof somehow is not an instance of World!");
			}
		}
	}

}
