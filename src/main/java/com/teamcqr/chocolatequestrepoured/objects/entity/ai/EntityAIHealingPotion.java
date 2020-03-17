package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.TargetUtil;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.EntityUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIHealingPotion extends AbstractCQREntityAI {

	protected int ticksNotHealing;
	protected boolean isHealing;

	public EntityAIHealingPotion(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHealingPotions() > 0 && this.entity.getHealth() <= Math.max(this.entity.getMaxHealth() * 0.15F, 5.0F);
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().clearPath();
		this.ticksNotHealing = 0;
		this.isHealing = false;
	}

	@Override
	public void resetTask() {
		this.ticksNotHealing = 0;
		this.isHealing = false;
		this.entity.resetActiveHand();
		if (this.entity.isHoldingPotion()) {
			this.entity.swapWeaponAndPotionSlotItemStacks();
		}
	}

	@Override
	public void updateTask() {
		Entity attackTarget = this.entity.getAttackTarget();

		if (this.isHealing) {
			this.entity.swingArm(EnumHand.MAIN_HAND);
		} else {
			if (attackTarget == null) {
				this.startHealing();
			}
		}

		boolean flag = true;
		if (attackTarget != null) {
			AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - CQRConfig.mobs.alertRadius /2, entity.posY - CQRConfig.mobs.alertRadius /3, entity.posZ - CQRConfig.mobs.alertRadius /2, entity.posX + CQRConfig.mobs.alertRadius /2, entity.posY + CQRConfig.mobs.alertRadius /3, entity.posZ + CQRConfig.mobs.alertRadius /2);
			List<Entity> possibleEnts = entity.world.getEntitiesInAABBexcluding(entity, aabb, TargetUtil.PREDICATE_ALLIES(entity.getFaction()));
			
			if (!possibleEnts.isEmpty()) {
				Entity e1 = null;
				int count = -1;
				double distance = Double.MAX_VALUE;
				for (Entity e2 : possibleEnts) {
					AxisAlignedBB aabb1 = new AxisAlignedBB(e2.posX - 4, e2.posY -2, e2.posZ -4, e2.posX +4, e2.posY +2, e2.posZ +4);
					List<Entity> list = e2.world.getEntitiesInAABBexcluding(e2, aabb1, TargetUtil.PREDICATE_ALLIES(this.entity.getFaction()));
					double d = this.entity.getDistanceSq(e2); 
					if (list.size() > count || (list.size() == count && d < distance)) {
						e1 = e2;
						count = list.size();
						distance = d;
					}
				}
				if (count >= 5) {
					this.entity.getNavigator().tryMoveToEntityLiving(e1, 1.0D);
					flag = false;
				}
			}
	
			boolean canMoveBackwards = this.canMoveBackwards();
			
			if(flag) {
				//No larger group in range
				this.updateRotation(attackTarget, 2.5F, 2.5F);

				if (canMoveBackwards) {
					EntityUtil.move2D(this.entity, 0.0D, -0.2D, this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 1.5, this.entity.rotationYawHead);
				}
			}

			if (!this.isHealing) {
				if (this.entity.collidedHorizontally || !canMoveBackwards || this.ticksNotHealing > 80 || this.entity.getDistance(attackTarget) > 8.0F) {
					this.startHealing();
				} else {
					this.checkAndPerformBlock();
				}
			}
		}
	}

	private void updateRotation(Entity entity, float deltaYaw, float deltaPitch) {
		double x = entity.posX - this.entity.posX;
		double y = entity.posY - this.entity.posY;
		double z = entity.posZ - this.entity.posZ;
		double d = Math.sqrt(x * x + z * z);

		float yaw = (float) Math.toDegrees(Math.atan2(-x, z));
		float pitch = (float) Math.toDegrees(Math.atan2(-y, d));
		this.entity.rotationYaw += MathHelper.clamp(MathHelper.wrapDegrees(yaw - this.entity.rotationYaw), -deltaYaw, deltaYaw);
		this.entity.rotationYaw = MathHelper.wrapDegrees(this.entity.rotationYaw);
		this.entity.rotationPitch += MathHelper.clamp(MathHelper.wrapDegrees(pitch - this.entity.rotationPitch), -deltaPitch, deltaPitch);
		this.entity.rotationPitch = MathHelper.clamp(this.entity.rotationPitch, -90.0F, 90.0F);
		this.entity.rotationYawHead = this.entity.rotationYaw;
	}

	private void checkAndPerformBlock() {
		if (!this.entity.isActiveItemStackBlocking()) {
			ItemStack offhand = this.entity.getHeldItem(EnumHand.OFF_HAND);

			if (offhand.getItem().isShield(offhand, this.entity)) {
				this.entity.setActiveHand(EnumHand.OFF_HAND);
			}
		}
	}

	private boolean canMoveBackwards() {
		double sin = -Math.sin(Math.toRadians(this.entity.rotationYaw));
		double cos = Math.cos(Math.toRadians(this.entity.rotationYaw));
		BlockPos pos = new BlockPos(this.entity.posX - sin, this.entity.posY - 0.001D, this.entity.posZ - cos);
		IBlockState state = this.entity.world.getBlockState(pos);
		return state.isSideSolid(this.entity.world, pos, EnumFacing.UP);
	}

	public void startHealing() {
		if (!this.isHealing) {
			this.isHealing = true;
			if (!this.entity.isHoldingPotion()) {
				this.entity.swapWeaponAndPotionSlotItemStacks();
			}
			this.entity.resetActiveHand();
			this.entity.setActiveHand(EnumHand.MAIN_HAND);
		}
	}

}
