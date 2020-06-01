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

	protected final World world;
	protected final T dungeon;
	protected final Random random = new Random();
	protected BlockPos pos;
	protected DungeonGenerator dungeonGenerator;

	// Why remove all parameters from the functions?!?!? Those were supposed to be all the same! It is even needed like that for some generators

	public AbstractDungeonGenerator(World world, T dungeon, BlockPos pos) {
		this.world = world;
		this.dungeon = dungeon;
		this.pos = pos;
	}

	public void generate() {
		if (this.world.isRemote) {
			return;
		}

		CQRMain.logger.info("Start generating dungeon {} at {}", this.dungeon, this.pos);

		try {
			this.preProcess();
		} catch (Exception e) {
			CQRMain.logger.error("Failed to pre-process dungeon " + this.dungeon + " at " + this.pos, e);
			return;
		}

		this.dungeonGenerator = new DungeonGenerator(this.world, this.pos, this.dungeon.getDungeonName());

		try {
			this.buildStructure();
		} catch (Exception e) {
			CQRMain.logger.error("Failed to process dungeon " + this.dungeon + " at " + this.pos, e);
			return;
		}

		DungeonGenerationManager.addStructure(this.world, this.dungeonGenerator, this.dungeon);

		try {
			this.postProcess();
		} catch (Exception e) {
			CQRMain.logger.error("Failed to post-process dungeon " + this.dungeon + " at " + this.pos, e);
		}
	}

	/**
	 * The field {@link AbstractDungeonGenerator#dungeonGenerator} hasn't been initialized and thus the dungeon position can be changed.
	 * Now all pre-processing should be done (for example loading a file).
	 */
	protected abstract void preProcess();

	/**
	 * The field {@link AbstractDungeonGenerator#dungeonGenerator} has been initialized.
	 * Now all {@link AbstractDungeonPart} objects should be added to the {@link AbstractDungeonGenerator#dungeonGenerator}.
	 */
	protected abstract void buildStructure();

	/**
	 * The {@link AbstractDungeonGenerator#dungeonGenerator} has been committed to the {@link DungeonGenerationManager}.
	 * Now all post-processing should be done (Note that the dungeon hasn't been generated yet).
	 */
	protected abstract void postProcess();

}
