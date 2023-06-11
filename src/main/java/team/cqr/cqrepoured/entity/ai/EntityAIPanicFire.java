package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import team.cqr.cqrepoured.config.CQRConfig;

public class EntityAIPanicFire extends PanicGoal {

	public EntityAIPanicFire(CreatureEntity creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean canUse() {
		if (CQRConfig.SERVER_CONFIG.mobs.disableFirePanicAI.get()) {
			return false;
		}
		if (this.mob.fireImmune()) {
			return false;
		}
		if (this.mob.isInLava() || this.mob.isOnFire()) {
			return this.findRandomPosition();
		}
		return false;
	}

}
