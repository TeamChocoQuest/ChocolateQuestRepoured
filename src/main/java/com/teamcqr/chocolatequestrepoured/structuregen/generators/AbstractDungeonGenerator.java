package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.AbstractDungeonPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerationManager;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractDungeonGenerator<T extends DungeonBase> {

	protected final Random random = new Random();
	protected final World world;
	protected final BlockPos pos;
	protected final T dungeon;
	protected final DungeonGenerator dungeonGenerator;

	// Why remove all parameters from the functions?!?!? Those were supposed to be all the same! It is even needed like that for some generators

	public AbstractDungeonGenerator(World world, BlockPos pos, T dungeon) {
		this.world = world;
		this.pos = pos;
		this.dungeon = dungeon;
		this.dungeonGenerator = new DungeonGenerator(world, pos, dungeon.getDungeonName());
	}

	public void generate() {
		if (this.world.isRemote) {
			return;
		}

		CQRMain.logger.info("Start generating dungeon {} at {}", this.dungeon, this.pos);

		try {
			this.preProcess();
			this.buildStructure();
			this.postProcess();
			DungeonGenerationManager.addStructure(this.world, this.dungeonGenerator, this.dungeon);
		} catch (Exception e) {
			CQRMain.logger.error("Failed to prepare dungeon " + this.dungeon + " for generation at " + this.pos, e);
		}
	}

	/**
	 * Now all pre-processing should be done (for example loading a file).
	 */
	protected abstract void preProcess();

	/**
	 * Now all {@link AbstractDungeonPart} objects should be added to the {@link AbstractDungeonGenerator#dungeonGenerator}.
	 */
	protected abstract void buildStructure();

	/**
	 * Now all post-processing should be done (Note that the dungeon hasn't been generated yet).
	 */
	protected abstract void postProcess();

}
