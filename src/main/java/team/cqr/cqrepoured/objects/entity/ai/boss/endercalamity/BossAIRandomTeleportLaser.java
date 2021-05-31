package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateMainAnimation;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIRandomTeleportLaser extends AbstractBossAIRandomShoot {

	public BossAIRandomTeleportLaser(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected int execRandomShoot() {
		Vec3d laserPosition = this.entity.getPositionVector();
		laserPosition = laserPosition.add(0, this.entity.height / 2, 0);
		// System.out.println("original eyepos: " + eyePos.toString());
		// DONE: Calculate new starting position of laser to match animation
		// Head distance with scale = 100%: 0.75 blocks
		AbstractEntityLaser laser = new EntityEndLaserTargeting(this.entity, this.entity.getAttackTarget(), Vec3d.ZERO);
		laser.setPosition(laserPosition.x, laserPosition.y, laserPosition.z);
		this.world.spawnEntity(laser);
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
	public void updateTask() {
		super.updateTask();
		boolean value = this.projectile != null;
		
		if(this.entity.rotateBodyPitch() != value) {
			this.entity.setRotateBodyPitch(value);
		}
		
		if (value && this.projectile instanceof AbstractEntityLaser) {
			// this.entity.rotationPitch = ((AbstractEntityLaser)this.projectile).rotationPitchCQR;
			// DONE: Fix buggy rotation
			AbstractEntityLaser laser = (AbstractEntityLaser) this.projectile;
			this.entity.rotationYaw = (float) laser.rotationYawCQR + 90.0F;
			this.entity.prevRotationYaw = (float) laser.prevRotationYawCQR + 90.0F;

			this.entity.rotationPitch = (float) laser.rotationPitchCQR;
			this.entity.prevRotationPitch = (float) laser.prevRotationPitchCQR;

			this.entity.rotationYawHead = this.entity.rotationYaw;
			this.entity.prevRotationYawHead = this.entity.prevRotationYaw;
		}
		
		
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_LASER;
	}

	@Override
	public int execPrepareShoot() {
		// 10 is the transition time of the animation controller
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_LASER).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		// 10 is the transition time
		// animation warmup is 1s => 20 ticks
		// 5 ticks is a little buffer
		return 35;
	}

	@Override
	public int execAfterShoot() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.killProjectile();
		// Animation cooldown time: 0.28s => 6 ticks
		// Transition time: 10 ticks
		return 30;
	}

	@Override
	public void resetTask() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		
		this.entity.setRotateBodyPitch(false);
		super.resetTask();
	}

}
