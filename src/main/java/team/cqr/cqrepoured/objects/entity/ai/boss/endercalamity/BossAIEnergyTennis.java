package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import javax.annotation.Nullable;

import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileEnergyOrb;

public class BossAIEnergyTennis extends AbstractBossAIEnderCalamity {

	@Nullable
	protected ProjectileEnergyOrb tennisball = null;
	private int ballTicks = 0;
	private int cooldown = 30;

	// TODO: Play animation

	public BossAIEnergyTennis(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && this.entity.hasAttackTarget();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && (this.tennisball != null || this.cooldown >= 0) && this.ballTicks < 200;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.entity.setCantUpdatePhase(true);
		this.entity.forceTeleport();
	}

	@Override
	public void updateTask() {
		if (this.entity.hasAttackTarget()) {
			this.entity.faceEntity(this.entity.getAttackTarget(), 90, 90);
		}
		if (this.cooldown > 0) {
			this.cooldown--;
			return;
		}
		if (this.tennisball == null) {
			this.tennisball = ProjectileEnergyOrb.shootAt(this.entity.getAttackTarget(), this.entity, this.world);
		} else {
			this.ballTicks++;
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		if (this.tennisball != null) {
			this.tennisball.setDead();
		}
		this.tennisball = null;
		this.cooldown = 15;
		this.ballTicks = 0;
		this.entity.setCantUpdatePhase(false);
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_ENERGY_TENNIS;
	}

}
