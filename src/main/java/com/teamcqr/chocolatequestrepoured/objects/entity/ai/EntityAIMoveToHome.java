package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.math.BlockPos;

public class EntityAIMoveToHome extends AbstractCQREntityAI {

	public EntityAIMoveToHome(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasHomePositionCQR() && !this.entity.hasLeader()) {
			BlockPos pos = this.entity.getHomePositionCQR();
			return this.entity.getDistance((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()) > 8.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		BlockPos pos = this.entity.getHomePositionCQR();
		this.entity.getNavigator().setPath(this.entity.getNavigator().getPathToPos(pos), 1.0D);
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

}
