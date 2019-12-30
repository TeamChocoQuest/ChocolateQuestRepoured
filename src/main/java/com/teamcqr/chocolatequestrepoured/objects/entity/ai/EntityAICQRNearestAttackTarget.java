package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class EntityAICQRNearestAttackTarget extends EntityAIBase {

	protected final AbstractEntityCQR entity;
	protected final Predicate<EntityLivingBase> predicate;
	protected final TargetUtil.Sorter sorter;
	protected EntityLivingBase attackTarget;

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
		this.entity = entity;
		this.predicate = new Predicate<EntityLivingBase>() {
			@Override
			public boolean apply(EntityLivingBase input) {
				if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
					return false;
				}
				if (!EntitySelectors.IS_ALIVE.apply(input)) {
					return false;
				}
				if (!EntityAICQRNearestAttackTarget.this.isSuitableTarget(input)) {
					return false;
				}
				return true;
			}
		};
		this.sorter = new TargetUtil.Sorter(entity);
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (this.entity.ticksExisted % 4 == 0 && this.entity.getAttackTarget() == null) {
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D);
			List<EntityLivingBase> possibleTargets = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.predicate);
			if (!possibleTargets.isEmpty()) {
				this.attackTarget = TargetUtil.getNearestEntity(this.entity, possibleTargets);
				return true;
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.setAttackTarget(this.attackTarget);
	}

	private boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		if (this.entity.getHeldItemMainhand().getItem() instanceof ItemStaffHealing) {
			if (!this.entity.getFaction().isAlly(possibleTarget)) {
				return false;
			}
			if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
				return false;
			}
			if (!this.entity.isInSightRange(possibleTarget)) {
				return false;
			}
			return possibleTarget.getHealth() < possibleTarget.getMaxHealth();
		}
		if (!this.entity.getFaction().isEnemy(possibleTarget)) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		if (this.entity.isEntityInFieldOfView(possibleTarget)) {
			return this.entity.isInSightRange(possibleTarget);
		}
		return !possibleTarget.isSneaking() && this.entity.getDistance(possibleTarget) < 12.0D;
	}

}
