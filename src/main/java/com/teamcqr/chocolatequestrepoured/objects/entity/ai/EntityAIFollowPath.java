package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.math.Vec3d;

public class EntityAIFollowPath extends AbstractCQREntityAI {

	private boolean isReversingPath = false;
	
	public EntityAIFollowPath(AbstractEntityCQR entity) {
		super(entity);
		setMutexBits(8);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity.getGuardPathPoints().length > 0 && this.entity.getHomePositionCQR() != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		Vec3d pos = new Vec3d(entity.getGuardPathPoints()[entity.getCurrentGuardPathTargetPoint()]).add(new Vec3d(entity.getHomePositionCQR())).addVector(0.5,0,0.5);
		float width = entity.width;
		if(entity.getRidingEntity() != null) {
			width = entity.getRidingEntity().width;
		}
		if(width <= 0) {
			width = 1;
		}
		width *= width;
		if(entity.getDistance(pos.x, pos.y, pos.z) <= (width +1) /2) {
			//Cycle to next position
			int newIndex = isReversingPath ? entity.getCurrentGuardPathTargetPoint() -1 : entity.getCurrentGuardPathTargetPoint() +1;
			if(newIndex == entity.getGuardPathPoints().length) {
				if(entity.isGuardPathLoop()) {
					newIndex = 0;
				} else {
					isReversingPath = true;
					newIndex--;
				}
			}
			if(newIndex == 0 && !entity.isGuardPathLoop()) {
				isReversingPath = false;
			}
			entity.setCurrentGuardPathTargetPoint(newIndex);
			pos = new Vec3d(entity.getGuardPathPoints()[entity.getCurrentGuardPathTargetPoint()]).add(new Vec3d(entity.getHomePositionCQR())).addVector(0.5,0,0.5);
		}
		entity.getMoveHelper().setMoveTo(pos.x, pos.y, pos.z, 1.0);
		super.updateTask();
	}

}
