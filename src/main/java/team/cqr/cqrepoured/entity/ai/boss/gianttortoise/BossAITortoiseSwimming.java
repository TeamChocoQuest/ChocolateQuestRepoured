package team.cqr.cqrepoured.entity.ai.boss.gianttortoise;

import net.minecraft.entity.ai.EntityAISwimming;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class BossAITortoiseSwimming extends EntityAISwimming {

	private EntityCQRGiantTortoise boss;

	public BossAITortoiseSwimming(EntityCQRGiantTortoise entityIn) {
		super(entityIn);
		this.boss = entityIn;
		this.setMutexBits(0);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute()) {
			return this.boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (super.shouldContinueExecuting()) {
			return this.boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.boss.getAttackTarget() != null) {
			this.boss.getNavigator().tryMoveToEntityLiving(this.boss.getAttackTarget(), 3);
		}
	}

}
