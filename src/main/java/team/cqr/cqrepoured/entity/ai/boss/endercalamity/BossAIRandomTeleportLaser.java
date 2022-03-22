package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIRandomTeleportLaser extends AbstractBossAIRandomShoot {

	public BossAIRandomTeleportLaser(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected int execRandomShoot() {
		// System.out.println("original eyepos: " + eyePos.toString());
		// DONE: Calculate new starting position of laser to match animation
		// Head distance with scale = 100%: 0.75 blocks
		AbstractEntityLaser laser = new EntityEndLaserTargeting(this.entity, this.entity.getAttackTarget());
		laser.setupPositionAndRotation();
		this.world.addFreshEntity(laser);
		this.projectile = laser;
		// 5 ticks buffer
		// DONE: Make animation longer to make this longer
		// TODO: Mark AI to not change looking direction when in shooting state
		// Animation prep: 1s
		// Animation cooldown: 1s
		// Animation length total: 6s
		return 70;
	}

	@Override
	public boolean faceTarget() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		boolean value = this.projectile != null;

		if (this.entity.rotateBodyPitch() != value) {
			this.entity.setRotateBodyPitch(value);
		}

		if (value && this.projectile instanceof AbstractEntityLaser) {
			// this.entity.rotationPitch = ((AbstractEntityLaser)this.projectile).rotationPitchCQR;
			// DONE: Fix buggy rotation
			AbstractEntityLaser laser = (AbstractEntityLaser) this.projectile;
			//this.entity.rotationYaw = laser.rotationYawCQR /* + 90.0F */;
			//this.entity.prevRotationYaw = laser.prevRotationYawCQR /* + 90.0F */;
			//TODO: Is yBodyRot correct or should we use yRot?
			this.entity.yBodyRot = laser.rotationYawCQR /* + 90.0F */;
			this.entity.yBodyRotO = laser.prevRotationYawCQR /* + 90.0F */;

			this.entity.rotationPitchCQR = laser.rotationPitchCQR;
			this.entity.prevRotationPitchCQR = laser.prevRotationPitchCQR;

			// System.out.println("Laser pitch: " + laser.rotationPitchCQR);
			// System.out.println("Entity pitch: " + entity.rotationPitchCQR);

			//this.entity.rotationYawHead = this.entity.rotationYaw;
			//this.entity.prevRotationYawHead = this.entity.prevRotationYaw;
			this.entity.yHeadRot = this.entity.yBodyRot;
			this.entity.yHeadRotO = this.entity.yBodyRotO;
		}
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_LASER;
	}

	@Override
	public int execPrepareShoot() {
		// 10 is the transition time of the animation controller
		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_LASER).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_LASER);
		// 10 is the transition time
		// animation warmup is 1s => 20 ticks
		// 5 ticks is a little buffer
		return 35;
	}

	@Override
	public int execAfterShoot() {
		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY);

		this.killProjectile();
		// Animation cooldown time: 0.28s => 6 ticks
		// Transition time: 10 ticks
		return 30;
	}

	@Override
	public void stop() {
		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY);

		this.entity.setRotateBodyPitch(false);
		super.stop();
	}

}
