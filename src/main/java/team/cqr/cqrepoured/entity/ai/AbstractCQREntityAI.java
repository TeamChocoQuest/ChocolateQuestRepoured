package team.cqr.cqrepoured.entity.ai;

import java.util.Random;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractCQREntityAI<T extends AbstractEntityCQR> extends Goal {

	protected final Random random = new Random();
	protected final T entity;
	protected final Level world;

	protected AbstractCQREntityAI(T entity) {
		this.entity = entity;
		this.world = entity.level();
	}

}
