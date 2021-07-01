package team.cqr.cqrepoured.integration.ancientwarfare;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.shadowmage.ancientwarfare.core.gamedata.AWGameData;
import net.shadowmage.ancientwarfare.structure.gamedata.StructureEntry;
import net.shadowmage.ancientwarfare.structure.gamedata.StructureMap;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;

public class AW2Integration {

	public static boolean isAW2StructureInChunk(World world, BlockPos pos) {
		try {
			StructureMap data = AWGameData.INSTANCE.getPerWorldData(world, StructureMap.class);
			Collection<StructureEntry> iterator = data.getEntriesNear(world, pos.getX(), pos.getZ(), MathHelper.ceil(CQRConfig.advanced.generationMinDistanceToOtherStructure / 16), true, new ArrayList<StructureEntry>());
			return !iterator.isEmpty();
		} catch (Exception ex) {
			CQRMain.logger.warn("Unable to process AW2-Dependency! Error: ", ex);
		}
		return false;
	}
}
