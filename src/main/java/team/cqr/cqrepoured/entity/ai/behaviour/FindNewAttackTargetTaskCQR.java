package team.cqr.cqrepoured.entity.ai.behaviour;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.FindNewAttackTargetTask;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.ai.sensor.FactionBasedAttackTargetSensor;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class FindNewAttackTargetTaskCQR<E extends AbstractEntityCQR> extends FindNewAttackTargetTask<E> {

	public FindNewAttackTargetTaskCQR(Predicate<LivingEntity> predicate) {
		super(predicate);
		this.stopAttackingWhen = predicate.or(this::isStillSuitableTarget);
	}

	public FindNewAttackTargetTaskCQR() {
		super();
		this.stopAttackingWhen = this::isStillSuitableTarget;
	}

	protected E entity = null;
	
	@Override
	protected void start(ServerWorld pLevel, E pEntity, long pGameTime) {
		this.entity = pEntity;
		super.start(pLevel, pEntity, pGameTime);
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
			if (!FactionBasedAttackTargetSensor.canTargetAlly(this.entity)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
		} else if (FactionBasedAttackTargetSensor.canTargetAlly(this.entity)) {
			AxisAlignedBB aabb = this.entity.getBoundingBox().inflate(32.0D);
			List<LivingEntity> possibleTargets = this.entity.level.getEntitiesOfClass(LivingEntity.class, aabb);
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
				if (FactionBasedAttackTargetSensor.isSuitableTargetAlly(possibleTargetAlly, this.entity)) {
					return false;
				}
			}
		}
		return true;
	}

}
