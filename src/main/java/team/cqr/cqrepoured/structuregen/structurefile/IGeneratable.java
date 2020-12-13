package team.cqr.cqrepoured.structuregen.structurefile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structureprot.ProtectedRegion;

public interface IGeneratable {

	void generate(World world, BlockPos dungeonPos, BlockPos dungeonPartPos, PlacementSettings settings, DungeonInhabitant dungeonMob, ProtectedRegion protectedRegion);

}
