package team.cqr.cqrepoured.entity.ai.boss.gianttortoise;

import net.minecraft.entity.ai.goal.SwimGoal;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

import java.util.EnumSet;

public class BossAITortoiseSwimming extends SwimGoal {

	private EntityCQRGiantTortoise boss;

	public BossAITortoiseSwimming(EntityCQRGiantTortoise entityIn) {
		super(entityIn);
		this.boss = entityIn;
		//this.setMutexBits(0);
		this.setFlags(EnumSet.noneOf(Flag.class));
	}

	@Override
	public boolean canUse() {
		if (super.canUse()) {
			return this.boss.getTarget() != null;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (super.canContinueToUse()) {
			return this.boss.getTarget() != null;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.boss.getTarget() != null) {
			this.boss.getNavigation().createPath(this.boss.getTarget(), 3);
		}
	}

}
