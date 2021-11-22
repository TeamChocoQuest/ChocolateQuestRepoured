package team.cqr.cqrepoured.objects.entity.ai.boss.exterminator;

import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;

public class BossAIExterminatorStun extends AbstractCQREntityAI<EntityCQRExterminator> {

	public BossAIExterminatorStun(EntityCQRExterminator entity) {
		super(entity);
		this.setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.isStunned() || TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this.entity);
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

}
