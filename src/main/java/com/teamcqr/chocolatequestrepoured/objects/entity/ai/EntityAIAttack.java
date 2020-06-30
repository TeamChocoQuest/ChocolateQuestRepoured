package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.IFakeWeapon;
import com.teamcqr.chocolatequestrepoured.objects.items.ISupportWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int attackTick;

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
		CQRFaction faction = this.entity.getFaction();
		if (faction != null) {
			Item item = this.entity.getHeldItemMainhand().getItem();
			if (faction.isEnemy(attackTarget)) {
				if (item instanceof ISupportWeapon) {
					this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((ISupportWeapon<?>) item).getFakeSword()));
				}
			} else if (faction.isAlly(attackTarget)) {
				if (item instanceof IFakeWeapon<?>) {
					this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
				}
			}
		}
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
		if (this.entity.getLastTimeHitByAxeWhileBlocking() + 60 > this.entity.ticksExisted) {
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
		if (this.attackTick + this.getAttackCooldownPeriod() <= this.entity.ticksExisted && this.entity.isInAttackReach(attackTarget)) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.resetActiveHand();
			}
			this.attackTick = this.entity.ticksExisted;
			this.entity.swingArm(EnumHand.MAIN_HAND);
			this.entity.attackEntityAsMob(attackTarget);
		}
	}

	public float getAttackCooldownPeriod() {
		float f = (float) (1.0D / this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0D);
		ItemStack stack = this.entity.getHeldItemOffhand();
		return stack.getItem().isShield(stack, this.entity) ? f + 20.0F : f;
	}

	public int getBlockCooldownPeriod() {
		return 20;
	}

}
