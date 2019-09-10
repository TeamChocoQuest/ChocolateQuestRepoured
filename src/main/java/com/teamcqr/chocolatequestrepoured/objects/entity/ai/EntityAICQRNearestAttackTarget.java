package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.AxisAlignedBB;

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
				if (!TargetUtil.PREDICATE.apply(input)) {
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
		if (this.entity.world.rand.nextInt(5) == 0) {
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D, 8.0D, 32.0D);
			List<EntityLivingBase> possibleTargets = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.predicate);
			possibleTargets.sort(this.sorter);
			for (EntityLivingBase possibleTarget : possibleTargets) {
				if (this.isSuitableTarget(possibleTarget)) {
					this.attackTarget = possibleTarget;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.setAttackTarget(attackTarget);
	}

	private boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		// TODO Replace with isEntityAlly when factions are finished
		if (this.entity.getFaction() == EFaction.getFactionOfEntity(possibleTarget)) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}
		if (!this.canReachTarget(possibleTarget)) {
			return false;
		}
		return true;
	}

	private boolean canReachTarget(EntityLivingBase possibleTarget) {
		Path path = this.entity.getNavigator().getPathToEntityLiving(possibleTarget);
		return path != null;
	}

}
