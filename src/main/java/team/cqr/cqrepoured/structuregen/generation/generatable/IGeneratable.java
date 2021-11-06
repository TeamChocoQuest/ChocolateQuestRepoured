package team.cqr.cqrepoured.structuregen.generation.generatable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;

public interface IGeneratable {

	void generate(World world, GeneratableDungeon dungeon);

}
