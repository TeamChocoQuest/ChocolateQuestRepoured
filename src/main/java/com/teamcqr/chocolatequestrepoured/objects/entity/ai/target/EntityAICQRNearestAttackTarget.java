package com.teamcqr.chocolatequestrepoured.objects.entity.ai.target;

import java.util.LinkedList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.IFakeWeapon;
import com.teamcqr.chocolatequestrepoured.objects.items.ISupportWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class EntityAICQRNearestAttackTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			this.entity.setAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.entity.getAttackTarget())) {
			return false;
		}
		this.entity.setAttackTarget(null);
		if (!this.entity.hasFaction()) {
			return false;
		}
		return this.random.nextInt(3) == 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public void startExecuting() {
		AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D);
		List<EntityLivingBase> possibleTargets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		List<EntityLivingBase> possibleTargetsAlly = new LinkedList<>();
		List<EntityLivingBase> possibleTargetsEnemy = new LinkedList<>();
		this.fillLists(possibleTargets, possibleTargetsAlly, possibleTargetsEnemy);
		if (!possibleTargetsAlly.isEmpty()) {
			this.entity.setAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsAlly));
		} else if (!possibleTargetsEnemy.isEmpty()) {
			this.entity.setAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsEnemy));
		}
	}

	private void fillLists(List<EntityLivingBase> list, List<EntityLivingBase> allies, List<EntityLivingBase> enemies) {
		for (EntityLivingBase possibleTarget : list) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
				continue;
			}
			if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
				continue;
			}
			if (possibleTarget == this.entity) {
				continue;
			}
			if (this.canTargetAlly() && this.isSuitableTargetAlly(possibleTarget)) {
				allies.add(possibleTarget);
			} else if (this.isSuitableTargetEnemy(possibleTarget)) {
				enemies.add(possibleTarget);
			}
		}
	}

	private boolean canTargetAlly() {
		Item item = this.entity.getHeldItemMainhand().getItem();
		return item instanceof ISupportWeapon<?> || item instanceof IFakeWeapon<?>;
	}

	private boolean isSuitableTargetAlly(EntityLivingBase possibleTarget) {
		CQRFaction faction = this.entity.getFaction();
		if (faction == null) {
			return false;
		}
		if (!faction.isAlly(possibleTarget) && possibleTarget != this.entity.getLeader()) {
			return false;
		}
		if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
			return false;
		}
		if (!this.entity.isInSightRange(possibleTarget)) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(possibleTarget);
	}

	private boolean isSuitableTargetEnemy(EntityLivingBase possibleTarget) {
		CQRFaction faction = this.entity.getFaction();
		if (faction == null) {
			return false;
		}
		if (!faction.isEnemy(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity.getLeader()) {
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

	private boolean isStillSuitableTarget(EntityLivingBase possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		if (this.entity.getDistance(possibleTarget) > 64.0D) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
		if (faction == null) {
			return false;
		}
		if (faction.isAlly(possibleTarget) || possibleTarget == this.entity.getLeader()) {
			if (!this.canTargetAlly()) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
		}
		return true;
	}

}
