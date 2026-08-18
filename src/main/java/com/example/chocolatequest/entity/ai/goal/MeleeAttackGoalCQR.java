package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ShieldItem;

public class MeleeAttackGoalCQR extends net.minecraft.world.entity.ai.goal.MeleeAttackGoal {
    protected final AbstractEntityCQR entity;
    protected int blockTick;

    public MeleeAttackGoalCQR(AbstractEntityCQR entity) {
        super(entity, 1.0D, false);
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (isRangedWeapon(this.entity.getMainHandItem().getItem())) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (isRangedWeapon(this.entity.getMainHandItem().getItem())) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.blockTick = 0;
    }

    public void tick() {
        super.tick();
        this.checkAndPerformBlock();
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.stopUsingItem();
    }

    protected void checkAndPerformBlock() {
        if (this.blockTick + getBlockCooldownPeriod() <= this.entity.tickCount && !this.entity.isBlocking()) {
            if (this.entity.getOffhandItem().getItem() instanceof ShieldItem) {
                this.entity.startUsingItem(InteractionHand.OFF_HAND);
            }
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target)) {
            this.resetAttackCooldown();
            this.blockTick = this.entity.tickCount;
            if (this.entity.isBlocking()) {
                this.entity.stopUsingItem();
            }
            this.entity.swing(InteractionHand.MAIN_HAND);
            this.entity.doHurtTarget(target);
        }
    }
    
    @Override
    protected boolean canPerformAttack(LivingEntity target) {
        return this.isTimeToAttack() && this.entity.isWithinMeleeAttackRange(target) && this.entity.getSensing().hasLineOfSight(target);
    }

    @Override
    protected int getTicksUntilNextAttack() {
        return (int) this.getAttackCooldownPeriod();
    }

    @Override
    protected int getAttackInterval() {
        return (int) this.getAttackCooldownPeriod();
    }

    public float getAttackCooldownPeriod() {
        double attackSpeed = this.entity.getAttributeValue(Attributes.ATTACK_SPEED);
        if (attackSpeed <= 0) attackSpeed = 4.0;
        return (float) (20.0D / attackSpeed);
    }

    public int getBlockCooldownPeriod() {
        return 30;
    }

    private boolean isRangedWeapon(net.minecraft.world.item.Item item) {
        return item instanceof net.minecraft.world.item.BowItem || item instanceof net.minecraft.world.item.CrossbowItem || item.getClass().getSimpleName().contains("StaffItem");
    }
}
