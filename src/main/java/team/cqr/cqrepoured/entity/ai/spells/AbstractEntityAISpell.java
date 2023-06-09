package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class AbstractEntityAISpell<T extends AbstractEntityCQR> implements IEntityAISpell {

	protected final Random random = new Random();
	protected final T entity;
	protected final Level world;
	protected boolean needsTargetToStart;
	protected boolean needsSightToStart;
	protected boolean needsTargetToContinue;
	protected boolean needsSightToContinue;
	protected final int cooldown;
	protected final int chargingTicks;
	protected final int castingTicks;
	protected int prevTimeCasted;
	protected int tick;

	protected AbstractEntityAISpell(T entity, int cooldown, int chargingTicks, int castingTicks) {
		this.entity = entity;
		this.world = entity.level;
		this.cooldown = cooldown;
		this.chargingTicks = Math.max(chargingTicks, 0);
		this.castingTicks = Math.max(castingTicks, 1);
		this.prevTimeCasted = -this.random.nextInt(cooldown + 1) - 1;
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.isAlive()) {
			return false;
		}
		if (this.needsTargetToStart) {
			LivingEntity attackTarget = this.entity.getTarget();
			if (attackTarget == null) {
				return false;
			}
			if (this.needsSightToStart && !this.entity.getSensing().canSee(attackTarget)) {
				return false;
			}
		}
		return this.entity.tickCount > this.prevTimeCasted + this.cooldown;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.isAlive()) {
			return false;
		}
		if (this.needsTargetToContinue) {
			LivingEntity attackTarget = this.entity.getTarget();
			if (attackTarget == null) {
				return false;
			}
			if (this.needsSightToContinue && !this.entity.getSensing().canSee(attackTarget)) {
				return false;
			}
		}
		return this.tick < this.chargingTicks + this.castingTicks;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void startExecuting() {
		this.tick = 0;
	}

	@Override
	public void resetTask() {
		this.prevTimeCasted = this.entity.tickCount;
		this.tick = -1;
	}

	@Override
	public void updateTask() {
		if (this.tick < this.chargingTicks) {
			if (this.tick == 0) {
				this.startChargingSpell();
			}
			this.chargeSpell();
		} else {
			if (this.tick == this.chargingTicks) {
				this.startCastingSpell();
			}
			this.castSpell();
		}
		this.tick++;
	}

	@Override
	public boolean isCharging() {
		return this.tick != -1 && this.tick < this.chargingTicks;
	}

	@Override
	public boolean isCasting() {
		return this.tick != -1 && this.tick < this.chargingTicks + this.castingTicks;
	}

	@Override
	public void startChargingSpell() {
		if (this.getStartChargingSound() != null) {
			this.entity.playSound(this.getStartChargingSound(), 1.0F, 0.9F + 0.2F * this.random.nextFloat());
		}
	}

	@Override
	public void chargeSpell() {

	}

	@Override
	public void startCastingSpell() {
		if (this.getStartCastingSound() != null) {
			this.entity.playSound(this.getStartCastingSound(), 1.0F, 0.9F + 0.2F * this.random.nextFloat());
		}
	}

	@Override
	public void castSpell() {

	}

	@Nullable
	protected SoundEvent getStartChargingSound() {
		return null;
	}

	@Nullable
	protected SoundEvent getStartCastingSound() {
		return null;
	}

	protected void setup(boolean needsTargetToStart, boolean needsSightToStart, boolean needsTargetToContinue, boolean needsSightToContinue) {
		this.needsTargetToStart = needsTargetToStart;
		this.needsSightToStart = needsSightToStart;
		this.needsTargetToContinue = needsTargetToContinue;
		this.needsSightToContinue = needsSightToContinue;
	}

}
