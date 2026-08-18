package com.example.chocolatequest.entity.ai.boss.piratecaptain;

import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BossAIPirateDodgeAndShoot extends Goal {
    private final PirateCaptainEntity entity;
    private int cooldown = 30;
    private int activeTicks = 0;

    public BossAIPirateDodgeAndShoot(PirateCaptainEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); // Block normal movement and looking
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        LivingEntity target = this.entity.getTarget();
        if (target != null && this.entity.distanceToSqr(target) < 36.0) { // within 6 blocks
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        this.activeTicks = 32;
        this.entity.setIsDodging(true);
        this.cooldown = 120;
        
        // Stop any current movement path
        this.entity.getNavigation().stop();
        // Ensure he is holding the revolver in his main hand during the animation
        this.entity.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.CAPTAIN_REVOLVER.get()));
        
        // Target tracking
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.activeTicks > 0;
    }

    @Override
    public void tick() {
        this.activeTicks--;
        
        // Ensure navigation is forcibly stopped every tick during the dodge
        this.entity.getNavigation().stop();
        this.entity.setDeltaMovement(0, this.entity.getDeltaMovement().y, 0); // Force him to be totally rooted to the ground
        
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            // Force body rotation towards the target
            double d0 = target.getX() - this.entity.getX();
            double d1 = target.getZ() - this.entity.getZ();
            float targetYaw = (float)(net.minecraft.util.Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.entity.setYRot(targetYaw);
            this.entity.yBodyRot = targetYaw;
            this.entity.yHeadRot = targetYaw;
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
        
        // Begin firing after a short readable wind-up, not after a long pause.
        if (this.activeTicks <= 24 && this.activeTicks >= 14 && this.activeTicks % 2 == 0) {
            this.entity.shootSingleBullet();
        }
    }

    @Override
    public void stop() {
        this.entity.setIsDodging(false);
    }
}
