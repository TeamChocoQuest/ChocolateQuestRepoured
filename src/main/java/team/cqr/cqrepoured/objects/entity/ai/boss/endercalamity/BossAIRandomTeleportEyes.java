package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileHomingEnderEye;

public class BossAIRandomTeleportEyes extends AbstractBossAIRandomShoot {

	public BossAIRandomTeleportEyes(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected int execRandomShoot() {
		Vec3d v = this.entity.hasAttackTarget() ? this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector()) : this.entity.getLookVec();
		v = v.normalize();
		ProjectileHomingEnderEye eye = new ProjectileHomingEnderEye(this.entity.world, this.entity, this.entity.getAttackTarget());
		eye.motionX = v.x / 10;
		eye.motionY = v.y / 10;
		eye.motionZ = v.z / 10;
		eye.velocityChanged = true;
		this.world.spawnEntity(eye);
		return 18;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_EYE_THROWER;
	}
	
	@Override
	public int execPrepareShoot() {
		//TODO: play animation
		//40 is the transition time of the animation controller
		//3: AI only executes every 3 ticks
		return 40 / 3;
	}

}
