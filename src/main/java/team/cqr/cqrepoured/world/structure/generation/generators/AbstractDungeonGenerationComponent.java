package team.cqr.cqrepoured.world.structure.generation.generators;

import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonInhabitant;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public abstract class AbstractDungeonGenerationComponent<D extends DungeonBase, T extends LegacyDungeonGenerator<D>> {

	protected final T generator;
	protected final D dungeon;

	protected AbstractDungeonGenerationComponent(T generator) {
		this.generator = generator;
		this.dungeon = this.generator.dungeon;
	}

	public abstract void preProcess(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generate(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType);

	public abstract void generatePost(Level world, CQRStructurePiece.Builder dungeonBuilder, DungeonInhabitant mobType);

}
