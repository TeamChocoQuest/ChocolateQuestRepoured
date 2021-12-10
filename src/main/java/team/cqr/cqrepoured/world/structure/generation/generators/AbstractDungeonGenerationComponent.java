package team.cqr.cqrepoured.world.structure.generation.generators;

import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitant;

public abstract class AbstractDungeonGenerationComponent<T extends AbstractDungeonGenerator<?>> {

	protected final T generator;
	protected final DungeonBase dungeon;

	protected AbstractDungeonGenerationComponent(T generator) {
		this.generator = generator;
		this.dungeon = this.generator.dungeon;
	}

	public abstract void preProcess(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generate(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generatePost(World world, GeneratableDungeon.Builder dungeonBuilder, DungeonInhabitant mobType);

}
