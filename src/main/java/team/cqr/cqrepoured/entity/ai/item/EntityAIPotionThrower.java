package team.cqr.cqrepoured.entity.ai.item;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttackRanged;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.ItemAlchemyBag;

public class EntityAIPotionThrower extends EntityAIAttackRanged<AbstractEntityCQR> {

	public EntityAIPotionThrower(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected ItemStack getEquippedWeapon() {
		return this.entity.getMainHandItem();
	}

	@Override
	protected boolean isRangedWeapon(Item item) {
		return (item instanceof ItemAlchemyBag || item instanceof SplashPotionItem || item instanceof LingeringPotionItem);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity attackTarget) {
		if (this.entity.tickCount > this.prevTimeAttacked + this.getAttackCooldown()) {
			ItemStack stack = this.getEquippedWeapon();
			Item item = stack.getItem();

			final double x = attackTarget.getX() - this.entity.getX();
			double y = attackTarget.getY() + attackTarget.getBbHeight() * 0.5D;
			final double z = attackTarget.getZ() - this.entity.getZ();
			final double distance = Math.sqrt(x * x + z * z);
			
			// Throwable potions
			if (item instanceof SplashPotionItem || item instanceof LingeringPotionItem) {
				PotionEntity proj = new PotionEntity(this.world, this.entity/*, stack.copy()*/);
				proj.setItem(stack.copy());
				y -= proj.getY();
				proj.shoot(x, y + distance * 0.06D, z, 1.F, this.entity.getRandom().nextFloat() * 0.25F);
				/*proj.motionX += this.entity.motionX;
				proj.motionZ += this.entity.motionZ;
				if (!this.entity.onGround) {
					proj.motionY += this.entity.motionY;
				}*/
				proj.setDeltaMovement(proj.getDeltaMovement().add(this.entity.getDeltaMovement()));
				proj.hasImpulse = true;
				this.entity.level.addFreshEntity(proj);
				this.entity.swing(Hand.OFF_HAND);
				this.entity.playSound(SoundEvents.SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);

				if (CQRConfig.mobs.offhandPotionsAreSingleUse) {
					stack.shrink(1);
				}

				this.prevTimeAttacked = this.entity.tickCount;
			} else if (item instanceof ItemAlchemyBag) {
				IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().get();
				int indx = this.entity.getRandom().nextInt(inventory.getSlots());
				ItemStack st = inventory.getStackInSlot(indx);
				Set<Integer> usedIDs = new HashSet<>();
				int counter = 0;
				while (st.isEmpty() && !usedIDs.contains(indx) && counter > inventory.getSlots()) {
					indx = this.entity.getRandom().nextInt(inventory.getSlots());
					usedIDs.add(indx);
					st = inventory.getStackInSlot(indx);
					counter++;
				}
				if (!st.isEmpty()) {
					ItemStack potion = st.copy();

					// Now throw it
					if (potion.getItem() instanceof SplashPotionItem || potion.getItem() instanceof LingeringPotionItem) {
						PotionEntity proj = new PotionEntity(this.world, this.entity/*, potion*/);
						proj.setItem(potion);
						y -= proj.getY();
						proj.shoot(x, y + distance * 0.08D, z, 1.F, this.entity.getRandom().nextFloat() * 0.25F);
						/*proj.motionX += this.entity.motionX;
						proj.motionZ += this.entity.motionZ;
						if (!this.entity.onGround) {
							proj.motionY += this.entity.motionY;
						}*/
						proj.setDeltaMovement(proj.getDeltaMovement().add(this.entity.getDeltaMovement()));
						proj.hasImpulse = true;
						this.entity.level.addFreshEntity(proj);
						this.entity.swing(Hand.OFF_HAND);
						this.entity.playSound(SoundEvents.SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}
				}

				this.prevTimeAttacked = this.entity.tickCount;
			}
		}
	}

	@Override
	protected int getAttackCooldown() {
		switch (this.world.getDifficulty()) {
		case HARD:
			return 20;
		case NORMAL:
			return 30;
		default:
			return 40;
		}
	}

	@Override
	protected int getAttackChargeTicks() {
		return 0;
	}

	@Override
	protected boolean canStrafe() {
		return true;
	}

	@Override
	protected double getAttackRange() {
		return 12;
	}

}
