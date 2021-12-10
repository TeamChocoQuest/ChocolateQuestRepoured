package team.cqr.cqrepoured.world.structure.generation.generation.part;

import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;

public interface IDungeonPartBuilder {

	IDungeonPart build(World world, DungeonPlacement placement);

}
