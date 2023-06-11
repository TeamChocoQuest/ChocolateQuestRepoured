package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.world.entity.ai.goal.Goal.Flag;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;

public abstract class AbstractBossAIRandomShoot extends AbstractBossAIEnderCalamity {

	private int cooldown;
	private E_PHASE currentPhase = E_PHASE.PREPARING_TO_SHOOT;
	@Nullable
	protected Entity projectile;

	private enum E_PHASE {
		TELEPORT, SHOOTING, PREPARING_TO_SHOOT, PREPARING_TO_TELEPORT
	}

	protected AbstractBossAIRandomShoot(EntityCQREnderCalamity entity) {
		super(entity);
		//this.setMutexBits(2);
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.entity != null && this.entity.hasAttackTarget()) {
			return this.canExecuteDuringPhase(this.entity.getCurrentPhase());
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void start() {
		super.start();
		this.currentPhase = E_PHASE.PREPARING_TO_TELEPORT;
		this.cooldown = this.execPrepareShoot();
	}

	public boolean faceTarget() {
		return true;
	}

	@Override
	public void tick() {
		this.cooldown--;
		if (this.entity.hasAttackTarget()) {
			if (this.faceTarget()) {
				this.entity.getLookControl().setLookAt(this.entity.getTarget(), 90, 90);
			}
			this.entity.setCantUpdatePhase(false);
		}
		// Timer gets set to the timer of the NEXT phase
		// The case basically checks for the phase we are in CURRENTLY
		if (this.cooldown <= 0) {
			switch (this.currentPhase) {
			case PREPARING_TO_SHOOT:
				this.cooldown = this.execRandomShoot();
				this.currentPhase = E_PHASE.SHOOTING;
				break;
			case PREPARING_TO_TELEPORT:
				if (this.projectile != null) {
					this.projectile.remove();
				}
				this.cooldown = 1;
				this.currentPhase = E_PHASE.TELEPORT;
				break;
			case SHOOTING:
				this.cooldown = 10;
				this.currentPhase = E_PHASE.PREPARING_TO_TELEPORT;
				this.cooldown += this.execAfterShoot();
				break;
			case TELEPORT:
				this.entity.forceTeleport();
				this.cooldown = this.execPrepareShoot();
				this.currentPhase = E_PHASE.PREPARING_TO_SHOOT;
				break;
			default:
				break;
			}
			this.entity.setCantUpdatePhase(this.currentPhase != E_PHASE.PREPARING_TO_TELEPORT || this.currentPhase != E_PHASE.TELEPORT);
		}
	}

	public int execAfterShoot() {
		return 1;
	}

	public abstract int execPrepareShoot();

	@Override
	public void stop() {
		this.killProjectile();
		this.entity.setCantUpdatePhase(false);
		super.stop();
	}

	protected void killProjectile() {
		if (this.projectile != null) {
			this.projectile.remove();
		}
	}

	/*
	 * Returns the duration of the random shoot
	 */
	protected abstract int execRandomShoot();

}
