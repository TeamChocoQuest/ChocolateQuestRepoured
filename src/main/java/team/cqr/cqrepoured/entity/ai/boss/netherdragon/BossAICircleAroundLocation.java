package team.cqr.cqrepoured.entity.ai.boss.netherdragon;

import java.util.EnumSet;

import org.joml.Vector3d;

import net.minecraft.world.entity.ai.goal.Goal.Flag;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAICircleAroundLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	private Vector3d targetPosition = null;
	private Vector3d nextPosition = null;
	private Vector3d vAngle = null;
	private Vector3d direction = null;
	private Vector3d center = null;
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

	private void calculateTargetPositions(Vector3d vInit) {
		this.vAngle = vInit;
		if (this.center == null) {
			this.center = new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
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
		this.nextPosition = this.nextPosition.add(new Vector3d(0, DELTA_Y + CIRCLING_HEIGHT, 0));
		DELTA_Y *= -1;
		this.direction = this.nextPosition.subtract(this.targetPosition);
		this.direction = this.direction.normalize();
	}

	@Override
	public void tick() {
		super.tick();
		double dist = this.entity.distanceToSqr(new Vector3d(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z));
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
			Vector3d v = new Vector3d(32, 0, 0);
			this.calculateTargetPositions(v);
		}
	}

}
