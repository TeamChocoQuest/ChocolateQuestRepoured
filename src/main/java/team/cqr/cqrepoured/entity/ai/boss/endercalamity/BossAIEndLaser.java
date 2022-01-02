package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIEndLaser extends AbstractBossAIEnderCalamity {

	private AbstractEntityLaser endlaser;
	private int executionTime = 0;

	private static final int LASER_SPAWN_TIME = 10;

	public BossAIEndLaser(EntityCQREnderCalamity entity) {
		super(entity);
		this.setMutexBits(2);
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_LASERING;
	}

	@Override
	public void start() {
		this.executionTime = 0;
		if (this.entity.hasHomePositionCQR()) {
			BlockPos home = this.entity.getHomePositionCQR();
			this.entity.teleport(home.getX(), home.getY(), home.getZ());
			// TODO: Remove blocks in the center
		}

		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_LASER_STATIONARY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_LASER_STATIONARY);
	}

	@Override
	public void tick() {
		super.tick();

		this.executionTime++;

		// Summon laser
		if (this.executionTime == LASER_SPAWN_TIME) {
			this.createLaser();
		}
		if (this.endlaser != null) {
			// DONE: Fix buggy rotation
			this.entity.rotationYaw = this.endlaser.rotationYawCQR + 90.0F;
			this.entity.prevRotationYaw = this.endlaser.prevRotationYawCQR + 90.0F;

			this.entity.rotationPitchCQR = this.endlaser.rotationPitchCQR;
			this.entity.prevRotationPitchCQR = this.endlaser.prevRotationPitchCQR;

			this.entity.rotationYawHead = this.entity.rotationYaw;
			this.entity.prevRotationYawHead = this.entity.prevRotationYaw;
		}
	}

	private void createLaser() {
		Vector3d laserPosition = this.entity.position();
		laserPosition = laserPosition.add(0, this.entity.height / 2, 0);
		// System.out.println("original eyepos: " + eyePos.toString());
		// DONE: Calculate new starting position of laser to match animation
		// Head distance with scale = 100%: 0.75 blocks
		float yaw = this.entity.rotationYaw;
		if (this.entity.hasAttackTarget()) {
			yaw = (float) Math.toDegrees(Math.atan2(-(this.entity.getAttackTarget().posX - this.entity.posX), this.entity.getAttackTarget().posZ - this.entity.posZ));
		}

		AbstractEntityLaser laser = new EntityEndLaser(this.entity.getEntityWorld(), this.entity, 64.0F, 8.0F, -0.01F);
		laser.rotationYawCQR = yaw;
		laser.rotationPitchCQR = 11.25F;
		laser.setPosition(laserPosition.x, laserPosition.y, laserPosition.z);
		this.world.spawnEntity(laser);
		this.endlaser = laser;
	}

	@Override
	public void stop() {
		if (this.endlaser != null) {
			this.endlaser.setDead();
			this.endlaser = null;
		}
		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY);
		this.entity.setCantUpdatePhase(false);
		this.entity.forceTeleport();
		super.stop();
	}

}
