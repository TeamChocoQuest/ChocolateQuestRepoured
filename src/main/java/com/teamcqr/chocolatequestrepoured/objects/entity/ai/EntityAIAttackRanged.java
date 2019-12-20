package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.ECQREntityArmPoses;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;

public class EntityAIAttackRanged extends EntityAIAttack {

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHeldItemWeapon().getItem() instanceof ItemBow && super.shouldExecute();
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null && this.entity.getEntitySenses().canSee(attackTarget)) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);

			if (this.entity.isEntityInFieldOfView(attackTarget)) {
				this.entity.setArmPose(ECQREntityArmPoses.PULLING_BOW);
				if (this.entity.getDistance(attackTarget) > 28.0D) {
					this.updatePath(attackTarget);
				} else if (this.entity.hasPath()) {
					this.entity.getNavigator().clearPath();
				}
				this.visionTick = 20;
			} else if (this.visionTick > 0) {
				this.updatePath(attackTarget);
				this.visionTick--;
			}

			this.checkAndPerformAttack(this.entity.getAttackTarget());
		}

		this.checkAndPerformBlock();
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick <= 0 && this.entity.getDistance(attackTarget) <= 32.0D && this.entity.getHeldItemMainhand().getItem() instanceof ItemBow) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.setArmPose(ECQREntityArmPoses.HOLDING_ITEM);
				this.entity.resetActiveHand();
				this.attackTick = 100;
				this.shieldTick = 60;
			} else {
				this.attackTick = 60;
				this.entity.setArmPose(ECQREntityArmPoses.HOLDING_ITEM);
			}
			EntityTippedArrow arrow = new EntityTippedArrow(this.entity.world, this.entity);
			double x = attackTarget.posX - this.entity.posX;
			double y = attackTarget.getEntityBoundingBox().minY + (double) (attackTarget.height / 3.0F) - arrow.posY;
			double z = attackTarget.posZ - this.entity.posZ;
			double distance = Math.sqrt(x * x + z * z);
			arrow.shoot(x, y + distance * 0.20000000298023224D, z, 1.6F, 7.0F - (float) this.entity.world.getDifficulty().getDifficultyId() * 2.0F);
			this.entity.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.entity.getRNG().nextFloat() * 0.4F + 0.8F));
			this.entity.world.spawnEntity(arrow);
			this.entity.setArmPose(ECQREntityArmPoses.HOLDING_ITEM);
		} else {
			this.entity.setArmPose(ECQREntityArmPoses.PULLING_BOW);
		}
	}

}
