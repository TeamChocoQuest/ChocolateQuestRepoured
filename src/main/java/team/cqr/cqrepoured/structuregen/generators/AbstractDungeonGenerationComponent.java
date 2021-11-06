package team.cqr.cqrepoured.structuregen.generators;

import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;

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
