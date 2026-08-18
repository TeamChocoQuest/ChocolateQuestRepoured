package com.example.chocolatequest.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.Difficulty;
import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.world.entity.EntitySelector;

import java.util.List;

public class EntityAICQRNearestAttackTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

    private static final int MIN_SEARCH_INTERVAL = 20;
    private static final int SEARCH_INTERVAL_VARIANCE = 11;
    private int nextSearchTick = -1;

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
		super(entity);
	}

	protected void wrapperSetAttackTarget(LivingEntity target) {
		this.entity.setTarget(target);
	}

	protected LivingEntity wrapperGetAttackTarget() {
		return this.entity.getTarget();
	}

	@Override
	public boolean canUse() {
		if (this.entity.level().getDifficulty() == Difficulty.PEACEFUL) {
			this.wrapperSetAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.wrapperGetAttackTarget())) {
			return false;
		}
		this.wrapperSetAttackTarget(null);

		if (this.nextSearchTick < 0) {
			// Stagger newly spawned groups so they do not all perform a 32-block scan in one tick.
			this.nextSearchTick = this.entity.tickCount + Math.floorMod(this.entity.getId(), MIN_SEARCH_INTERVAL);
		}
		if (this.entity.tickCount < this.nextSearchTick) {
			return false;
		}

		this.nextSearchTick = this.entity.tickCount + MIN_SEARCH_INTERVAL + this.random.nextInt(SEARCH_INTERVAL_VARIANCE);
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {

		if (this.entity.hasLeader()) {
			LivingEntity leader = this.entity.getLeader();
			LivingEntity leaderTarget = leader instanceof net.minecraft.world.entity.Mob ? ((net.minecraft.world.entity.Mob)leader).getTarget() : null;
			if (leaderTarget != null && this.isSuitableTargetEnemy(leaderTarget)) {
				this.wrapperSetAttackTarget(leaderTarget);
				return;
			}
		}
		AABB aabb = this.entity.getBoundingBox().inflate(32.0D);
		List<LivingEntity> possibleTargets = this.entity.level().getEntitiesOfClass(LivingEntity.class, aabb);
		boolean canTargetAlly = this.canTargetAlly();
		LivingEntity nearestAlly = null;
		LivingEntity nearestEnemy = null;
		double nearestAllyDistance = Double.MAX_VALUE;
		double nearestEnemyDistance = Double.MAX_VALUE;

		for (LivingEntity possibleTarget : possibleTargets) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)
					|| !EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(possibleTarget)
					|| possibleTarget == this.entity) {
				continue;
			}

			double distance = this.entity.distanceToSqr(possibleTarget);
			if (canTargetAlly && this.isSuitableTargetAlly(possibleTarget)) {
				if (distance < nearestAllyDistance) {
					nearestAllyDistance = distance;
					nearestAlly = possibleTarget;
				}
			} else if (this.isSuitableTargetEnemy(possibleTarget) && distance < nearestEnemyDistance) {
				nearestEnemyDistance = distance;
				nearestEnemy = possibleTarget;
			}
		}

		this.wrapperSetAttackTarget(nearestAlly != null ? nearestAlly : nearestEnemy);
	}

	protected boolean canTargetAlly() {
		Item item = this.entity.getMainHandItem().getItem();
		return item instanceof com.example.chocolatequest.item.staff.HealStaffItem;
	}

	protected boolean isSuitableTargetAlly(LivingEntity possibleTarget) {
		EDefaultFaction faction = this.entity.getDefaultFaction();
		if (faction == null) {
			return false;
		}
		if (!TargetUtil.isAllyCheckingLeaders(this.entity, possibleTarget)) {
			return false;
		}
		if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
			return false;
		}
		if (!this.entity.isInSightRange(possibleTarget)) {
			return false;
		}
		return this.entity.getSensing().hasLineOfSight(possibleTarget);
	}

	protected boolean isSuitableTargetEnemy(LivingEntity possibleTarget) {
		if (!TargetUtil.isEnemyCheckingLeaders(this.entity, possibleTarget)) {
			return false;
		}

		double distanceSqr = this.entity.distanceToSqr(possibleTarget);
		boolean detectable = this.entity.isInAttackReach(possibleTarget);
		if (!detectable) {
			net.minecraft.world.phys.Vec3 viewVector = this.entity.getViewVector(1.0F).normalize();
			net.minecraft.world.phys.Vec3 toTarget = possibleTarget.position().subtract(this.entity.position()).normalize();
			boolean inFOV = viewVector.dot(toTarget) > 0.0D;
			detectable = inFOV ? this.entity.isInSightRange(possibleTarget)
					: !possibleTarget.isCrouching() && distanceSqr < 12.0D * 12.0D;
		}

		return detectable && this.entity.getSensing().hasLineOfSight(possibleTarget);
	}

	protected boolean isStillSuitableTarget(LivingEntity possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		if (this.entity.distanceToSqr(possibleTarget) > 64.0D * 64.0D) {
			return false;
		}
		if (TargetUtil.isAllyCheckingLeaders(this.entity, possibleTarget)) {
			if (!this.canTargetAlly()) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
		} else if (this.canTargetAlly()) {
			AABB aabb = this.entity.getBoundingBox().inflate(32.0D);
			List<LivingEntity> possibleTargets = this.entity.level().getEntitiesOfClass(LivingEntity.class, aabb);
			for (LivingEntity possibleTargetAlly : possibleTargets) {
				if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTargetAlly)) {
					continue;
				}
				if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(possibleTargetAlly)) {
					continue;
				}
				if (possibleTargetAlly == this.entity) {
					continue;
				}
				if (this.isSuitableTargetAlly(possibleTargetAlly)) {
					return false;
				}
			}
		}
		return true;
	}

}
