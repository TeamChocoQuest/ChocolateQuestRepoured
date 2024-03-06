package team.cqr.cqrepoured.api.event.worldgen;

import java.util.Map;

import de.dertoaster.multihitboxlib.api.event.AbstractRegistrationEvent;
import net.minecraft.world.level.block.Block;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.block.PreparablePosInfo;

public class PreparerRegistrationEvent extends AbstractRegistrationEvent<Class<? extends Block>, PreparablePosInfo> {

	public PreparerRegistrationEvent(Map<Class<? extends Block>, PreparablePosInfo> map) {
		super(map);
	}

}
