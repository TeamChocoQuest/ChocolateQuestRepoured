package team.cqr.cqrepoured.entity.ai.attack.special;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class EntityAIAttackSpecial extends AbstractCQREntityAI<AbstractEntityCQR> {

	public static final List<AbstractEntityAIAttackSpecial> SPECIAL_ATTACKS = new ArrayList<>();
	static {
		// SPECIAL_ATTACKS.add(new EntityAIAttackSpecialDagger());
		// SPECIAL_ATTACKS.add(new EntityAIAttackSpecialGreatSword());
		// SPECIAL_ATTACKS.add(new EntityAIAttackSpecialSpear());
		SPECIAL_ATTACKS.add(new EntityAIAttackSpecialSpinAttack());
	}
	private AbstractEntityAIAttackSpecial activeSpecialAttack;
	private int specialAttackTick;
	private int tick;

	public EntityAIAttackSpecial(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean canUse() {
		LivingEntity attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		if (!this.entity.getSensing().canSee(attackTarget)) {
			return false;
		}
		for (AbstractEntityAIAttackSpecial specialAttack : SPECIAL_ATTACKS) {
			if (this.specialAttackTick + specialAttack.getCooldown(this.entity) >= this.entity.ticksExisted) {
				continue;
			}
			if (!specialAttack.shouldStartAttack(this.entity, attackTarget)) {
				continue;
			}
			if (!DungeonGenUtils.percentageRandom(specialAttack.getAttackChance(this.entity, attackTarget))) {
				continue;
			}
			this.activeSpecialAttack = specialAttack;
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.tick >= this.activeSpecialAttack.getMaxUseTime()) {
			return false;
		}
		LivingEntity attackTarget = this.entity.getAttackTarget();
		if ((this.activeSpecialAttack.needsTargetToContinue() || this.activeSpecialAttack.needsSightToContinue()) && attackTarget == null) {
			return false;
		}
		if (this.activeSpecialAttack.needsSightToContinue() && !this.entity.getSensing().canSee(attackTarget)) {
			return false;
		}
		return this.activeSpecialAttack.shouldContinueAttack(this.entity, attackTarget);
	}

	@Override
	public boolean isInterruptable() {
		return this.activeSpecialAttack.isInterruptible(this.entity);
	}

	@Override
	public void start() {
		this.specialAttackTick = this.entity.ticksExisted;
		this.tick = 0;

		LivingEntity attackTarget = this.entity.getAttackTarget();
		this.activeSpecialAttack.startAttack(this.entity, attackTarget);
	}

	@Override
	public void stop() {
		this.tick = -1;

		this.activeSpecialAttack.resetAttack(this.entity);
	}

	@Override
	public void tick() {
		LivingEntity attackTarget = this.entity.getAttackTarget();
		this.activeSpecialAttack.continueAttack(this.entity, attackTarget, this.tick++);

		if (this.tick == this.activeSpecialAttack.getMaxUseTime()) {
			this.activeSpecialAttack.stopAttack(this.entity, attackTarget);
		}
	}

}
