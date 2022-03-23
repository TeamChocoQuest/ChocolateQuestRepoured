package team.cqr.cqrepoured.entity.ai.target;

import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Difficulty;
import team.cqr.cqrepoured.entity.ICirclingEntity;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.init.CQRItems;

import java.util.List;

public class EntityAINearestAttackTargetAtHomeArea<T extends AbstractEntityCQR & ICirclingEntity> extends AbstractCQREntityAI<T> {

	protected final Predicate<LivingEntity> predicate = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(input)) {
			return false;
		}
		return EntityAINearestAttackTargetAtHomeArea.this.isSuitableTarget(input);
	};

	public EntityAINearestAttackTargetAtHomeArea(T entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.entity.level.getDifficulty() == Difficulty.PEACEFUL) {
			this.entity.setTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.entity.getTarget())) {
			return false;
		}
		this.entity.setTarget(null);
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	private static final Vector3i SIZE_VECTOR = new Vector3i(32, 32, 32);

	@Override
	public void start() {
		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getCirclingCenter().offset(SIZE_VECTOR), this.entity.getCirclingCenter().subtract(SIZE_VECTOR));
		List<LivingEntity> possibleTargets = this.entity.level.getEntitiesOfClass(LivingEntity.class, aabb, this.predicate);
		if (!possibleTargets.isEmpty()) {
			this.entity.setTarget(TargetUtil.getNearestEntity(this.entity, possibleTargets));
		}
	}

	private boolean isSuitableTarget(LivingEntity possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		Faction faction = this.entity.getFaction();
		if (this.entity.getMainHandItem().getItem() == CQRItems.STAFF_HEALING.get()) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*
			 * if (!this.entity.isInSightRange(possibleTarget)) { return false; }
			 */
			// return this.entity.getEntitySenses().canSee(possibleTarget);
			return this.isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*
		 * if (!this.entity.getEntitySenses().canSee(possibleTarget)) { return false; }
		 */
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		/*
		 * if (this.entity.isEntityInFieldOfView(possibleTarget)) { return this.entity.isInSightRange(possibleTarget); }
		 */
		return !possibleTarget.isCrouching() && this.entity.distanceTo(possibleTarget) < 32.0D;
	}

	private boolean isStillSuitableTarget(LivingEntity possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		Faction faction = this.entity.getFaction();
		if (this.entity.getMainHandItem().getItem() == CQRItems.STAFF_HEALING.get()) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*
			 * if (!this.entity.isInSightRange(possibleTarget)) { return false; }
			 */
			// return this.entity.getEntitySenses().canSee(possibleTarget);
			return this.isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*
		 * if (!this.entity.isInSightRange(possibleTarget)) { return false; } return
		 * this.entity.getEntitySenses().canSee(possibleTarget);
		 */
		return this.isInHomeZone(possibleTarget);
	}

	private boolean isInHomeZone(LivingEntity possibleTarget) {
		double distance = possibleTarget.position().distanceTo(new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ()));
		return Math.abs(distance) <= 48 + 8 * (this.world.getDifficulty().ordinal());
	}

}
