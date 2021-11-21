package team.cqr.cqrepoured.structuregen.generators;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;

public abstract class AbstractDungeonGenerator<T extends DungeonBase> implements Supplier<GeneratableDungeon> {

	protected final World world;
	protected final Random random;
	protected final BlockPos pos;
	protected final T dungeon;
	protected final GeneratableDungeon.Builder dungeonBuilder;

	private final Map<File, CQStructure> cachedStructures = new HashMap<>();

	protected AbstractDungeonGenerator(World world, BlockPos pos, T dungeon, Random random) {
		this.world = world;
		this.pos = pos;
		this.dungeon = dungeon;
		this.random = random;
		this.dungeonBuilder = new GeneratableDungeon.Builder(this.world, this.pos, this.dungeon);
	}

	@Override
	public GeneratableDungeon get() {
		try {
			this.preProcess();
			this.buildStructure();
			this.postProcess();
			return this.dungeonBuilder.build(this.world);
		} catch (Throwable e) {
			// TODO handle this elsewhere, DungeonPreparationHelper maybe?
			CQRMain.logger.error("Failed to prepare dungeon {} for generation at {}", this.dungeon, this.pos, e);
			throw new RuntimeException(e);
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

	public T getDungeon() {
		return this.dungeon;
	}

}
