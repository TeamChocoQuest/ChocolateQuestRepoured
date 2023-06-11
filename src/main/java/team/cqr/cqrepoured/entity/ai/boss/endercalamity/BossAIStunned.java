package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIStunned extends AbstractBossAIEnderCalamity {

	public BossAIStunned(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		return this.entity.isDowned();
	}

	@Override
	public void start() {
		if (this.entity.hasHomePositionCQR()) {
			BlockPos home = this.entity.getHomePositionCQR();
			this.entity.teleport(home.getX(), home.getY(), home.getZ());
		}
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return true;
	}

}
