package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon.EDragonMovementState;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class BossAISpiralUpOrDown extends EntityAIBase {

	protected boolean flyingUp = false;
	protected EntityCQRNetherDragon dragon;
	
	//Height it will change = deltaYPerIteration * maxIterations
	protected static final int maxIterations = 20;
	protected int currentIterations = 0;
	protected Vec3d lastTarget;
	
	protected static final double deltaYPerIteration = 0.75D;
	protected static final double anglePerIteration = 36.0D; //10 requires 36 iterations for one circle
	
	public BossAISpiralUpOrDown(EntityCQRNetherDragon dragon) {
		this.dragon = dragon;
	}

	@Override
	public boolean shouldExecute() {
		return (this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS) || this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS));
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(currentIterations > maxIterations) {
			this.dragon.updateMovementState(EDragonMovementState.FLYING);
			resetTask();
			return false;
		}
		return true;
	}
	
	@Override
	public void updateTask() {
		if(shouldContinueExecuting()) {
			if(this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS)) {
				flyingUp = false;
			} else if(this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_UPWARDS)) {
				flyingUp = true;
			}
			if(lastTarget == null || this.dragon.getDistance(lastTarget.x, lastTarget.y, lastTarget.z) <= 0.5D) {
				currentIterations++;
				
				//DONE: Calculate position and move to it
				Vec3d newPos = new Vec3d(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
				
				//DONE: Redo this
				// Get the current movement vector of the entity (rotation on x/z plane)
				// Rotate this vector by the anglePerIteration
				// Add this vector to the entities current pos and we have the new target
				Vec3d vec = new Vec3d(0,0,3D);
				//TODO: Adjust this vector to the direction the dragon is looking in?
				newPos = newPos.add(VectorUtil.rotateVectorAroundY(vec, flyingUp ? anglePerIteration * (currentIterations +1) : 360.0D - (currentIterations +1)));
				newPos.addVector(0,flyingUp ? deltaYPerIteration : -deltaYPerIteration, 0);
				
				lastTarget = newPos;
			}
			this.dragon.getNavigator().tryMoveToXYZ(lastTarget.x, lastTarget.y, lastTarget.z, 1.1F);
			
		} else {
			resetTask();
		}
	}
	
	@Override
	public void resetTask() {
		this.currentIterations = 0;
	}

}
