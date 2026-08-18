package com.example.chocolatequest.entity.ai.boss.exterminator;

import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;

import java.util.EnumSet;

public class BossAIExterminatorStun extends AbstractCQREntityAI<EntityCQRExterminator> {

	public BossAIExterminatorStun(EntityCQRExterminator entity) {
		super(entity);
		this.setFlags(EnumSet.allOf(Flag.class));
	}

	@Override
	public boolean canUse() {
		return this.entity.isStunned() || TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this.entity);
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}

