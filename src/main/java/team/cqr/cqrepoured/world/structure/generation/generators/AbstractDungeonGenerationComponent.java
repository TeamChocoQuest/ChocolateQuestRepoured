package team.cqr.cqrepoured.world.structure.generation.generators;

import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

public abstract class AbstractDungeonGenerationComponent<D extends DungeonBase, T extends LegacyDungeonGenerator<D>> {

	protected final T generator;
	protected final D dungeon;

	protected AbstractDungeonGenerationComponent(T generator) {
		this.generator = generator;
		this.dungeon = this.generator.dungeon;
	}

	public abstract void preProcess(Level world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generate(Level world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generatePost(Level world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

}
