package team.cqr.cqrepoured.integration.ancientwarfare;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowmage.ancientwarfare.core.gamedata.AWGameData;
import net.shadowmage.ancientwarfare.structure.gamedata.StructureEntry;
import net.shadowmage.ancientwarfare.structure.gamedata.StructureMap;

public class AW2Integration {

	public static boolean isAW2StructureInRange(World world, BlockPos pos, int radius) {
		StructureMap structureMap = AWGameData.INSTANCE.getPerWorldData(world, StructureMap.class);
		Collection<StructureEntry> strutures = structureMap.getEntriesNear(world, pos.getX(), pos.getZ(), radius >> 4, true, new ArrayList<StructureEntry>());
		return !strutures.isEmpty();
	}

}
