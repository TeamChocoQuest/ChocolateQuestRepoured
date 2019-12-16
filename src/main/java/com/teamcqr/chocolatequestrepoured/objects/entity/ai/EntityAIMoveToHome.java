package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.math.BlockPos;

public class EntityAIMoveToHome extends AbstractCQREntityAI {

	public EntityAIMoveToHome(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity.getHomePositionCQR() == null) {
			return false;
		}
		if (this.entity.hasHomePositionCQR() && !this.entity.hasLeader()) {
			BlockPos pos = this.entity.getHomePositionCQR();
			double x = (double) pos.getX() + 0.5D - this.entity.posX;
			double y = (double) pos.getY() - this.entity.posY;
			double z = (double) pos.getZ() + 0.5D - this.entity.posZ;
			double distance = Math.sqrt(x * x + y * y + z * z);

			return distance > 8.0D;
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
		this.entity.getNavigator().tryMoveToXYZ(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1.0D);
	}

}
