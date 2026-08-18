package com.example.chocolatequest.entity.ai.attack.special;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

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
        ItemStack itemStackOff = attacker.getOffhandItem();

        return this.doesItemStackFitForSpinAttack(itemStackMain) && this.doesItemStackFitForSpinAttack(itemStackOff);
    }

    protected boolean doesItemStackFitForSpinAttack(final ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.getItem() == Items.AIR) {
            return false;
        }
        final Item item = stack.getItem();
        if (item instanceof ShieldItem) {
            return false;
        }
        return item instanceof SwordItem || item instanceof AxeItem;
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
        if (target != null) {
            this.attackDirection = target.position().subtract(attacker.position()).normalize().scale(0.25);
            this.attackDirection = this.attackDirection.subtract(0, this.attackDirection.y, 0);
        }
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
        attacker.setDeltaMovement(deltaMovement);
        attacker.hasImpulse = true;

        // First: Damage all entities around us
        final double radius = 1.5 * 1.0; // attacker.getSizeVariation() is 1.0 for now
        List<Entity> affectedEntities = attacker.level().getEntities(attacker, attacker.getBoundingBox().inflate(radius), entity -> {
            if (!(entity instanceof LivingEntity)) return false;
            if (entity.isAlliedTo(attacker)) return false;
            if (entity.getType() == attacker.getType()) return false;
            return true;
        });
        
        affectedEntities.forEach((Entity entity) -> {
            if (entity == null) {
                return;
            }
            if (attacker.distanceTo(entity) > radius) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;

                float dmg = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
                
                final float knockbackStrength = 0.6125F;
                living.hurt(attacker.damageSources().mobAttack(attacker), dmg);
                living.knockback(knockbackStrength, attacker.getX() - living.getX(), attacker.getZ() - living.getZ());
            }
        });
    }

    @Override
    public void stopAttack(AbstractEntityCQR attacker, LivingEntity target) {
        attacker.setSpinToWin(false);
        this.ticksCollided = 0;
    }

    @Override
    public void resetAttack(AbstractEntityCQR attacker) {
        attacker.setSpinToWin(false);
        this.ticksCollided = 0;
    }

}
