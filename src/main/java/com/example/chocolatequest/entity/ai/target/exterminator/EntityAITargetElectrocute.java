package com.example.chocolatequest.entity.ai.target.exterminator;

import net.minecraft.world.entity.LivingEntity;

import com.example.chocolatequest.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityAITargetElectrocute extends EntityAICQRNearestAttackTarget {

	private Supplier<LivingEntity> funcGetElectrocuteTarget;
	private Consumer<LivingEntity> funcSetElectrocuteTarget;

	public EntityAITargetElectrocute(AbstractEntityCQR entity, Supplier<LivingEntity> funcGetElectrocuteTarget, Consumer<LivingEntity> funcSetElectrocuteTarget) {
		super(entity);

		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcSetElectrocuteTarget = funcSetElectrocuteTarget;
	}

	@Override
	protected LivingEntity wrapperGetAttackTarget() {
		return this.funcGetElectrocuteTarget.get();
	}

	@Override
	protected void wrapperSetAttackTarget(LivingEntity target) {
		this.funcSetElectrocuteTarget.accept(target);
	}

	@Override
	protected boolean canTargetAlly() {
		return false;
	}

	@Override
	protected boolean isSuitableTargetAlly(LivingEntity possibleTarget) {
		return false;
	}

	@Override
	protected boolean isStillSuitableTarget(LivingEntity possibleTarget) {
		return possibleTarget != null && this.entity.distanceTo(possibleTarget) <= (this.entity.getBbWidth() + 4 * 4) && super.isStillSuitableTarget(possibleTarget) && this.entity.hasLineOfSight(possibleTarget);
	}

	@Override
	protected boolean isSuitableTargetEnemy(LivingEntity possibleTarget) {
		return possibleTarget != null && this.entity.distanceTo(possibleTarget) <= (this.entity.getBbWidth() + 4 * 2) && super.isSuitableTargetEnemy(possibleTarget) && TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(possibleTarget)
				&& !TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(possibleTarget);
	}

}

