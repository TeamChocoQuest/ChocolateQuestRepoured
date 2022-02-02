package team.cqr.cqrepoured.entity.ai.attack;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIAttack extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int attackTick;
	private float attackCooldownOverhead;

	public EntityAIAttack(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity attackTarget = this.entity.getTarget();
		return attackTarget != null && this.entity.getSensing().canSee(attackTarget);
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity attackTarget = this.entity.getTarget();
		return attackTarget != null && this.entity.getSensing().canSee(attackTarget);
	}

	@Override
	public void start() {
		LivingEntity attackTarget = this.entity.getTarget();
		this.updatePath(attackTarget);
		this.checkAndPerformBlock();
	}

	@Override
	public void tick() {
		LivingEntity attackTarget = this.entity.getTarget();

		if (attackTarget != null) {
			this.entity.getLookControl().setLookAt(attackTarget, 12.0F, 12.0F);
			this.updatePath(attackTarget);
			this.checkAndPerformAttack(this.entity.getTarget());
			this.checkAndPerformBlock();
		}
	}

	@Override
	public void stop() {
		this.entity.getNavigation().stop();
		this.entity.stopUsingItem();
	}

	protected void updatePath(LivingEntity target) {
		this.entity.getNavigation().moveTo(target, 1.0D);
	}

	protected void checkAndPerformBlock() {
		if (this.entity.getLastTimeHitByAxeWhileBlocking() + 80 > this.entity.tickCount) {
			if (this.entity.isBlocking()) {
				this.entity.stopUsingItem();
			}
		} else if (this.attackTick + this.getBlockCooldownPeriod() <= this.entity.tickCount && !this.entity.isBlocking()) {
			ItemStack offhand = this.entity.getMainHandItem();
			if (offhand.getItem().isShield(offhand, this.entity)) {
				this.entity.startUsingItem(Hand.OFF_HAND);
			}
		}
	}

	protected void checkAndPerformAttack(LivingEntity attackTarget) {
		if (this.attackTick + (int) this.getAttackCooldownPeriod() <= this.entity.tickCount && this.entity.isInAttackReach(attackTarget)) {
			if (this.entity.isBlocking()) {
				this.entity.stopUsingItem();
			}
			if (this.attackTick + this.getAttackCooldownPeriod() > this.entity.tickCount) {
				this.attackCooldownOverhead = this.getAttackCooldownPeriod() % 1.0F;
			} else {
				this.attackCooldownOverhead = 0.0F;
			}
			this.attackTick = this.entity.tickCount;
			this.entity.swing(Hand.MAIN_HAND);
			this.entity.canAttack(attackTarget);
		}
	}

	public float getAttackCooldownPeriod() {
		return (float) (1.0D / this.entity.getAttribute(Attributes.ATTACK_SPEED).getValue() * 20.0D) + this.attackCooldownOverhead;
	}

	public int getBlockCooldownPeriod() {
		return 30;
	}

}
