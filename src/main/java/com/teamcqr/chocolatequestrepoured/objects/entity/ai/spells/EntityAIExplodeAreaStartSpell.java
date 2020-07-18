package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;

public class EntityAIExplodeAreaStartSpell extends AbstractEntityAISpell<EntityCQRBoarmage> implements IEntityAISpellAnimatedVanilla {

	public EntityAIExplodeAreaStartSpell(EntityCQRBoarmage entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, false, true, false);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && !this.entity.isExecutingExplodeAreaAttack();
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		this.entity.startExplodeAreaAttack();
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public float getRed() {
		return 1;
	}

	@Override
	public float getGreen() {
		return 1;
	}

	@Override
	public float getBlue() {
		return 0;
	}

}
