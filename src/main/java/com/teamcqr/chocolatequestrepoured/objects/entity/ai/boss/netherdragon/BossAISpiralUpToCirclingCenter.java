package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAISpiralUpToCirclingCenter extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	private static final double MIN_DISTANCE_TO_HOME = 8;
	private static final double MIN_DISTANCE_TO_NODE = 3;
	private Vec3d v = new Vec3d(5,2,0);
	private Vec3d center = new Vec3d(0,0,0);
	private Vec3d targetPos = center;
	private double angleIncrement = 45;
	
	public BossAISpiralUpToCirclingCenter(EntityCQRNetherDragon entity) {
		super(entity);
		setMutexBits(1 | 2 | 4);
	}

	@Override
	public boolean shouldExecute() {
		Vec3d center = new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
		double yCirclingCenter = center.y + BossAICircleAroundLocation.CIRCLING_HEIGHT + (1.5* BossAICircleAroundLocation.DELTA_Y);
		return entity.getPositionVector().distanceTo(center) <= MIN_DISTANCE_TO_HOME && entity.posY < yCirclingCenter;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		Vec3d center = new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
		double yCirclingCenter = center.y + BossAICircleAroundLocation.CIRCLING_HEIGHT + (1.5* BossAICircleAroundLocation.DELTA_Y);
		return this.entity.posY < yCirclingCenter;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.entity.setFlyingUp(true);
		this.center = new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
		this.targetPos = center.add(v);
		this.entity.getNavigator().tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, 1.5);
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		if(entity.getPositionVector().distanceTo(targetPos) <= MIN_DISTANCE_TO_NODE) {
			v = VectorUtil.rotateVectorAroundY(v, angleIncrement);
			center = center.add(0, v.y, 0);
			this.targetPos = center.add(v);
			//System.out.println("Center: " + center.toString());
		}
		this.entity.getNavigator().tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, 1.5);
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.setFlyingUp(false);
		this.center = new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
	}


}
