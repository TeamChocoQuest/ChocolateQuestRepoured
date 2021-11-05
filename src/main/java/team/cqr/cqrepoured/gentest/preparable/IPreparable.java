package team.cqr.cqrepoured.gentest.preparable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.IGeneratable;

public interface IPreparable<T extends IGeneratable> {

	default T prepare(World world, DungeonPlacement placement) {
		return CQRConfig.advanced.debugMode ? this.prepareDebug(world, placement) : this.prepareNormal(world, placement);
	}

	T prepareNormal(World world, DungeonPlacement placement);

	T prepareDebug(World world, DungeonPlacement placement);

}
