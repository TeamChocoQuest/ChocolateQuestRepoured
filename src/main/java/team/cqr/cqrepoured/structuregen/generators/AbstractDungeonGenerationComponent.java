package team.cqr.cqrepoured.structuregen.generators;

import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonBase;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;

public abstract class AbstractDungeonGenerationComponent<T extends AbstractDungeonGenerator<?>> {
	
	protected final T generator;
	protected final DungeonBase dungeon;
	
	public AbstractDungeonGenerationComponent(T generator) {
		this.generator = generator;
		this.dungeon = this.generator.dungeon;
	}

	public abstract void preProcess(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType);
	public abstract void generate(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType);
	public abstract void generatePost(World world, DungeonGenerator dungeonGenerator, DungeonInhabitant mobType);
	
}
