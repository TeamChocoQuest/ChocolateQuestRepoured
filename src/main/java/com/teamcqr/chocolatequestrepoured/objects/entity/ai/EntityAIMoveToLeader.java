package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathPoint;

public class EntityAIMoveToLeader extends EntityAIBase {

	protected final EntityLiving entity;

	public EntityAIMoveToLeader(EntityLiving entity) {
		this.entity = entity;
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity instanceof ICQREntity) {
			EntityLivingBase leader = ((ICQREntity) this.entity).getLeader();
			if (leader != null && !leader.isDead) {
				double x = leader.posX - this.entity.posX;
				double y = leader.posY - this.entity.posY;
				double z = leader.posZ - this.entity.posZ;
				double distance = Math.sqrt(x * x + y * y + z * z);

				return distance > 8.0D;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase leader = ((ICQREntity) this.entity).getLeader();
		if (leader != null && !leader.isDead) {
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
		EntityLivingBase leader = ((ICQREntity) this.entity).getLeader();
		this.entity.getNavigator().tryMoveToXYZ(leader.posX, leader.posY, leader.posZ, 1.0D);
	}

	@Override
	public void updateTask() {
		if (!this.entity.getNavigator().noPath()) {
			PathPoint target = this.entity.getNavigator().getPath().getFinalPathPoint();
			EntityLivingBase leader = ((ICQREntity) this.entity).getLeader();
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
