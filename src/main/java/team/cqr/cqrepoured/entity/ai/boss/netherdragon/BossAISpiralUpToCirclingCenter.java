package team.cqr.cqrepoured.entity.ai.boss.netherdragon;

import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAISpiralUpToCirclingCenter extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	private static final double MIN_DISTANCE_TO_HOME = 8;
	private static final double MIN_DISTANCE_TO_NODE = 3;
	private Vector3d v = new Vector3d(5, 2, 0);
	private Vector3d center = new Vector3d(0, 0, 0);
	private Vector3d targetPos = this.center;
	private double angleIncrement = 45;

	public BossAISpiralUpToCirclingCenter(EntityCQRNetherDragon entity) {
		super(entity);
		this.setMutexBits(1 | 2 | 4);
	}

	@Override
	public boolean shouldExecute() {
		Vector3d center = new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
		double yCirclingCenter = center.y + BossAICircleAroundLocation.CIRCLING_HEIGHT + (1.5 * BossAICircleAroundLocation.DELTA_Y);
		return this.entity.getPositionVector().distanceTo(center) <= MIN_DISTANCE_TO_HOME && this.entity.posY < yCirclingCenter;
	}

	@Override
	public boolean shouldContinueExecuting() {
		Vector3d center = new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
		double yCirclingCenter = center.y + BossAICircleAroundLocation.CIRCLING_HEIGHT + (1.5 * BossAICircleAroundLocation.DELTA_Y);
		return this.entity.posY < yCirclingCenter;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.entity.setFlyingUp(true);
		this.center = new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
		this.targetPos = this.center.add(this.v);
		this.entity.getNavigator().tryMoveToXYZ(this.targetPos.x, this.targetPos.y, this.targetPos.z, getSpeed());
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.entity.getPositionVector().distanceTo(this.targetPos) <= MIN_DISTANCE_TO_NODE) {
			this.v = VectorUtil.rotateVectorAroundY(this.v, this.angleIncrement);
			this.center = this.center.add(0, this.v.y, 0);
			this.targetPos = this.center.add(this.v);
			// System.out.println("Center: " + center.toString());
		}
		this.entity.getNavigator().tryMoveToXYZ(this.targetPos.x, this.targetPos.y, this.targetPos.z, getSpeed());
	}

	private static double getSpeed() {
		return 0.15;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.setFlyingUp(false);
		this.center = new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ());
	}

}
