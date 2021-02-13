package team.cqr.cqrepoured.integration.ancientwarfare;

import net.minecraft.world.World;
import net.shadowmage.ancientwarfare.core.gamedata.AWGameData;
import net.shadowmage.ancientwarfare.structure.gamedata.StructureMap;
import team.cqr.cqrepoured.CQRMain;

public class AW2Integration {

	public static boolean isAW2StructureInChunk(int chunkX, int chunkZ, World world) {
		try {
			StructureMap data = AWGameData.INSTANCE.getPerWorldData(world, StructureMap.class);
			return data.getStructureAt(world, chunkX, chunkZ).isPresent();
		} catch(Exception ex) {
			CQRMain.logger.warn("Unable to process AW2-Dependency! Error: ", ex);
		}
		return false;
	}
}
