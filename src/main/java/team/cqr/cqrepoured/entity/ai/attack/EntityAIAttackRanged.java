package team.cqr.cqrepoured.entity.ai.attack;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.item.IRangedWeapon;

public class EntityAIAttackRanged<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	protected int prevTimeAttacked;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public EntityAIAttackRanged(T entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	protected ItemStack getEquippedWeapon() {
		return this.entity.getMainHandItem();
	}

	@Override
	public boolean canUse() {
		if (!this.isRangedWeapon(this.getEquippedWeapon().getItem())) {
			return false;
		}
		LivingEntity attackTarget = this.entity.getTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getSensing().canSee(attackTarget);
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.isRangedWeapon(this.getEquippedWeapon().getItem())) {
			return false;
		}
		LivingEntity attackTarget = this.entity.getTarget();
		if (attackTarget == null) {
			return false;
		}
		return this.entity.getLastTimeSeenAttackTarget() + 100 >= this.entity.tickCount;
	}

	@Override
	public void start() {
		this.entity.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.entity.getNavigation().stop();
		this.entity.stopUsingItem();
		this.entity.swinging = false;
	}

	@Override
	public void tick() {
		LivingEntity attackTarget = this.entity.getTarget();
		if (attackTarget == null) {
			return;
		}
		double distanceSq = this.entity.distanceToSqr(attackTarget);
		double attackRangeSq = this.getAttackRange() * this.getAttackRange();

		if (this.entity.getSensing().canSee(attackTarget) && (distanceSq < attackRangeSq * 0.9D * 0.9D || (distanceSq < attackRangeSq && !this.entity.isPathFinding()))) {
			// this.entity.faceEntity(attackTarget, 30.0F, 30.0F);
			this.entity.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
			this.checkAndPerformAttack(attackTarget);
			this.entity.getNavigation().stop();
			this.strafingTime++;
		} else {
			this.entity.getNavigation().moveTo(attackTarget, 1.0D);
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
			if (distanceSq > attackRangeSq * 0.75D * 0.75D) {
				this.strafingBackwards = false;
			} else if (distanceSq < attackRangeSq * 0.25D * 0.25D) {
				this.strafingBackwards = true;
			}

			float f = this.getStrafingSpeed();
			this.entity.getMoveControl().strafe(this.strafingBackwards ? -f : f, this.strafingClockwise ? f : -f);
		}
	}

	protected float getStrafingSpeed() {
		return (float) (this.entity instanceof AbstractEntityCQRBoss ? CQRConfig.mobs.entityStrafingSpeed : CQRConfig.mobs.entityStrafingSpeedBoss);
	}

	protected boolean canStrafe() {
		if (!this.entity.canStrafe()) {
			return false;
		}
		return this.entity instanceof AbstractEntityCQRBoss ? CQRConfig.mobs.enableEntityStrafing : CQRConfig.mobs.enableEntityStrafingBoss;
	}

	protected void checkAndPerformAttack(LivingEntity attackTarget) {
		if (this.entity.tickCount > this.prevTimeAttacked + this.getAttackCooldown()) {
			if (this.getAttackChargeTicks() > 0) {
				this.entity.startUsingItem(Hand.MAIN_HAND);
				this.entity.swinging = true;
			}

			if (this.entity.getUseItemRemainingTicks() >= this.getAttackChargeTicks()) {
				ItemStack stack = this.getEquippedWeapon();
				Item item = stack.getItem();

				if (item instanceof BowItem) {
					ItemStack arrowItem = this.entity.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.ARROW);
					if (arrowItem.isEmpty() || !(arrowItem.getItem() instanceof ArrowItem)) {
						arrowItem = new ItemStack(Items.ARROW);
					}
					AbstractArrowEntity arrow = ((ArrowItem) arrowItem.getItem()).createArrow(this.world, arrowItem, this.entity);
					// arrowItem.shrink(1);

					double x = attackTarget.getX() - this.entity.getX();
					double y = attackTarget.getY() + attackTarget.getBbHeight() * 0.5D - arrow.getY();
					double z = attackTarget.getZ() - this.entity.getZ();
					double distance = Math.sqrt(x * x + z * z);
					arrow.shoot(x, y + distance * distance * 0.0045D, z, 2.4F, this.getInaccuracy());
					/*arrow.motionX += this.entity.motionX;
					arrow.motionZ += this.entity.motionZ;
					if (!this.entity.onGround) {
						arrow.motionY += this.entity.motionY;
					}*/
					Vector3d shooterVec = this.entity.getDeltaMovement();
					arrow.setDeltaMovement(arrow.getDeltaMovement().add(shooterVec.x(), this.entity.isOnGround() ? 0 : shooterVec.y(), shooterVec.z()));
					this.world.addFreshEntity(arrow);
					this.entity.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
				} else if (item instanceof IRangedWeapon) {
					((IRangedWeapon) item).shoot(this.world, this.entity, attackTarget, Hand.MAIN_HAND);
					if (((IRangedWeapon) item).getShootSound() != null) {
						this.entity.playSound(((IRangedWeapon) item).getShootSound(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}
				}

				this.prevTimeAttacked = this.entity.tickCount;
				if (this.getAttackChargeTicks() > 0) {
					this.entity.stopUsingItem();
					this.entity.swinging = false;
				} else {
					this.entity.startUsingItem(Hand.MAIN_HAND);
				}
			}
		}
	}

	protected float getInaccuracy() {
		float inaccuracy = 4.0F;
		if (this.world.getDifficulty() == Difficulty.HARD) {
			inaccuracy = 1.0F;
		} else if (this.world.getDifficulty() == Difficulty.NORMAL) {
			inaccuracy = 2.0F;
		}
		return inaccuracy;
	}

	protected boolean isRangedWeapon(Item item) {
		return item instanceof BowItem || item instanceof IRangedWeapon;
	}

	protected double getAttackRange() {
		ItemStack stack = this.getEquippedWeapon();
		Item item = stack.getItem();

		if (item instanceof BowItem) {
			return 32.0D;
		} else if (item instanceof IRangedWeapon) {
			return ((IRangedWeapon) item).getRange();
		}

		return 32.0D;
	}

	protected int getAttackCooldown() {
		ItemStack stack = this.getEquippedWeapon();
		Item item = stack.getItem();

		if (item instanceof BowItem) {
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
		ItemStack stack = this.getEquippedWeapon();
		Item item = stack.getItem();

		if (item instanceof BowItem) {
			return 20;
		} else if (item instanceof IRangedWeapon) {
			return ((IRangedWeapon) item).getChargeTicks();
		}

		return 40;
	}

}
