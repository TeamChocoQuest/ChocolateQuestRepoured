package com.teamcqr.chocolatequestrepoured.objects.entity.ai.target;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;

public class EntityAIHurtByTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected EntityLivingBase attackTarget;
	protected int prevRevengeTimer;

	public EntityAIHurtByTarget(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (this.entity.getRevengeTimer() == this.prevRevengeTimer) {
			return false;
		}
		EntityLivingBase revengeTarget = this.entity.getRevengeTarget();
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(revengeTarget)) {
			return false;
		}
		if (!revengeTarget.isEntityAlive()) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
		if (faction != null && faction.isAlly(revengeTarget)) {
			return false;
		}
		if (revengeTarget == this.entity.getLeader()) {
			return false;
		}
		if (!this.entity.isInSightRange(revengeTarget)) {
			return false;
		}
		this.attackTarget = revengeTarget;
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public void startExecuting() {
		this.prevRevengeTimer = this.entity.getRevengeTimer();
		this.trySetAttackTarget(this.entity);
		CQRFaction faction = this.entity.getFaction();
		if (faction != null && faction.isEnemy(this.attackTarget)) {
			this.callForHelp();
		}
	}

	protected void callForHelp() {
		double radius = CQRConfig.mobs.alertRadius;
		Vec3d eyeVec = this.entity.getPositionEyes(1.0F);
		Vec3d vec1 = eyeVec.subtract(radius, radius * 0.5D, radius);
		Vec3d vec2 = eyeVec.addVector(radius, radius * 0.5D, radius);
		AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
		List<EntityLiving> allies = this.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this::isSuitableAlly);
		for (EntityLiving ally : allies) {
			this.trySetAttackTarget(ally);
		}
	}

	protected boolean isSuitableAlly(EntityLiving possibleAlly) {
		if (!EntitySelectors.IS_ALIVE.apply(possibleAlly)) {
			return false;
		}
		if (possibleAlly == this.entity) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
		if (faction == null || !faction.isAlly(possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigator().getPathToEntityLiving(this.entity);
		return path != null && path.getCurrentPathLength() <= 20;
	}

	protected boolean trySetAttackTarget(EntityLiving entityLiving) {
		if (entityLiving.getHeldItemMainhand().getItem() instanceof ItemStaffHealing) {
			return false;
		}
		if (entityLiving instanceof AbstractEntityCQR) {
			AbstractEntityCQR entityCQR = (AbstractEntityCQR) entityLiving;
			CQRFaction faction = entityCQR.getFaction();
			if (faction != null && faction.isAlly(this.attackTarget)) {
				return false;
			}
		}
		EntityLivingBase oldAttackTarget = entityLiving.getAttackTarget();
		if (oldAttackTarget != null && entityLiving.getEntitySenses().canSee(oldAttackTarget) && entityLiving.getDistance(oldAttackTarget) < entityLiving.getDistance(this.attackTarget)) {
			return false;
		}
		entityLiving.setAttackTarget(this.attackTarget);
		return true;
	}

}
