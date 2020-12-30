package team.cqr.cqrepoured.structuregen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.PropertyFileHelper;

/**
 * Copyright (c) 29.04.2019<br>
 * Developed by DerToaster98<br>
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonRegistry {

	private static final DungeonRegistry INSTANCE = new DungeonRegistry();

	private final List<DungeonBase> dungeons = new ArrayList<>();

	// TODO: Initialize this on world load
	/*
	 * First key: Dimension Second key: Dungeon name Third value: where this dungeon did spawn
	 */

	public static DungeonRegistry getInstance() {
		return DungeonRegistry.INSTANCE;
	}

	public List<DungeonBase> getDungeons() {
		return this.dungeons;
	}

	public DungeonBase getDungeon(String name) {
		for (DungeonBase dungeon : this.dungeons) {
			if (dungeon.getDungeonName().equals(name)) {
				return dungeon;
			}
		}
		return null;
	}

	public CQRWeightedRandom<DungeonBase> getDungeonsForPos(World world, BlockPos pos) {
		CQRWeightedRandom<DungeonBase> dungeonsForChunk = new CQRWeightedRandom<>();
		boolean isChunkBehindWall = CQRConfig.wall.enabled && (pos.getZ() >> 4) < -CQRConfig.wall.distance;

		for (DungeonBase dungeon : this.dungeons) {
			if (dungeon.canSpawnAtPos(world, pos, isChunkBehindWall)) {
				dungeonsForChunk.add(dungeon, dungeon.getWeight());
			}
		}

		return dungeonsForChunk;
	}

	public List<DungeonBase> getLocationSpecificDungeonsForChunk(World world, int chunkX, int chunkZ) {
		List<DungeonBase> dungeonsForChunk = new ArrayList<>();

		for (DungeonBase dungeon : this.dungeons) {
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
			DungeonBase dungeon = this.createDungeonFromFile(file);

			if (dungeon != null) {
				this.dungeons.add(dungeon);

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
	}

	private DungeonBase createDungeonFromFile(File file) {
		Properties prop = PropertyFileHelper.readPropFile(file);
		if (prop == null) {
			return null;
		}

		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		String generatorType = prop.getProperty("generator", "");
		EDungeonGenerator dungeonGenerator = EDungeonGenerator.getDungeonGenerator(generatorType);

		return dungeonGenerator != null ? dungeonGenerator.createDungeon(name, prop) : null;
	}

}
