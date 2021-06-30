package team.cqr.cqrepoured.integration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import team.cqr.cqrepoured.integration.ancientwarfare.AW2Integration;

public class IntegrationInformation {

	public static boolean isAW2StructureAlreadyThere(World world, BlockPos pos) {
		if (Loader.isModLoaded("ancientwarfare")) {
			return AW2Integration.isAW2StructureInChunk(world, pos);
		}

		return false;
	}

}
