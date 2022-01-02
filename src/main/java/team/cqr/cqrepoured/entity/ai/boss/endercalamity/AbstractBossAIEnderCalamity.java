package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public abstract class AbstractBossAIEnderCalamity extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	protected AbstractBossAIEnderCalamity(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		return this.canExecuteDuringPhase(this.entity.getCurrentPhase());
	}

	protected abstract boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase);

}
