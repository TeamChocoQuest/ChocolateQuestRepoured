package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import java.util.Random;

public abstract class AbstractCQREntityAI<T extends AbstractEntityCQR> extends Goal {

	protected final Random random = new Random();
	protected final T entity;
	protected final World world;

	protected AbstractCQREntityAI(T entity) {
		this.entity = entity;
		this.world = entity.level;
	}

}
