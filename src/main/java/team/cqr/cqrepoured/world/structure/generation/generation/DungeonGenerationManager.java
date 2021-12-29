package team.cqr.cqrepoured.world.structure.generation.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class DungeonGenerationManager {

	private static final Map<World, DungeonGenerationManager> INSTANCES = Collections.synchronizedMap(new HashMap<>());

	private final List<GeneratableDungeon> dungeonGeneratorList = Collections.synchronizedList(new ArrayList<>());
	private final World world;

	public DungeonGenerationManager(World world) {
		this.world = world;
	}

	@Nullable
	public static DungeonGenerationManager getInstance(World world) {
		if (!world.isRemote) {
			return INSTANCES.get(world);
		}
		return null;
	}

	public static void handleWorldLoad(World world) {
		if (!world.isRemote && !INSTANCES.containsKey(world)) {
			INSTANCES.put(world, new DungeonGenerationManager(world));
		}
	}

	public static void handleWorldSave(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
		}
	}

	public static void handleWorldUnload(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			CQRMain.logger.info("Saved {} parts to generate", INSTANCES.get(world).dungeonGeneratorList.size());
			INSTANCES.remove(world);
		}
	}

	public static void handleWorldTick(World world) {
		if (!world.isRemote && INSTANCES.containsKey(world)) {
			INSTANCES.get(world).tick();
		}
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
		while (!generatableDungeon.isGenerated()) {
			generatableDungeon.tick(world);
		}
		ForgeModContainer.logCascadingWorldGeneration = logCascadingWorldGeneration;
	}

	private void tick() {
		for (int i = 0; i < this.dungeonGeneratorList.size(); i++) {
			GeneratableDungeon generatableDungeon = this.dungeonGeneratorList.get(i);

			generatableDungeon.tick(this.world);

			if (generatableDungeon.isGenerated()) {
				this.dungeonGeneratorList.remove(i--);
			}
		}
	}

}
