package team.cqr.cqrepoured.entity.ai.boss.netherdragon;

import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.util.VectorUtil;

import java.util.EnumSet;

public class BossAICircleAroundLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	private Vec3 targetPosition = null;
	private Vec3 nextPosition = null;
	private Vec3 vAngle = null;
	private Vec3 direction = null;
	private Vec3 center = null;
	static double DELTA_Y = 1.5;
	static final double CIRCLING_HEIGHT = 20;
	protected static final double ANGLE_INCREMENT = 36;
	protected static final double MIN_DISTANCE_TO_TARGET = 5;

	private int attackTimer = 70;

	public BossAICircleAroundLocation(EntityCQRNetherDragon entity) {
		super(entity);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.entity.getCirclingCenter() != null && this.entity.deathTime <= 0 && !this.entity.isFlyingUp()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.canUse();
	}

	private void calculateTargetPositions(Vec3 vInit) {
		this.vAngle = vInit;
		if (this.center == null) {
			this.center = new Vec3(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
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
		this.nextPosition = this.nextPosition.add(new Vec3(0, DELTA_Y + CIRCLING_HEIGHT, 0));
		DELTA_Y *= -1;
		this.direction = this.nextPosition.subtract(this.targetPosition);
		this.direction = this.direction.normalize();
	}

	@Override
	public void tick() {
		super.tick();
		double dist = this.entity.distanceToSqr(new Vec3(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z));
		dist = Math.sqrt(dist);
		if (dist <= MIN_DISTANCE_TO_TARGET) {
			this.calculateTargetPositions();
		}
		this.entity.getNavigation().moveTo(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z, getSpeed());

		if (this.entity.getTarget() != null) {
			this.attackTimer--;
			if (this.attackTimer <= 0) {
				this.attackTimer = 40 + this.entity.getRandom().nextInt(81);
				this.entity.performRangedAttack(this.entity.getTarget(), 1);
			}
		}
	}

	private static double getSpeed() {
		return 0.15;
	}

	@Override
	public void stop() {
		this.vAngle = null;
		this.nextPosition = null;
		this.targetPosition = null;
	}

	@Override
	public void start() {
		super.start();
		if (this.targetPosition == null) {
			Vec3 v = new Vec3(32, 0, 0);
			this.calculateTargetPositions(v);
		}
	}

}
