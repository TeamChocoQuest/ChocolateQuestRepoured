package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRanged extends EntityAIAttack {

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
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
		this.updatePath(attackTarget);
		this.entity.isSwingInProgress = true;
		this.entity.setActiveHand(EnumHand.MAIN_HAND);
	}
	
	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null) {
			boolean canSeeAttackTarget = this.entity.getEntitySenses().canSee(attackTarget);
			if (canSeeAttackTarget) {
				this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);
				this.checkAndPerformAttack(this.entity.getAttackTarget());
			}
			if (canSeeAttackTarget && this.entity.isEntityInFieldOfView(attackTarget)) {
				if (this.entity.getDistance(attackTarget) > 28.0D) {
					this.entity.isSwingInProgress = false;
					this.updatePath(attackTarget);
				} else if (this.entity.hasPath()) {
					this.entity.getNavigator().clearPath();
				}
				this.visionTick = 10;
			} else if (this.visionTick > 0) {
				this.updatePath(attackTarget);
				this.visionTick--;
				this.entity.isSwingInProgress = false;
			}
		}
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick <= 0 && this.entity.getDistance(attackTarget) <= 32.0D) {
			ItemStack stack = this.entity.getHeldItemMainhand();
			this.entity.isSwingInProgress = false;
			if (stack.getItem() instanceof ItemBow) {
				this.attackTick = 60;
				EntityTippedArrow arrow = new EntityTippedArrow(this.entity.world, this.entity);
				double x = attackTarget.posX - this.entity.posX;
				double y = attackTarget.posY + (double) attackTarget.height * 0.5D - arrow.posY;
				double z = attackTarget.posZ - this.entity.posZ;
				double distance = Math.sqrt(x * x + z * z);
				arrow.shoot(x, y + distance * 0.06D, z, 3.0F, 0.0F);
				arrow.motionX += this.entity.motionX;
				arrow.motionX += this.entity.motionX;
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

	private boolean isRangedWeapon(Item item) {
		return item instanceof ItemBow || item instanceof IRangedWeapon;
	}

}
