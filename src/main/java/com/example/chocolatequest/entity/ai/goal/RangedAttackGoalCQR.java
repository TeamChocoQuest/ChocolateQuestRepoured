package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;

import java.util.EnumSet;

public class RangedAttackGoalCQR extends Goal {
    protected final AbstractEntityCQR entity;
    protected int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private double speedModifier = 1.0D;
    private final float attackRadiusSqr = 225.0f; // 15 blocks

    public RangedAttackGoalCQR(AbstractEntityCQR entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!isRangedWeapon(this.entity.getMainHandItem().getItem()) && !isRangedWeapon(this.entity.getOffhandItem().getItem())) {
            return false;
        }
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.entity.getNavigation().isDone());
    }

    @Override
    public void start() {
        this.entity.setAggressive(true);
        if (this.entity.getItemInHand(getRangedHand()).getItem() instanceof BowItem) {
            this.speedModifier = 1.15D;
        } else {
            this.speedModifier = 1.0D;
        }
    }

    @Override
    public void stop() {
        this.entity.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.stopUsingItem();
    }

    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            double distSq = this.entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean canSee = this.entity.getSensing().hasLineOfSight(target);
            boolean wasSeeing = this.seeTime > 0;
            if (canSee != wasSeeing) {
                this.seeTime = 0;
            }
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            boolean isFireStaff = this.entity.getItemInHand(getRangedHand()).getItem() instanceof com.example.chocolatequest.item.staff.FireStaffItem;
            double actualAttackRadiusSqr = isFireStaff ? 36.0D : (double)this.attackRadiusSqr;

            if (!(distSq > actualAttackRadiusSqr) && this.seeTime >= 20) {
                if (!isFireStaff) {
                    this.entity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.entity.getNavigation().stop();
                    this.strafingTime = -1;
                }
            } else {
                this.entity.getNavigation().moveTo(target, this.speedModifier);
                this.strafingTime = -1;
            }

            if (!isFireStaff) {
                if (this.strafingTime >= 20) {
                    if ((double)this.entity.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }
                    if ((double)this.entity.getRandom().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }
                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (distSq > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (distSq < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }
                    this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.entity.lookAt(target, 30.0F, 30.0F);
                } else {
                    this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                }
            } else {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (this.entity.isUsingItem()) {
                if (!canSee && this.seeTime < -60) {
                    this.entity.stopUsingItem();
                } else if (canSee) {
                    int i = this.entity.getTicksUsingItem();
                    
                    if (isFireStaff) {
                        if (distSq > actualAttackRadiusSqr) {
                            this.entity.stopUsingItem();
                            this.attackTime = 0;
                        } else if (i >= 60) { // Fire staff shoots for 3 seconds before taking a break
                            this.entity.stopUsingItem();
                            this.attackTime = 20; // 1 second break
                        }
                    } else if (i >= 20) {
                        this.entity.stopUsingItem();
                        InteractionHand hand = getRangedHand();
                        this.entity.swing(hand);
                        Item itemInHand = this.entity.getItemInHand(hand).getItem();
                        if(itemInHand instanceof BowItem || itemInHand instanceof CrossbowItem) {
                            this.entity.performRangedAttack(target, 1.0F);
                        } else if (itemInHand instanceof com.example.chocolatequest.item.IRangedWeapon ranged) {
                            ranged.shoot(this.entity.level(), this.entity, target, hand);
                        }
                        this.attackTime = 40; 
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60 && (!isFireStaff || distSq <= actualAttackRadiusSqr)) {
                this.entity.startUsingItem(getRangedHand());
            }
        }
    }

    private boolean isRangedWeapon(Item item) {
        return item instanceof BowItem || item instanceof CrossbowItem || item instanceof com.example.chocolatequest.item.IRangedWeapon || item.getClass().getSimpleName().contains("StaffItem");
    }

    private InteractionHand getRangedHand() {
        if (isRangedWeapon(this.entity.getMainHandItem().getItem())) {
            return InteractionHand.MAIN_HAND;
        } else if (isRangedWeapon(this.entity.getOffhandItem().getItem())) {
            return InteractionHand.OFF_HAND;
        }
        return InteractionHand.MAIN_HAND;
    }
}
