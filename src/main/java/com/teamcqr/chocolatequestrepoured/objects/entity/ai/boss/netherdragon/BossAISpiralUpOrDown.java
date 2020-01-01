package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon.EDragonMovementState;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAISpiralUpOrDown extends AbstractCQREntityAI {

	protected boolean flyingUp = false;
	// Height it will change = deltaYPerIteration * maxIterations
	protected static final int maxIterations = 20;
	protected int currentIterations = 0;
	protected Vec3d lastTarget;

	protected static final double deltaYPerIteration = 0.75D;
	protected static final double anglePerIteration = 36.0D; // 10 requires 36 iterations for one circle

	public BossAISpiralUpOrDown(EntityCQRNetherDragon dragon) {
		super(dragon);
	}

	protected EntityCQRNetherDragon getDragon() {
		return (EntityCQRNetherDragon) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		return (this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS)
				|| this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS));
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.currentIterations > maxIterations) {
			this.getDragon().updateMovementState(EDragonMovementState.FLYING);
			this.resetTask();
			return false;
		}
		return true;
	}

	@Override
	public void updateTask() {
		if (this.shouldContinueExecuting()) {
			if (this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS)) {
				this.flyingUp = false;
			} else if (this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_UPWARDS)) {
				this.flyingUp = true;
			}
			if (this.lastTarget == null || this.getDragon().getDistance(this.lastTarget.x, this.lastTarget.y, this.lastTarget.z) <= 0.5D) {
				this.currentIterations++;

				// DONE: Calculate position and move to it
				Vec3d newPos = new Vec3d(this.getDragon().posX, this.getDragon().posY, this.getDragon().posZ);

				// DONE: Redo this
				// Get the current movement vector of the entity (rotation on x/z plane)
				// Rotate this vector by the anglePerIteration
				// Add this vector to the entities current pos and we have the new target
				Vec3d vec = new Vec3d(this.getDragon().motionX, 0, this.getDragon().motionZ);// new Vec3d(0,0,3D);
				vec = vec.normalize();
				vec = vec.add(vec).add(vec);
				// DONE: Adjust this vector to the direction the dragon is looking in?
				newPos = newPos.add(VectorUtil.rotateVectorAroundY(vec, this.flyingUp ? anglePerIteration * (this.currentIterations + 1) : 360.0D - (this.currentIterations + 1)));
				newPos.addVector(0, this.flyingUp ? deltaYPerIteration : -deltaYPerIteration, 0);

				this.lastTarget = newPos;
			}
			this.getDragon().getNavigator().tryMoveToXYZ(this.lastTarget.x, this.lastTarget.y, this.lastTarget.z, 1.1F);

		} else {
			this.resetTask();
		}
	}

	@Override
	public void resetTask() {
		this.currentIterations = 0;
	}

}
