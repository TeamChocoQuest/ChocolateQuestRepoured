package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerationManager;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractDungeonGenerator<T extends DungeonBase> {

	protected final World world;
	protected final BlockPos pos;
	protected final T dungeon;
	protected final DungeonGenerator dungeonGenerator;

	public AbstractDungeonGenerator(World world, BlockPos pos, T dungeon) {
		this.world = world;
		this.pos = pos;
		this.dungeon = dungeon;
		this.dungeonGenerator = new DungeonGenerator(world, pos, dungeon.getDungeonName());
	}

	public void generate() {
		if (world.isRemote) {
			return;
		}

		CQRMain.logger.info("Start generating dungeon {} at {}", this.dungeon, pos);

		this.preProcess();
		this.buildStructure();
		this.postProcess();

		DungeonGenerationManager.addStructure(this.world, this.dungeonGenerator, this.dungeon);
	}

	protected abstract void preProcess();

	protected abstract void buildStructure();

	protected abstract void postProcess();

}
