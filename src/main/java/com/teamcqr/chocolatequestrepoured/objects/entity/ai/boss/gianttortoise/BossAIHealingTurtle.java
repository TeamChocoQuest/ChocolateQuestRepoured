package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

public class BossAIHealingTurtle extends AbstractCQREntityAI {

	private boolean healingActive = false;

	private final int healingDuration = 160;
	private final int MIN_HEALING_AMOUNT = 50;
	private int currHealTicks = 0;

	public BossAIHealingTurtle(EntityCQRGiantTortoise entity) {
		super(entity);
		setMutexBits(8);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		this.healingActive = false;
		if(!getBoss().isSpinning() && !getBoss().isStunned() && (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2F) && this.currHealTicks < this.getHealingAmount() && this.getHealingAmount() >= MIN_HEALING_AMOUNT) {
			((EntityCQRGiantTortoise) this.entity).setHealing(true);
			if(((EntityCQRGiantTortoise) this.entity).isInShell()) {
				this.healingActive = true;
				return true;
			} else {
				((EntityCQRGiantTortoise) this.entity).targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_IN);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isInterruptible() {
		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setStunned(false);
		this.currHealTicks = 0;
		updateTask();
	}

	@Override
	public boolean shouldContinueExecuting() {
		this.healingActive = false;
		if(!this.entity.isDead && this.currHealTicks <= this.getHealingAmount()) {
			if(((EntityCQRGiantTortoise) this.entity).isInShell()) {
				this.healingActive = true;
			}
			return true;
		} 
		return false;
	}

	@Override
	public void updateTask() {
		((EntityCQRGiantTortoise) this.entity).setHealing(true);
		if (this.healingActive) {
			((EntityCQRGiantTortoise) this.entity).setInShell(true);
			if (this.currHealTicks >= this.getHealingAmount() || (this.entity.getHealth() / this.entity.getMaxHealth() >= 0.8F)) {
				// Cancel
				this.healingActive = false;
				((EntityCQRGiantTortoise) this.entity).setTimesHealed(((EntityCQRGiantTortoise) this.entity).getTimesHealed() +1);
				this.getBoss().setCanBeStunned(true);
			} else {
				((WorldServer)entity.getEntityWorld()).spawnParticle(EnumParticleTypes.HEART, entity.posX, entity.posY, entity.posZ, 5, 0.5D, 1.0D, 0.5D, 0D);
				this.getBoss().heal(1F);
				this.getBoss().setCanBeStunned(false);
				this.getBoss().setStunned(false);
			}
			this.currHealTicks++;
		}
	}
	
	public int getHealingAmount() {
		if (((EntityCQRGiantTortoise) this.entity).getTimesHealed() <= 0) {
			return 0;
		}
		return this.healingDuration / ((EntityCQRGiantTortoise) this.entity).getTimesHealed();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setCanBeStunned(true);
		this.currHealTicks = 0;
		((EntityCQRGiantTortoise) this.entity).setHealing(false);
		this.healingActive = false;
	}

}
