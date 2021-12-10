package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public interface IGeneratable {

	void generate(World world, GeneratableDungeon dungeon);

}
