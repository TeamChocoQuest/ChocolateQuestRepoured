package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.IGeneratable;

public interface IPreparable<T extends IGeneratable> {

	default T prepare(World world, DungeonPlacement placement) {
		return CQRConfig.advanced.structureImportMode ? this.prepareDebug(world, placement) : this.prepareNormal(world, placement);
	}

	T prepareNormal(World world, DungeonPlacement placement);

	T prepareDebug(World world, DungeonPlacement placement);

}
