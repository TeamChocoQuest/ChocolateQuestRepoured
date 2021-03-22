package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.objects.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.IRangedWeapon;

public class EntityAIAttackRanged extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int prevTimeAttacked;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public EntityAIAttackRanged(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.isRangedWeapon(this.entity.getHeldItemMainhand().getItem())) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(attackTarget);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.isRangedWeapon(this.entity.getHeldItemMainhand().getItem())) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getLastTimeSeenAttackTarget() + 100 >= this.entity.ticksExisted;
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.entity.resetActiveHand();
		this.entity.isSwingInProgress = false;
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return;
		}
		double distance = this.entity.getDistance(attackTarget);

		if (this.entity.getEntitySenses().canSee(attackTarget) && (distance < this.getAttackRange() * 0.9D || (distance < this.getAttackRange() && !this.entity.hasPath()))) {
			// this.entity.faceEntity(attackTarget, 30.0F, 30.0F);
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
			this.checkAndPerformAttack(attackTarget);
			this.entity.getNavigator().clearPath();
			this.strafingTime++;
		} else {
			this.entity.getNavigator().tryMoveToEntityLiving(attackTarget, 1.0D);
			this.strafingTime = -1;
			// this.entity.resetActiveHand();
			// this.entity.isSwingInProgress = false;
		}

		if (this.strafingTime >= 20) {
			if (this.random.nextDouble() < 0.3D) {
				this.strafingClockwise = !this.strafingClockwise;
			}

			if (this.random.nextDouble() < 0.3D) {
				this.strafingBackwards = !this.strafingBackwards;
			}

			this.strafingTime = 0;
		}

		if (this.canStrafe() && this.strafingTime > -1) {
			if (distance > this.getAttackRange() * 0.75D) {
				this.strafingBackwards = false;
			} else if (distance < this.getAttackRange() * 0.25D) {
				this.strafingBackwards = true;
			}

			float f = (float) (this.entity.isNonBoss() ? CQRConfig.mobs.entityStrafingSpeed : CQRConfig.mobs.entityStrafingSpeedBoss);
			this.entity.getMoveHelper().strafe(this.strafingBackwards ? -f : f, this.strafingClockwise ? f : -f);
		}
	}

	private boolean canStrafe() {
		if (!this.entity.canStrafe()) {
			return false;
		}
		return this.entity.isNonBoss() ? CQRConfig.mobs.enableEntityStrafing : CQRConfig.mobs.enableEntityStrafingBoss;
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.entity.ticksExisted > this.prevTimeAttacked + this.getAttackCooldown()) {
			if (this.getAttackChargeTicks() > 0) {
				this.entity.setActiveHand(EnumHand.MAIN_HAND);
				this.entity.isSwingInProgress = true;
			}

			if (this.entity.getItemInUseMaxCount() >= this.getAttackChargeTicks()) {
				ItemStack stack = this.entity.getHeldItemMainhand();
				Item item = stack.getItem();

				if (item instanceof ItemBow) {
					ItemStack arrowItem = this.entity.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.ARROW);
					if (arrowItem.isEmpty() || !(arrowItem.getItem() instanceof ItemArrow)) {
						arrowItem = new ItemStack(Items.ARROW);
					}
					EntityArrow arrow = ((ItemArrow) arrowItem.getItem()).createArrow(this.world, arrowItem, this.entity);
					// arrowItem.shrink(1);

					double x = attackTarget.posX - this.entity.posX;
					double y = attackTarget.posY + (double) attackTarget.height * 0.5D - arrow.posY;
					double z = attackTarget.posZ - this.entity.posZ;
					double distance = Math.sqrt(x * x + z * z);
					float inaccuracy = 4.0F;
					if (this.world.getDifficulty() == EnumDifficulty.HARD) {
						inaccuracy = 1.0F;
					} else if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
						inaccuracy = 2.0F;
					}
					arrow.shoot(x, y + distance * distance * 0.0045D, z, 2.4F, inaccuracy);
					arrow.motionX += this.entity.motionX;
					arrow.motionZ += this.entity.motionZ;
					if (!this.entity.onGround) {
						arrow.motionY += this.entity.motionY;
					}
					this.world.spawnEntity(arrow);
					this.entity.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
				} else if (item instanceof IRangedWeapon) {
					((IRangedWeapon) item).shoot(this.world, this.entity, attackTarget, EnumHand.MAIN_HAND);
					if (((IRangedWeapon) item).getShootSound() != null) {
						this.entity.playSound(((IRangedWeapon) item).getShootSound(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}
				}

				this.prevTimeAttacked = this.entity.ticksExisted;
				if (this.getAttackChargeTicks() > 0) {
					this.entity.resetActiveHand();
					this.entity.isSwingInProgress = false;
				} else {
					this.entity.swingArm(EnumHand.MAIN_HAND);
				}
			}
		}
	}

	protected boolean isRangedWeapon(Item item) {
		return item instanceof ItemBow || item instanceof IRangedWeapon;
	}

	protected double getAttackRange() {
		ItemStack stack = this.entity.getHeldItemMainhand();
		Item item = stack.getItem();

		if (item instanceof ItemBow) {
			return 32.0D;
		} else if (item instanceof IRangedWeapon) {
			return ((IRangedWeapon) item).getRange();
		}

		return 32.0D;
	}

	protected int getAttackCooldown() {
		ItemStack stack = this.entity.getHeldItemMainhand();
		Item item = stack.getItem();

		if (item instanceof ItemBow) {
			switch (this.world.getDifficulty()) {
			case HARD:
				return 20;
			case NORMAL:
				return 30;
			default:
				return 40;
			}
		} else if (item instanceof IRangedWeapon) {
			return ((IRangedWeapon) item).getCooldown();
		}

		return 40;
	}

	protected int getAttackChargeTicks() {
		ItemStack stack = this.entity.getHeldItemMainhand();
		Item item = stack.getItem();

		if (item instanceof ItemBow) {
			return 20;
		} else if (item instanceof IRangedWeapon) {
			return ((IRangedWeapon) item).getChargeTicks();
		}

		return 40;
	}

}
