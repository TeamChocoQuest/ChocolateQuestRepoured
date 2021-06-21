package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import javax.annotation.Nullable;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateMainAnimation;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileEnergyOrb;

public class BossAIEnergyTennis extends AbstractBossAIEnderCalamity {
	/*
	 * Animation lengths:
	 *  - Shooting: 
	 *  - Spawning (single, it is looped): 1,56s => 32 ticks (sound: 5s => 100 ticks)
	 *  Transition length: 10 ticks (it is 0 for the shooting
	 */

	@Nullable
	protected ProjectileEnergyOrb tennisball = null;
	private int ballTicks = 0;
	private int remainingAttempts = 0;
	private static final int WARMUP_DURATION = 100;
	private int warmupTime = WARMUP_DURATION;

	public void calculateRemainingAttempts() {
		switch(this.world.getDifficulty()) {
		case HARD:
			this.remainingAttempts = 3;
			break;
		case NORMAL:
			this.remainingAttempts = 6;
			break;
		default:
			this.remainingAttempts = 9;
			break;
		}
	}
	
	// TODO: Play sound

	public BossAIEnergyTennis(EntityCQREnderCalamity entity) {
		super(entity);
		this.calculateRemainingAttempts();
	}

	@Override
	public boolean shouldExecute() {
		return this.remainingAttempts > 0 && super.shouldExecute() && this.entity.hasAttackTarget();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && (this.tennisball != null || this.warmupTime >= 0) && this.ballTicks < 200;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.entity.setCantUpdatePhase(true);
		this.entity.forceTeleport();
		
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_CHARGE_ENERGY_BALL).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
	}

	@Override
	public void updateTask() {
		if (this.entity.hasAttackTarget()) {
			this.entity.faceEntity(this.entity.getAttackTarget(), 90, 90);
		}
		if (this.warmupTime > 0) {
			this.warmupTime--;
			return;
		}
		if (this.tennisball == null) {
			IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_BALL).build();
			CQRMain.NETWORK.sendToAllTracking(message, this.entity);
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
		this.warmupTime = WARMUP_DURATION;
		this.ballTicks = 0;
		this.entity.setCantUpdatePhase(false);
		// IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.remainingAttempts--;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_ENERGY_TENNIS;
	}

}
