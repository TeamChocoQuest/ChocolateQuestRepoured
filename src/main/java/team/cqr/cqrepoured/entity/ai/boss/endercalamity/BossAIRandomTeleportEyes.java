package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHomingEnderEye;

public class BossAIRandomTeleportEyes extends AbstractBossAIRandomShoot {

	public BossAIRandomTeleportEyes(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected int execRandomShoot() {
		Vec3 v = this.entity.hasAttackTarget() ? this.entity.getTarget().position().subtract(this.entity.position()) : this.entity.getLookAngle();
		v = v.normalize();
		ProjectileHomingEnderEye eye = new ProjectileHomingEnderEye(this.entity, this.entity.level(), this.entity.getTarget());
		/*eye.motionX = v.x / 10;
		eye.motionY = v.y / 10;
		eye.motionZ = v.z / 10;
		eye.velocityChanged = true;*/
		eye.setDeltaMovement(v.scale(0.1));
		eye.hasImpulse = true;
		this.world.addFreshEntity(eye);
		return 18;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_EYE_THROWER;
	}

	@Override
	public int execPrepareShoot() {
		// TODO: play animation
		// 40 is the transition time of the animation controller
		// 3: AI only executes every 3 ticks
		return 40 / 3;
	}

	@Override
	protected void killProjectile() {
	}

}
