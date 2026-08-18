package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.ShelobEntity;
import com.example.chocolatequest.entity.projectile.PoisonProjectileEntity;
import com.example.chocolatequest.entity.projectile.WebProjectileEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ShelobWebshotGoal extends Goal {
    private final ShelobEntity entity;
    private static final int MIN_WEBS = 3;
    private static final int MAX_WEBS = 7;
    private static final int MIN_COOLDOWN = 40;
    private static final int MAX_COOLDOWN = 80;
    private static final double MAX_DISTANCE_SQ = 20 * 20;
    private static final double SPEED_MULTIPLIER = 1.3;
    
    private int cooldown = 100;

    public ShelobWebshotGoal(ShelobEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.noneOf(Goal.Flag.class));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceToSqr(target) < MAX_DISTANCE_SQ;
    }

    private int animTick = 0;

    @Override
    public void start() {
        this.animTick = 20; 
        
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            boolean isMoving = this.entity.getDeltaMovement().horizontalDistanceSqr() > 0.001 || !this.entity.getNavigation().isDone();
            if (isMoving) {
                this.entity.triggerAnim("action_controller", "walk_shoot");
            } else {
                this.entity.triggerAnim("action_controller", "shoot");
            }
            this.cooldown = MIN_COOLDOWN + this.entity.getRandom().nextInt(MAX_COOLDOWN - MIN_COOLDOWN + 1);
        }
    }

    public void tick() {
        if (this.animTick == 10) { 
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                int projCount = MIN_WEBS + this.entity.getRandom().nextInt(MAX_WEBS - MIN_WEBS + 1);
                double angle = 180.0 / projCount;
                Vec3 v = target.position().subtract(this.entity.position()).normalize();
                
                for (int i = -(projCount / 2); i <= (projCount / 2); i++) {
                    Vec3 velo = v.yRot((float) Math.toRadians(i * angle));
                    velo = velo.add(0, 0.1, 0);

                    net.minecraft.world.entity.projectile.ThrowableItemProjectile proj;
                    if (this.entity.getRandom().nextDouble() > 0.8) {
                        proj = new PoisonProjectileEntity(this.entity.level(), this.entity);
                    } else {
                        proj = new WebProjectileEntity(this.entity.level(), this.entity);
                    }
                    
                    proj.setPos(this.entity.getX(), this.entity.getEyeY(), this.entity.getZ());
                    proj.setDeltaMovement(velo.scale(SPEED_MULTIPLIER));
                    this.entity.level().addFreshEntity(proj);
                }
            }
        }
        
        this.animTick--;
    }

    @Override
    public boolean canContinueToUse() {
        return this.animTick > 0;
    }
}
