package team.cqr.cqrepoured.world.structure.generation;

import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonRegistry {

	private static final DungeonRegistry INSTANCE = new DungeonRegistry();

	private final Map<String, DungeonBase> dungeons = new HashMap<>();

	public static DungeonRegistry getInstance() {
		return DungeonRegistry.INSTANCE;
	}

	public Collection<DungeonBase> getDungeons() {
		return this.dungeons.values();
	}

	@Nullable
	public DungeonBase getDungeon(String name) {
		return this.dungeons.get(name);
	}

	public List<DungeonBase> getLocationSpecificDungeonsForChunk(World world, int chunkX, int chunkZ) {
		List<DungeonBase> dungeonsForChunk = new ArrayList<>();

		for (DungeonBase dungeon : this.dungeons.values()) {
			if (dungeon.canSpawnInChunkWithLockedPosition(world, chunkX, chunkZ)) {
				dungeonsForChunk.add(dungeon);
			}
		}

		return dungeonsForChunk;
	}

	public void loadDungeonFiles() {
		this.dungeons.clear();

		Collection<File> files = FileUtils.listFiles(CQRMain.CQ_DUNGEON_FOLDER, new String[] { "properties", "prop", "cfg" }, true);
		CQRMain.logger.info("Loading {} dungeon configuration files...", files.size());

		for (File file : files) {
			DungeonBase dungeon = DungeonRegistry.createDungeonFromFile(file);

			if (dungeon != null && !this.dungeons.containsKey(dungeon.getDungeonName())) {
				this.dungeons.put(dungeon.getDungeonName(), dungeon);

				if (dungeon.isModDependencyMissing()) {
					CQRMain.logger.warn("{}: Dungeon is missing one or more mod dependencies!", file.getName());
				}

				if (dungeon.isEnabled()) {
					if (dungeon.getWeight() <= 0) {
						CQRMain.logger.warn("{}: Dungeon is enabled and weight is set to or below 0!", file.getName());
					}

					if (dungeon.getChance() <= 0) {
						CQRMain.logger.warn("{}: Dungeon is enabled and chance is set to or below 0!", file.getName());
					}
				}
			}
		}

		GridRegistry.getInstance().loadGridFiles();
	}

	public static DungeonBase createDungeonFromFile(File file) {
		Properties prop = PropertyFileHelper.readPropFile(file);
		if (prop == null) {
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		return createDungeonFromFile(prop, name);
	}
	
	public static DungeonBase createDungeonFromFile(Properties prop, String name) {
		if (prop == null) {
			return null;
		}

		String generatorType = prop.getProperty("generator", "");
		EDungeonGenerator dungeonGenerator = EDungeonGenerator.getDungeonGenerator(generatorType);

		return dungeonGenerator != null ? dungeonGenerator.createDungeon(name, prop) : null;
	}

}
