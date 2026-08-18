package com.example.chocolatequest.entity.mob;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CQShieldBlockGoal<T extends PathfinderMob & ICQMob> extends Goal {
    private final T mob;
    private int blockTime;
    private int cooldownTime;

    public CQShieldBlockGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldownTime > 0) {
            this.cooldownTime--;
            return false;
        }

        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (this.mob.getOffhandItem().isEmpty() || !(this.mob.getOffhandItem().getItem() instanceof net.minecraft.world.item.ShieldItem)) {
            return false;
        }

        // Block if target is relatively close or if they have a projectile weapon
        double distance = this.mob.distanceToSqr(target);
        if (distance < 25.0D) { // Within 5 blocks
            // Randomly decide to block when close
            return this.mob.getRandom().nextInt(20) == 0;
        } else if (target.isHolding(net.minecraft.world.item.Items.BOW) || target.isHolding(net.minecraft.world.item.Items.CROSSBOW)) {
            // Block more often if target holds a bow
            return this.mob.getRandom().nextInt(10) == 0;
        }

        return false;
    }

    @Override
    public void start() {
        this.blockTime = 20 + this.mob.getRandom().nextInt(40); // Block for 1-3 seconds
        this.mob.startUsingItem(InteractionHand.OFF_HAND);
    }

    public void tick() {
        this.blockTime--;
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.blockTime > 0 && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void stop() {
        this.mob.stopUsingItem();
        this.cooldownTime = 20 + this.mob.getRandom().nextInt(40); // 1-3 seconds cooldown before blocking again
    }
}
