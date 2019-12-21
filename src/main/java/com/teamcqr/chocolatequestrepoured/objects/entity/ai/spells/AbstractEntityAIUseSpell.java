package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.SoundEvent;

/*
 * 20.12.2019
 * Made by: DerToaster98
 * Comment: This code is adapted minecraft vanilla code, so it is made by Mojang
 */
public abstract class AbstractEntityAIUseSpell extends AbstractCQREntityAI {

	public AbstractEntityAIUseSpell(AbstractEntityCQR entity) {
		super(entity);
	}

	protected int spellWarmup;
	protected int spellCooldown;

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.entity.getAttackTarget() == null) {
			this.entity.setSpellCasting(false);
			this.entity.setSpellType(ESpellType.NONE);
			return false;
		} else if (this.entity.isSpellcasting()) {
			return false;
		} else if (!this.entity.getActiveSpell().equals(ESpellType.NONE)) {
			return false;
		} else {
			this.entity.setSpellCasting(false);
			this.entity.setSpellType(ESpellType.NONE);
			return this.entity.ticksExisted >= this.spellCooldown;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		return this.entity.getAttackTarget() != null && this.spellWarmup > 0;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.entity.setSpellCasting(false);
		this.spellWarmup = this.getCastWarmupTime();
		this.entity.setSpellTicks(this.getCastingTime());
		this.spellCooldown = this.entity.ticksExisted + this.getCastingInterval();
		SoundEvent soundevent = this.getSpellPrepareSound();

		if (soundevent != null) {
			this.entity.playSound(soundevent, 1.0F, 1.0F);
		}

		this.entity.setSpellType(getSpellType());
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		this.entity.setSpellCasting(true);
		--this.spellWarmup;

		if (this.spellWarmup == 0) {
			this.castSpell();
			this.entity.setSpellCasting(false);
			this.entity.setSpellType(ESpellType.NONE);
			this.entity.playSound(this.getSpellType().getSpellSound(), 1.0F, 1.0F);
		}
	}

	protected abstract void castSpell();

	protected int getCastWarmupTime() {
		return 20;
	}

	protected abstract int getCastingTime();

	protected abstract int getCastingInterval();

	@Nullable
	protected abstract SoundEvent getSpellPrepareSound();

	protected abstract ESpellType getSpellType();

}
