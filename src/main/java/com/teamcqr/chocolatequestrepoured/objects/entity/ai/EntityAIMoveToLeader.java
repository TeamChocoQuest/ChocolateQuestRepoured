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
			return this.entity.getDistance(leader) > 8.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.hasLeader()) {
			EntityLivingBase leader = this.entity.getLeader();

			if (this.entity.getDistance(leader) > 4.0D) {
				return this.entity.hasPath();
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		EntityLivingBase leader = this.entity.getLeader();
		this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
	}

	@Override
	public void updateTask() {
		if (this.entity.hasPath()) {
			EntityLivingBase leader = this.entity.getLeader();
			PathPoint target = this.entity.getNavigator().getPath().getFinalPathPoint();

			if (leader.getDistance(target.x, target.y, target.z) > 4.0D) {
				this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
			}
		}
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

}
