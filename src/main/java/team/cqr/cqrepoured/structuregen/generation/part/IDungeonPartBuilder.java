package team.cqr.cqrepoured.structuregen.generation.part;

import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;

public interface IDungeonPartBuilder {

	DungeonPart build(World world, DungeonPlacement placement);

}
