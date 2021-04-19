package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;

public class BossAIStunned extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	public BossAIStunned(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.isDowned();
	}

	@Override
	public void startExecuting() {
		if(this.entity.hasHomePositionCQR()) {
			BlockPos home = this.entity.getHomePositionCQR();
			this.entity.attemptTeleport(home.getX(), home.getY(), home.getZ());
		}
	}
	
}
