package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRanged extends EntityAIAttack {

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.isRangedWeapon(this.entity.getHeldItemMainhand().getItem()) && super.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isRangedWeapon(this.entity.getHeldItemMainhand().getItem()) && super.shouldContinueExecuting();
	}

	@Override
	public void startExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (this.entity.getDistance(attackTarget) < 28.0D) {
			this.entity.setActiveHand(EnumHand.MAIN_HAND);
			this.entity.isSwingInProgress = true;
		} else {
			this.updatePath(attackTarget);
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.isSwingInProgress = false;
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);

			double distance = this.entity.getDistance(attackTarget);
			if (distance < 28.0D || (!this.entity.hasPath() && distance < 32.0D)) {
				this.checkAndPerformAttack(this.entity.getAttackTarget());
				this.entity.getNavigator().clearPath();
				this.entity.setActiveHand(EnumHand.MAIN_HAND);
				this.entity.isSwingInProgress = true;
			} else {
				this.updatePath(attackTarget);
				this.entity.resetActiveHand();
				this.entity.isSwingInProgress = false;
			}
		}
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick <= 0 && this.entity.getDistance(attackTarget) <= 32.0D) {
			ItemStack stack = this.entity.getHeldItemMainhand();
			if (stack.getItem() instanceof ItemBow) {
				this.attackTick = 60;
				ItemStack arrowItem = this.entity.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.ARROW);
				if (arrowItem.isEmpty() || !(arrowItem.getItem() instanceof ItemArrow)) {
					arrowItem = new ItemStack(Items.ARROW, 1);
				}
				EntityArrow arrow = ((ItemArrow) arrowItem.getItem()).createArrow(this.entity.world, arrowItem, this.entity);
				arrowItem.shrink(1);

				double x = attackTarget.posX - this.entity.posX;
				double y = attackTarget.posY + (double) attackTarget.height * 0.5D - arrow.posY;
				double z = attackTarget.posZ - this.entity.posZ;
				double distance = Math.sqrt(x * x + z * z);
				arrow.shoot(x, y + distance * 0.06D, z, 3.0F, 0.0F);
				arrow.motionX += this.entity.motionX;
				arrow.motionZ += this.entity.motionZ;
				if (!this.entity.onGround) {
					arrow.motionY += this.entity.motionY;
				}
				this.entity.world.spawnEntity(arrow);
				this.entity.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
			} else if (stack.getItem() instanceof IRangedWeapon) {
				IRangedWeapon weapon = (IRangedWeapon) stack.getItem();
				this.attackTick = weapon.getCooldown();
				weapon.shoot(this.entity.world, this.entity, attackTarget, EnumHand.MAIN_HAND);
				this.entity.playSound(weapon.getShootSound(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
			}
		}
	}

	protected boolean isRangedWeapon(Item item) {
		return item instanceof ItemBow || item instanceof IRangedWeapon;
	}

}
