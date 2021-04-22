package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public abstract class AbstractBossAIEnderCalamity extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	public AbstractBossAIEnderCalamity(EntityCQREnderCalamity entity) {
		super(entity);
	}
	
	@Override
	public boolean shouldExecute() {
		return this.canExecuteDuringPhase(this.entity.getCurrentPhase());
	}

	protected abstract boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase);

}
