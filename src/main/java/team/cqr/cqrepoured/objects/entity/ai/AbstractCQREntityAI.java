package team.cqr.cqrepoured.objects.entity.ai;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public abstract class AbstractCQREntityAI<T extends AbstractEntityCQR> extends EntityAIBase {

	protected final Random random = new Random();
	protected final T entity;
	protected final World world;

	public AbstractCQREntityAI(T entity) {
		this.entity = entity;
		this.world = entity.world;
	}

}
