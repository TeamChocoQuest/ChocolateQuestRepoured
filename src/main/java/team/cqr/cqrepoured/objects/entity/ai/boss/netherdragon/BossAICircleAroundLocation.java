package team.cqr.cqrepoured.objects.entity.ai.boss.netherdragon;

import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNetherDragon;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAICircleAroundLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	private Vec3d targetPosition = null;
	private Vec3d nextPosition = null;
	private Vec3d vAngle = null;
	private Vec3d direction = null;
	private Vec3d center = null;
	static double DELTA_Y = 1.5;
	static final double CIRCLING_HEIGHT = 20;
	protected static final double ANGLE_INCREMENT = 36;
	protected static final double MIN_DISTANCE_TO_TARGET = 5;

	private int attackTimer = 70;

	public BossAICircleAroundLocation(EntityCQRNetherDragon entity) {
		super(entity);
		this.setMutexBits(1 | 2 | 4);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.getCirclingCenter() != null && this.entity.deathTime <= 0 && !this.entity.isFlyingUp()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.shouldExecute();
	}

	private void calculateTargetPositions(Vec3d vInit) {
		this.vAngle = vInit;
		if (this.center == null) {
			this.center = new Vec3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
		}
		this.calculateTargetPositions();
	}

	private void calculateTargetPositions() {
		if (this.nextPosition == null) {
			this.targetPosition = this.center.add(this.vAngle);
		} else {
			this.targetPosition = this.nextPosition;
		}
		this.vAngle = VectorUtil.rotateVectorAroundY(this.vAngle, ANGLE_INCREMENT);
		this.nextPosition = this.center.add(this.vAngle);
		this.nextPosition = this.nextPosition.add(new Vec3d(0, DELTA_Y + CIRCLING_HEIGHT, 0));
		DELTA_Y *= -1;
		this.direction = this.nextPosition.subtract(this.targetPosition);
		this.direction = this.direction.normalize();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		double dist = this.entity.getDistance(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z);
		if (dist <= MIN_DISTANCE_TO_TARGET) {
			this.calculateTargetPositions();
		}
		this.entity.getNavigator().tryMoveToXYZ(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z, getSpeed());

		if (this.entity.getAttackTarget() != null) {
			this.attackTimer--;
			if (this.attackTimer <= 0) {
				this.attackTimer = 40 + this.entity.getRNG().nextInt(81);
				this.entity.attackEntityWithRangedAttack(this.entity.getAttackTarget(), 1);
			}
		}
	}

	private static double getSpeed() {
		return 0.15;
	}

	@Override
	public void resetTask() {
		this.vAngle = null;
		this.nextPosition = null;
		this.targetPosition = null;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		if (this.targetPosition == null) {
			Vec3d v = new Vec3d(32, 0, 0);
			this.calculateTargetPositions(v);
		}
	}

}
