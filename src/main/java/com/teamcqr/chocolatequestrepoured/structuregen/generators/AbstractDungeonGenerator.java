package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonDataManager;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerationManager;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractDungeonGenerator<T extends DungeonBase> {

	protected final Random random;
	protected final World world;
	protected final BlockPos pos;
	protected final T dungeon;
	protected final DungeonGenerator dungeonGenerator;
	private final DungeonDataManager.DungeonSpawnType spawnType;

	private final Map<File, CQStructure> cachedStructures = new HashMap<>();

	public AbstractDungeonGenerator(World world, BlockPos pos, T dungeon, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		this.world = world;
		this.pos = pos;
		this.dungeon = dungeon;
		this.random = rand;
		this.spawnType = spawnType;
		this.dungeonGenerator = new DungeonGenerator(world, pos, dungeon.getDungeonName());
	}

	public void generate(boolean generateImmediately) {
		if (this.world.isRemote) {
			return;
		}

		CQRMain.logger.info("Start generating dungeon {} at {}", this.dungeon, this.pos);

		try {
			this.preProcess();
			this.buildStructure();
			this.postProcess();
			DungeonGenerationManager.addStructure(this.world, this.dungeonGenerator, this.dungeon, this.spawnType, generateImmediately);
		} catch (Exception e) {
			CQRMain.logger.error(String.format("Failed to prepare dungeon %s for generation at %s", this.dungeon, this.pos), e);
		}
	}

	// Actually having 3 methods here is useless as they are just called one after another

	protected abstract void preProcess();

	protected abstract void buildStructure();

	protected abstract void postProcess();

	public CQStructure loadStructureFromFile(File file) {
		if (this.cachedStructures.containsKey(file)) {
			return this.cachedStructures.get(file);
		}
		CQStructure structure = CQStructure.createFromFile(file);
		this.cachedStructures.put(file, structure);
		return structure;
	}

	public World getWorld() {
		return this.world;
	}

}
