package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class EntityAIHurtByTarget extends AbstractCQREntityAI {

	protected final Predicate<EntityLiving> predicateAlly;
	protected EntityLivingBase attackTarget;
	protected int prevRevengeTimer;

	public EntityAIHurtByTarget(AbstractEntityCQR entity) {
		super(entity);
		this.predicateAlly = new Predicate<EntityLiving>() {
			@Override
			public boolean apply(EntityLiving input) {
				if (input == null) {
					return false;
				}
				if (!EntitySelectors.IS_ALIVE.apply(input)) {
					return false;
				}
				if (!EntityAIHurtByTarget.this.isSuitableAlly(input)) {
					return false;
				}
				return true;
			}
		};
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (this.entity.getHeldItemMainhand().getItem() instanceof ItemStaffHealing) {
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
		if (!this.entity.getFaction().isEnemy(revengeTarget)) {
			
		}
		if (!this.entity.isInSightRange(revengeTarget)) {
			return false;
		}
		EntityLivingBase target = this.entity.getAttackTarget();
		if (target != null && this.entity.getEntitySenses().canSee(target) && this.entity.getDistance(target) < this.entity.getDistance(revengeTarget) + 4.0D) {
			return false;
		}
		this.attackTarget = revengeTarget;
		return true;
	}

	@Override
	public void startExecuting() {
		this.entity.setAttackTarget(this.attackTarget);
		this.prevRevengeTimer = this.entity.getRevengeTimer();
		this.callForHelp();
	}

	protected void callForHelp() {
		AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(16.0D);
		List<EntityLiving> allies = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly);
		int size = allies.size();
		for (int i = 0; i < size; i++) {
			EntityLiving ally = allies.get(i);
			if (ally.getAttackTarget() == null) {
				allies.get(i).setAttackTarget(this.attackTarget);
			}
		}
	}

	protected boolean isSuitableAlly(EntityLiving possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!this.entity.getFaction().isAlly(possibleAlly)) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(possibleAlly);
	}

}
