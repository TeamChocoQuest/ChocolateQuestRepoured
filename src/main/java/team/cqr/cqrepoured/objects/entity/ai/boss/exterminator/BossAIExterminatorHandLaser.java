package team.cqr.cqrepoured.objects.entity.ai.boss.exterminator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityExterminatorHandLaser;

public class BossAIExterminatorHandLaser extends AbstractCQREntityAI<EntityCQRExterminator> {

	private static final int MAX_DISTANCE = 32;
	private static final int MIN_DISTANCE = 8;

	private AbstractEntityLaser activeLaser = null;
	private EntityLivingBase target = null;

	private int timer;
	private int timeOut = 0;

	public BossAIExterminatorHandLaser(EntityCQRExterminator entity) {
		super(entity);

		this.setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		if (this.timeOut > 0) {
			this.timeOut--;
			return false;
		}
		if (this.entity != null && this.entity.isEntityAlive() && this.entity.hasAttackTarget() && !this.entity.isCurrentlyPlayingAnimation() && (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.5F)) {
			final float distance = this.entity.getDistance(this.entity.getAttackTarget());
			this.target = this.entity.getAttackTarget();
			return this.entity.hasAttackTarget() && distance <= MAX_DISTANCE && distance >= MIN_DISTANCE;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity != null && this.entity.isEntityAlive() && this.target != null && this.target.isEntityAlive() && this.timer > 0;
	}

	@Override
	public void startExecuting() {
		this.timer = 150;
		super.startExecuting();
		checkAndOrStartCannonLaser();
	}

	private boolean checkAndOrStartCannonLaser() {
		// If the laser already exists => no need to validate the rest!
		if (this.activeLaser != null) {
			return true;
		}
		if (!(this.entity.isCannonArmReadyToShoot() && this.entity.isCannonRaised())) {
			// System.out.println("cannon not ready, can we raise it?");
			if (!this.entity.isCannonRaised()) {
				// System.out.println("cannon is not raised, telling it to raise...");
				if (this.entity.switchCannonArmState(true)) {
					// System.out.println("raise command received!");
				} else {
					// System.out.println("Raise command failed?!");
				}
			}
		} else {
			// System.out.println("Cannon is ready and cannon is raised");
			if (this.activeLaser == null) {
				// System.out.println("Laser does not exist, cannon is raised, so create the laser...");
				this.activeLaser = new EntityExterminatorHandLaser(this.entity, this.target, Vec3d.ZERO);
				this.world.spawnEntity(this.activeLaser);
			}
			return true;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.timer--;
		// System.out.println("Executing...");
		if (this.checkAndOrStartCannonLaser()) {
			// System.out.println("we have a laser, let's position it...");
			this.entity.rotationYaw = (float) this.activeLaser.rotationYawCQR /* + 90.0F */;
			this.entity.prevRotationYaw = (float) this.activeLaser.prevRotationYawCQR /* + 90.0F */;

			this.entity.faceEntity(this.target, 90, 90);
		} else {
			// System.out.println("No laser :/");
		}
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.target = null;
		this.timer = 300;
		if (this.activeLaser != null) {
			this.activeLaser.setDead();
			this.activeLaser = null;
		}
		this.entity.setCannonArmAutoTimeoutForLowering(40);
		this.timeOut = 200;
	}

}
