package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.math.Vec3d;

public class EntityAIFollowAttackTarget extends AbstractCQREntityAI {

	public EntityAIFollowAttackTarget(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getLastPosAttackTarget() != null && this.entity.getLastTimeSeenAttackTarget() + 20 >= this.entity.ticksExisted;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		Vec3d vec = this.entity.getLastPosAttackTarget();
		this.entity.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1.0D);
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

}
