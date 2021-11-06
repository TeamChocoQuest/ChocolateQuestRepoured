package team.cqr.cqrepoured.objects.entity.ai.attack.special;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.swords.ItemDagger;
import team.cqr.cqrepoured.objects.items.swords.ItemGreatSword;
import team.cqr.cqrepoured.util.IRangedWeapon;

public class EntityAIAttackSpecialSpinAttack extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialSpinAttack() {
		super(false, false, ATTACK_DURATION, COOLDOWN_BASE);
	}

	protected static final int COOLDOWN_BASE = 50;
	protected static final int ATTACK_DURATION = 200;
	protected static final float MAX_DISTANCE_TO_TARGET = 12;
	
	protected Vec3d attackDirection = Vec3d.ZERO;
	protected short ticksCollided = 0;
	protected boolean targetWasNullInLastCycle = false;
	
	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		if(!attacker.canUseSpinToWinAttack()) {
			return false;
		}
		ItemStack itemStackMain = attacker.getHeldItemMainhand();
		ItemStack itemStackOff = attacker.getHeldItemOffhand();
		
		return doesItemStackFitForSpinAttack(itemStackMain) && doesItemStackFitForSpinAttack(itemStackOff);
	}

	protected boolean doesItemStackFitForSpinAttack(final ItemStack stack) {
		if (stack == null || stack.isEmpty() || stack.getItem() == Items.AIR) {
			return false;
		}
		final Item item = stack.getItem();
		if (item instanceof ItemShield || item instanceof IRangedWeapon || item instanceof ItemGreatSword) {
			return false;
		}

		return item instanceof ItemSword || item instanceof ItemDagger || item instanceof ItemAxe;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		return (target == null ||attacker.getDistance(target) <= MAX_DISTANCE_TO_TARGET) && !(attacker.collidedHorizontally && this.ticksCollided >= 20);
	}

	@Override
	public boolean isInterruptible(AbstractEntityCQR entity) {
		return false;
	}

	@Override
	public void startAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		attacker.setSpinToWin(true);
		this.calcAttackDirection(attacker, target);
	}

	private void calcAttackDirection(AbstractEntityCQR attacker, EntityLivingBase target) {
		attackDirection = target.getPositionVector().subtract(attacker.getPositionVector()).normalize().scale(0.25);
		attackDirection = attackDirection.subtract(0, attackDirection.y, 0);
	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, EntityLivingBase target, int tick) {
		final boolean oldTargetWasNull = this.targetWasNullInLastCycle;
		targetWasNullInLastCycle = target == null || (target != null && target.isDead);
		if(attacker.collidedHorizontally) {
			this.ticksCollided++;
		} else {
			this.ticksCollided = 0;
		}
		
		if(!targetWasNullInLastCycle && oldTargetWasNull != targetWasNullInLastCycle) {
			this.calcAttackDirection(attacker, target);
		}
		
		attacker.motionX = attackDirection.x;
		attacker.motionZ = attackDirection.z;
		attacker.velocityChanged = true;
		
		//First: Damage all entities around us
		final double radius = 1.5 * attacker.getSizeVariation();
		List<Entity> affectedEntities = attacker.getEntityWorld().getEntitiesInAABBexcluding(
				attacker, 
				attacker.getEntityBoundingBox().grow(radius), 
				TargetUtil.createPredicateNonAlly(attacker.getFaction())
			);
		affectedEntities.forEach((Entity entity) -> {
			if(entity == null) {
				return;
			}
			if(entity instanceof MultiPartEntityPart) {
				return;
			}
			if(attacker.getDistance(entity) > radius) {
				return;
			}
			if(entity instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) entity;
				
				float dmg = (float) attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				dmg += 0.75 * EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), living.getCreatureAttribute());
				dmg += 0.75 * EnchantmentHelper.getModifierForCreature(attacker.getHeldItemOffhand(), living.getCreatureAttribute());
				
				living.attackEntityFrom(DamageSource.causeThornsDamage(attacker), dmg);
				final Vec3d v = living.getPositionVector().subtract(attacker.getPositionVector()).normalize().scale(1.25).add(0, 0.25, 0).scale(attacker.getSizeVariation()).add(attackDirection);
				living.motionX += v.x;
				living.motionY += v.y;
				living.motionZ += v.z;
				living.velocityChanged = true;
			}
		});
	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, EntityLivingBase target) {

	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {
		attacker.setSpinToWin(false);
		this.ticksCollided = 0;
	}

}
