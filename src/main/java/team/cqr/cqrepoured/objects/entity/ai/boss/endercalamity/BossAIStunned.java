package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIStunned extends AbstractBossAIEnderCalamity {

	public BossAIStunned(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.isDowned();
	}

	@Override
	public void startExecuting() {
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
