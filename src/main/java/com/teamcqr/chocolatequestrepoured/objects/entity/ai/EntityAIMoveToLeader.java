package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathPoint;

public class EntityAIMoveToLeader extends AbstractCQREntityAI {

	public EntityAIMoveToLeader(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasLeader()) {
			EntityLivingBase leader = this.entity.getLeader();
			double x = leader.posX - this.entity.posX;
			double y = leader.posY - this.entity.posY;
			double z = leader.posZ - this.entity.posZ;
			double distance = Math.sqrt(x * x + y * y + z * z);

			return distance > 8.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.hasLeader()) {
			EntityLivingBase leader = this.entity.getLeader();
			double x = leader.posX - this.entity.posX;
			double y = leader.posY - this.entity.posY;
			double z = leader.posZ - this.entity.posZ;
			double distance = Math.sqrt(x * x + y * y + z * z);

			if (distance > 4.0D) {
				return !this.entity.getNavigator().noPath();
			}

			this.entity.getNavigator().clearPath();
		}
		return false;
	}

	@Override
	public void startExecuting() {
		EntityLivingBase leader = this.entity.getLeader();
		this.entity.getNavigator().tryMoveToXYZ(leader.posX, leader.posY, leader.posZ, 1.0D);
	}

	@Override
	public void updateTask() {
		if (!this.entity.getNavigator().noPath()) {
			PathPoint target = this.entity.getNavigator().getPath().getFinalPathPoint();
			EntityLivingBase leader = this.entity.getLeader();
			double x = leader.posX - target.x;
			double y = leader.posY - target.y;
			double z = leader.posZ - target.z;
			double distance = Math.sqrt(x * x + y * y + z * z);
			
			if (distance > 4.0D) {
				this.entity.getNavigator().tryMoveToXYZ(leader.posX, leader.posY, leader.posZ, 1.0D);
			}
		}
	}

}
