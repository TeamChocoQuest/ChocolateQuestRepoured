package team.cqr.cqrepoured.entity.ai.boss.exterminator;

import java.util.EnumSet;

import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;

public class BossAIExterminatorStun extends AbstractCQREntityAI<EntityCQRExterminator> {

	public BossAIExterminatorStun(EntityCQRExterminator entity) {
		super(entity);
		//this.setMutexBits(7);
		this.setFlags(EnumSet.allOf(Flag.class));
	}

	@Override
	public boolean canUse() {
		return this.entity.isStunned() || TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this.entity);
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
