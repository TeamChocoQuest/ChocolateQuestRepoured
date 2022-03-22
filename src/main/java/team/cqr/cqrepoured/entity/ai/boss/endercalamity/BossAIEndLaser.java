package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

import java.util.EnumSet;

public class BossAIEndLaser extends AbstractBossAIEnderCalamity {

	private AbstractEntityLaser endlaser;
	private int executionTime = 0;

	private static final int LASER_SPAWN_TIME = 10;

	public BossAIEndLaser(EntityCQREnderCalamity entity) {
		super(entity);
		//this.setMutexBits(2);
		this.setFlags(EnumSet.of(Flag.LOOK));
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
			//this.entity.rotationYaw = this.endlaser.rotationYawCQR + 90.0F;
			//this.entity.prevRotationYaw = this.endlaser.prevRotationYawCQR + 90.0F;
			//TODO: Is yBodyRot correct or should we use yRot?
			this.entity.yBodyRot = this.endlaser.rotationYawCQR + 90.0F;
			this.entity.yBodyRotO = this.endlaser.prevRotationYawCQR + 90.0F;

			this.entity.rotationPitchCQR = this.endlaser.rotationPitchCQR;
			this.entity.prevRotationPitchCQR = this.endlaser.prevRotationPitchCQR;

			//this.entity.rotationYawHead = this.entity.rotationYaw;
			//this.entity.prevRotationYawHead = this.entity.prevRotationYaw;
			this.entity.yHeadRot = this.entity.yBodyRot;
			this.entity.yHeadRotO = this.entity.yBodyRotO;
		}
	}

	private void createLaser() {
		Vector3d laserPosition = this.entity.position();
		laserPosition = laserPosition.add(0, this.entity.getBbHeight() / 2, 0);
		// System.out.println("original eyepos: " + eyePos.toString());
		// DONE: Calculate new starting position of laser to match animation
		// Head distance with scale = 100%: 0.75 blocks
		float yaw = this.entity.yRot;
		if (this.entity.hasAttackTarget()) {
			yaw = (float) Math.toDegrees(Math.atan2(-(this.entity.getTarget().getX() - this.entity.getX()), this.entity.getTarget().getZ() - this.entity.getZ()));
		}

		AbstractEntityLaser laser = new EntityEndLaser(this.entity.getWorld(), this.entity, 64.0F, 8.0F, -0.01F);
		laser.rotationYawCQR = yaw;
		laser.rotationPitchCQR = 11.25F;
		laser.setPos(laserPosition.x, laserPosition.y, laserPosition.z);
		this.world.addFreshEntity(laser);
		this.endlaser = laser;
	}

	@Override
	public void stop() {
		if (this.endlaser != null) {
			this.endlaser.remove();
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
