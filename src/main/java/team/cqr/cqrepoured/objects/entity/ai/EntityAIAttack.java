package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAIAttack extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int attackTick;
	private float attackCooldownOverhead;

	public EntityAIAttack(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		return attackTarget != null && this.entity.getEntitySenses().canSee(attackTarget);
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		return attackTarget != null && this.entity.getEntitySenses().canSee(attackTarget);
	}

	@Override
	public void startExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		this.updatePath(attackTarget);
		this.checkAndPerformBlock();
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);
			this.updatePath(attackTarget);
			this.checkAndPerformAttack(this.entity.getAttackTarget());
			this.checkAndPerformBlock();
		}
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.entity.resetActiveHand();
	}

	protected void updatePath(EntityLivingBase target) {
		this.entity.getNavigator().tryMoveToEntityLiving(target, 1.0D);
	}

	protected void checkAndPerformBlock() {
		if (this.entity.getLastTimeHitByAxeWhileBlocking() + 80 > this.entity.ticksExisted) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.resetActiveHand();
			}
		} else if (this.attackTick + this.getBlockCooldownPeriod() <= this.entity.ticksExisted && !this.entity.isActiveItemStackBlocking()) {
			ItemStack offhand = this.entity.getHeldItemOffhand();
			if (offhand.getItem().isShield(offhand, this.entity)) {
				this.entity.setActiveHand(EnumHand.OFF_HAND);
			}
		}
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick + (int) this.getAttackCooldownPeriod() <= this.entity.ticksExisted && this.entity.isInAttackReach(attackTarget)) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.resetActiveHand();
			}
			if (this.attackTick + this.getAttackCooldownPeriod() > this.entity.ticksExisted) {
				this.attackCooldownOverhead = this.getAttackCooldownPeriod() % 1.0F;
			} else {
				this.attackCooldownOverhead = 0.0F;
			}
			this.attackTick = this.entity.ticksExisted;
			this.entity.swingArm(EnumHand.MAIN_HAND);
			this.entity.attackEntityAsMob(attackTarget);
		}
	}

	public float getAttackCooldownPeriod() {
		return (float) (1.0D / this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0D) + this.attackCooldownOverhead;
	}

	public int getBlockCooldownPeriod() {
		return 30;
	}

}
