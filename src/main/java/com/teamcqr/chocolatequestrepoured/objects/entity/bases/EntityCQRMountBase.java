package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityCQRMountBase extends EntityAnimal {

	public EntityCQRMountBase(World worldIn) {
		super(worldIn);
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.9D));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return entityIn instanceof AbstractEntityCQR || entityIn instanceof EntityPlayer;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	@Nullable
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}

	@Override
	public boolean canBeSteered() {
		Entity entity = this.getControllingPassenger();

		return entity != null && (entity instanceof AbstractEntityCQR || entity instanceof EntityPlayer);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!super.processInteract(player, hand)) {
			if (!this.isBeingRidden()) {
				if (!this.world.isRemote) {
					player.startRiding(this);
				}

				return true;
			}

		}
		return false;

	}

	@Override
	public void travel(float strafe, float vertical, float forward) {
		if (this.isBeingRidden() && this.canBeSteered()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			this.rotationYaw = entitylivingbase.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
			this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.renderYawOffset = this.rotationYaw;
			this.rotationYawHead = this.renderYawOffset;
			strafe = entitylivingbase.moveStrafing * 0.5F;
			forward = entitylivingbase.moveForward;
			vertical = entitylivingbase.moveVertical;

			if (forward <= 0.0F) {
				forward *= 0.25F;
			}

			if (this.onGround) {
				strafe = 0.0F;
				forward = 0.0F;
			}

			if (this.onGround && moveVertical > 0.2) {
				this.motionY = 0.5F;

				if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
					this.motionY += (double) ((float) (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier()
							+ 1) * 0.1F);
				}

				this.setJumping(true);
				this.isAirBorne = true;

				if (forward > 0.0F) {
					float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
					float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
					this.motionX += (double) (-0.4F * f);
					this.motionZ += (double) (0.4F * f1);
					// this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
				}

			}

			this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

			if (this.canPassengerSteer()) {
				this.setAIMoveSpeed(
						(float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			} else if (entitylivingbase instanceof EntityPlayer) {
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			if (this.onGround) {
				this.setJumping(false);
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

			if (f2 > 1.0F) {
				f2 = 1.0F;
			}

			this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		} else {
			this.jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}

	@Override
	protected abstract ResourceLocation getLootTable();

}
