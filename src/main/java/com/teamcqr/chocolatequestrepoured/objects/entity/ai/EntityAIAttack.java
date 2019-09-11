package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI {

	protected Path path;
	protected int attackTick;

	public EntityAIAttack(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (this.isSuitableTarget(attackTarget) && this.entity.getEntitySenses().canSee(attackTarget) && this.canMoveToEntity(attackTarget)) {
			return true;
		}
		this.entity.setAttackTarget(null);
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if ((this.isSuitableTarget(attackTarget) || (attackTarget != null && attackTarget != this.entity && !this.entity.getEntitySenses().canSee(attackTarget))) && this.entity.hasPath()) {
			return true;
		}
		this.entity.setAttackTarget(null);
		this.resetTask();
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().setPath(this.path, 1.0D);
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		this.attackTick--;
		
		if (this.entity.canEntityBeSeen(attackTarget)) {
			PathPoint finalPathPoint = this.path.getFinalPathPoint();
			if (attackTarget.getDistance(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) > 1.0D) {
				this.canMoveToEntity(attackTarget);
				this.entity.getNavigator().setPath(this.path, 1.0D);
			}
		}
		
		double distance = this.entity.getDistanceSq(attackTarget);
		this.checkAndPerformAttack(this.entity.getAttackTarget(), distance);
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.attackTick = 0;
	}

	protected boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == null) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		if (!possibleTarget.isEntityAlive()) {
			return false;
		}
		// TODO Replace with isEntityAlly when factions are finished
		if (this.entity.getFaction() == EFaction.getFactionOfEntity(possibleTarget)) {
			return false;
		}
		if (possibleTarget instanceof EntityPlayer && (((EntityPlayer) possibleTarget).isCreative() || ((EntityPlayer) possibleTarget).isSpectator())) {
			return false;
		}
		return true;
	}
	
	protected boolean canMoveToEntity(EntityLivingBase target) {
		this.path = this.entity.getNavigator().getPathToEntityLiving(target);
		return this.path != null;
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget, double distance) {
		double d0 = this.getAttackReachSqr(attackTarget);

		if (distance <= d0 && this.attackTick <= 0) {
			this.attackTick = 20;
			this.entity.swingArm(EnumHand.MAIN_HAND);
			this.entity.attackEntityAsMob(attackTarget);
		}
	}

	protected double getAttackReachSqr(EntityLivingBase attackTarget) {
		return (double) (this.entity.width * 2.0F * this.entity.width * 2.0F + attackTarget.width);
	}

}
