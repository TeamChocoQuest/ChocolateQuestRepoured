package team.cqr.cqrepoured.gentest.preparable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.IGeneratable;

public interface IPreparable {

	IGeneratable prepare(World world, DungeonPlacement placement);

}
