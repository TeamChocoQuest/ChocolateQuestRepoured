package team.cqr.cqrepoured.gentest.generatable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;

public interface IGeneratable {

	void generate(World world, GeneratableDungeon dungeon);

}
