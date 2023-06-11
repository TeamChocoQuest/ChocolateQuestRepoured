package team.cqr.cqrepoured.entity.ai.attack.special;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.item.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.IRangedWeapon;
import team.cqr.cqrepoured.item.sword.ItemDagger;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;

import java.util.List;

public class EntityAIAttackSpecialSpinAttack extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialSpinAttack() {
		super(false, false, ATTACK_DURATION, COOLDOWN_BASE);
	}

	protected static final int COOLDOWN_BASE = 50;
	protected static final int ATTACK_DURATION = 200;
	protected static final float MAX_DISTANCE_TO_TARGET = 12;

	protected Vec3 attackDirection = Vec3.ZERO;
	protected short ticksCollided = 0;
	protected boolean targetWasNullInLastCycle = false;

	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, LivingEntity target) {
		if (!attacker.canUseSpinToWinAttack()) {
			return false;
		}
		ItemStack itemStackMain = attacker.getMainHandItem();
		ItemStack itemStackOff = attacker.getMainHandItem();

		return this.doesItemStackFitForSpinAttack(itemStackMain) && this.doesItemStackFitForSpinAttack(itemStackOff);
	}

	protected boolean doesItemStackFitForSpinAttack(final ItemStack stack) {
		if (stack == null || stack.isEmpty() || stack.getItem() == Items.AIR) {
			return false;
		}
		final Item item = stack.getItem();
		if (item instanceof ShieldItem || item instanceof IRangedWeapon || item instanceof ItemGreatSword) {
			return false;
		}

		return item instanceof SwordItem || item instanceof ItemDagger || item instanceof AxeItem;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, LivingEntity target) {
		return (target == null || attacker.distanceTo(target) <= MAX_DISTANCE_TO_TARGET) && (!attacker.horizontalCollision || (this.ticksCollided < 20));
	}

	@Override
	public boolean isInterruptible(AbstractEntityCQR entity) {
		return false;
	}

	@Override
	public void startAttack(AbstractEntityCQR attacker, LivingEntity target) {
		attacker.setSpinToWin(true);
		this.calcAttackDirection(attacker, target);
	}

	private void calcAttackDirection(AbstractEntityCQR attacker, LivingEntity target) {
		this.attackDirection = target.position().subtract(attacker.position()).normalize().scale(0.25);
		this.attackDirection = this.attackDirection.subtract(0, this.attackDirection.y, 0);
	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, LivingEntity target, int tick) {
		final boolean oldTargetWasNull = this.targetWasNullInLastCycle;
		this.targetWasNullInLastCycle = target == null || target.isDeadOrDying();
		if (attacker.horizontalCollision) {
			this.ticksCollided++;
		} else {
			this.ticksCollided = 0;
		}

		if (!this.targetWasNullInLastCycle && oldTargetWasNull != this.targetWasNullInLastCycle) {
			this.calcAttackDirection(attacker, target);
		}

		Vec3 deltaMovement = new Vec3(this.attackDirection.x, attacker.getDeltaMovement().y(), this.attackDirection.z);
		/*attacker.motionX = this.attackDirection.x;
		attacker.motionZ = this.attackDirection.z;
		atacker.velocityChanged = true;*/
		attacker.setDeltaMovement(deltaMovement);
		attacker.hasImpulse = true;

		// First: Damage all entities around us
		final double radius = 1.5 * attacker.getSizeVariation();
		List<Entity> affectedEntities = attacker.getWorld().getEntities(attacker, attacker.getBoundingBox().inflate(radius), TargetUtil.createPredicateNonAlly(attacker.getFaction()));
		affectedEntities.forEach((Entity entity) -> {
			if (entity == null) {
				return;
			}
			if (entity instanceof PartEntity) {
				return;
			}
			if (attacker.distanceTo(entity) > radius) {
				return;
			}
			if (entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) entity;

				float dmg = (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				dmg += 0.75 * EnchantmentHelper.getDamageBonus(attacker.getOffhandItem(), living.getMobType());
				dmg += 0.75 * EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), living.getMobType());

				/*
				 * living.attackEntityFrom(DamageSource.causeThornsDamage(attacker), dmg);
				 * final Vec3d v = living.getPositionVector().subtract(attacker.getPositionVector()).normalize().scale(1.25).add(0,
				 * 0.25, 0).scale(attacker.getSizeVariation()).add(attackDirection);
				 * living.motionX += v.x;
				 * living.motionY += v.y;
				 * living.motionZ += v.z;
				 * living.velocityChanged = true;
				 */

				final float knockbackStrength = 0.6125F * attacker.getSizeVariation();
				living.hurt(DamageSource.mobAttack(attacker), dmg);
				living.knockback(/*entity,*/ knockbackStrength, 1, 1);//Correct replacement?
			}
		});
	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, LivingEntity target) {

	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {
		attacker.setSpinToWin(false);
		this.ticksCollided = 0;
	}

}
