package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;

public class EntityAIFollowPath extends AbstractCQREntityAI {

	private boolean isReversingPath = false;
	
	public EntityAIFollowPath(AbstractEntityCQR entity) {
		super(entity);
		setMutexBits(8);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity.getGuardPathPoints().length > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		BlockPos pos = entity.getGuardPathPoints()[entity.getCurrentGuardPathTargetPoint()].add(entity.getHomePositionCQR());
		float width = entity.width;
		if(entity.getRidingEntity() != null) {
			width = entity.getRidingEntity().width;
		}
		if(width <= 0) {
			width = 1;
		}
		width *= width;
		if(entity.getDistanceSq(pos) <= width +1) {
			//Cycle to next position
			int newIndex = isReversingPath ? entity.getCurrentGuardPathTargetPoint() -1 : entity.getCurrentGuardPathTargetPoint() +1;
			if(newIndex == entity.getGuardPathPoints().length) {
				if(entity.isGuardPathLoop()) {
					newIndex = -1;
				} else {
					isReversingPath = !isReversingPath;
				}
			}
			if(newIndex < 0 && isReversingPath) {
				newIndex = 0;
				isReversingPath = !isReversingPath;
			}
			if(isReversingPath) {
				newIndex--;
			} else {
				newIndex++;
			}
			entity.setCurrentGuardPathTargetPoint(newIndex);
			pos = entity.getGuardPathPoints()[entity.getCurrentGuardPathTargetPoint()].add(entity.getHomePositionCQR());
		}
		entity.move(MoverType.SELF, pos.getX(), pos.getY(), pos.getZ());
		super.updateTask();
	}

}
