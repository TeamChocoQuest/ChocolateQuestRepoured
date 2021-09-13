package team.cqr.cqrepoured.gentest.part;

import net.minecraft.world.World;
import team.cqr.cqrepoured.gentest.DungeonPlacement;

public interface IDungeonPartBuilder {

	DungeonPart build(World world, DungeonPlacement placement);

}
