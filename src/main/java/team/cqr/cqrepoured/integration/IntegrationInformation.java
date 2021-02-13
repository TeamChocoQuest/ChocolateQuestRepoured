package team.cqr.cqrepoured.integration;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import team.cqr.cqrepoured.integration.ancientwarfare.AW2Integration;

public class IntegrationInformation {

	public static boolean isAW2StructureAlreadyThere(int chunkX, int chunkZ, World world) {
		if(Loader.isModLoaded("ancientwarfare")) {
			return AW2Integration.isAW2StructureInChunk(chunkX, chunkZ, world);
		}
		
		return false;
	}

}
