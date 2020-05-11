package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAICircleAroundLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {
	
	
	private Vec3d targetPosition = null;
	private Vec3d nextPosition = null;
	private Vec3d vAngle = null;
	private Vec3d direction = null;
	private Vec3d center = null;
	static double DELTA_Y = 3;
	static final double CIRCLING_HEIGHT = 20;
	protected static final double ANGLE_INCREMENT = 36;
	protected static final double MIN_DISTANCE_TO_TARGET = 5;
	
	public BossAICircleAroundLocation(EntityCQRNetherDragon entity) {
		super(entity);
		setMutexBits(1|2|4);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity.getCirclingCenter() != null && this.entity.deathTicks <= 0 && !this.entity.isFlyingUp()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && shouldExecute();
	}

	private void calculateTargetPositions(Vec3d vInit) {
		this.vAngle = vInit;
		if(this.center == null) {
			this.center = new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
		}
		calculateTargetPositions();
	}
	
	private void calculateTargetPositions() {
		if(this.nextPosition == null) {
			this.targetPosition = center.add(vAngle);
		} else {
			this.targetPosition = this.nextPosition;
		}
		vAngle = VectorUtil.rotateVectorAroundY(vAngle, ANGLE_INCREMENT);
		this.nextPosition = center.add(vAngle);
		this.nextPosition = this.nextPosition.add(new Vec3d(0,DELTA_Y + CIRCLING_HEIGHT,0));
		DELTA_Y *= -1;
		this.direction = this.nextPosition.subtract(targetPosition);
		this.direction = this.direction.normalize();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		double dist = this.entity.getDistance(targetPosition.x, targetPosition.y, targetPosition.z);
		if(dist <= MIN_DISTANCE_TO_TARGET) {
			calculateTargetPositions();
		}
		this.entity.getNavigator().tryMoveToXYZ(targetPosition.x, targetPosition.y, targetPosition.z, 1);
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
		if(this.targetPosition == null) {
			Vec3d v = new Vec3d(32, 0, 0);
			calculateTargetPositions(v); 
		}
	}
	
}
