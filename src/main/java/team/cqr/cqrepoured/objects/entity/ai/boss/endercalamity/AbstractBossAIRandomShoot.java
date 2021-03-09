package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public abstract class AbstractBossAIRandomShoot extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	private int cooldown = 0;
	private E_PHASE currentPhase = E_PHASE.PREPARING_TO_SHOOT;
	@Nullable
	protected Entity projectile = null;
	
	private static enum E_PHASE {
		TELEPORT,
		SHOOTING,
		PREPARING_TO_SHOOT,
		PREPARING_TO_TELEPORT
	}
	
	public AbstractBossAIRandomShoot(EntityCQREnderCalamity entity) {
		super(entity);
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.entity != null && this.entity.hasAttackTarget()) {
			return this.canExecuteDuringPhase(this.entity.getCurrentPhase());
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.currentPhase = E_PHASE.PREPARING_TO_TELEPORT;
		this.cooldown = 10;
	}
	
	@Override
	public void updateTask() {
		this.cooldown--;
		if(this.entity.hasAttackTarget()) {
			this.entity.faceEntity(this.entity.getAttackTarget(), 90, 90);
			this.entity.setCantUpdatePhase(false);
		}
		//Timer gets set to the timer of the NEXT phase
		//The case basically checks for the phase we are in CURRENTLY
		if(this.cooldown <= 0) {
			switch(this.currentPhase) {
			case PREPARING_TO_SHOOT:
				this.cooldown = this.execRandomShoot();
				this.currentPhase = E_PHASE.SHOOTING;
				break;
			case PREPARING_TO_TELEPORT:
				if(this.projectile != null) {
					this.projectile.setDead();
				}
				this.cooldown = 1;
				this.currentPhase = E_PHASE.TELEPORT;
				break;
			case SHOOTING:
				this.cooldown = 10;
				this.currentPhase = E_PHASE.PREPARING_TO_TELEPORT;
				this.execAfterShoot();
				break;
			case TELEPORT:
				this.entity.forceTeleport();
				this.cooldown = this.execPrepareShoot();
				this.currentPhase = E_PHASE.PREPARING_TO_SHOOT;
				break;
			default:
				break;
			}
			this.entity.setCantUpdatePhase(this.currentPhase != E_PHASE.PREPARING_TO_TELEPORT || this.currentPhase != E_PHASE.TELEPORT );
		}
	}
	
	public void execAfterShoot() {}
	
	public abstract int execPrepareShoot();
	
	@Override
	public void resetTask() {
		if(this.projectile != null) {
			this.projectile.setDead();
		}
		this.entity.setCantUpdatePhase(false);
		super.resetTask();
	}
	
	/*
	 * Returns the duration of the random shoot
	 */
	protected abstract int execRandomShoot();
	protected abstract boolean canExecuteDuringPhase(EEnderCalamityPhase phase);

}
