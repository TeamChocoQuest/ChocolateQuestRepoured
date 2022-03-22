package team.cqr.cqrepoured.entity.ai.target;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Difficulty;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;

import java.util.ArrayList;
import java.util.List;

public class EntityAICQRNearestAttackTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

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
		if (this.world.getDifficulty() == Difficulty.PEACEFUL) {
			this.wrapperSetAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.wrapperGetAttackTarget())) {
			return false;
		}
		this.wrapperSetAttackTarget(null);
		return this.random.nextInt(3) == 0;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		AxisAlignedBB aabb = this.entity.getBoundingBox().inflate(32.0D);
		List<LivingEntity> possibleTargets = this.world.getEntitiesOfClass(LivingEntity.class, aabb);
		List<LivingEntity> possibleTargetsAlly = new ArrayList<>();
		List<LivingEntity> possibleTargetsEnemy = new ArrayList<>();
		this.fillLists(possibleTargets, possibleTargetsAlly, possibleTargetsEnemy);
		if (!possibleTargetsAlly.isEmpty()) {
			this.wrapperSetAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsAlly));
		} else if (!possibleTargetsEnemy.isEmpty()) {
			this.wrapperSetAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsEnemy));
		}
	}

	protected void fillLists(List<LivingEntity> list, List<LivingEntity> allies, List<LivingEntity> enemies) {
		boolean canTargetAlly = this.canTargetAlly();
		for (LivingEntity possibleTarget : list) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
				continue;
			}
			if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(possibleTarget)) {
				continue;
			}
			if (possibleTarget == this.entity) {
				continue;
			}
			if (canTargetAlly && this.isSuitableTargetAlly(possibleTarget)) {
				allies.add(possibleTarget);
			} else if (this.isSuitableTargetEnemy(possibleTarget)) {
				enemies.add(possibleTarget);
			}
		}
	}

	protected boolean canTargetAlly() {
		Item item = this.entity.getMainHandItem().getItem();
		return item instanceof ISupportWeapon<?> || item instanceof IFakeWeapon<?>;
	}

	protected boolean isSuitableTargetAlly(LivingEntity possibleTarget) {
		Faction faction = this.entity.getFaction();
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
		return this.entity.getSensing().canSee(possibleTarget);
	}

	protected boolean isSuitableTargetEnemy(LivingEntity possibleTarget) {
		if (!TargetUtil.isEnemyCheckingLeaders(this.entity, possibleTarget)) {
			return false;
		}
		if (!this.entity.getSensing().canSee(possibleTarget)) {
			return false;
		}
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		if (this.entity.isEntityInFieldOfView(possibleTarget)) {
			return this.entity.isInSightRange(possibleTarget);
		}
		return !possibleTarget.isCrouching() && this.entity.distanceToSqr(possibleTarget) < 12.0D * 12.0D;
	}

	protected boolean isStillSuitableTarget(LivingEntity possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(possibleTarget)) {
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
			AxisAlignedBB aabb = this.entity.getBoundingBox().inflate(32.0D);
			List<LivingEntity> possibleTargets = this.world.getEntitiesOfClass(LivingEntity.class, aabb);
			for (LivingEntity possibleTargetAlly : possibleTargets) {
				if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTargetAlly)) {
					continue;
				}
				if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(possibleTargetAlly)) {
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
