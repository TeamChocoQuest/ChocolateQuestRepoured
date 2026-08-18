package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.ShelobEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ShelobHookGoal extends Goal {
    private final ShelobEntity entity;
    private static final double MIN_RANGE_SQ = 9 * 9;
    private static final double MAX_RANGE_SQ = 24 * 24;
    private static final int MAX_COOLDOWN = 60;
    private int cooldown = 0;
    
    private int pullTicks = 0;
    private LivingEntity hookedTarget = null;

    public ShelobHookGoal(ShelobEntity entity) {
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
        if (target != null && target.isAlive()) {
            double distSq = this.entity.distanceToSqr(target);
            if (distSq >= MIN_RANGE_SQ && distSq <= MAX_RANGE_SQ && this.entity.getSensing().hasLineOfSight(target)) {
                return true;
            }
        }
        return false;
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
            this.cooldown = MAX_COOLDOWN + this.entity.getRandom().nextInt(20);
        }
    }

    public void tick() {
        if (this.animTick == 10) { 
            LivingEntity target = this.entity.getTarget();
            if (target != null) {
                com.example.chocolatequest.entity.projectile.ProjectileSpiderHook proj = new com.example.chocolatequest.entity.projectile.ProjectileSpiderHook(this.entity.level());
                proj.setOwner(this.entity);
                double dirX = target.getX() - this.entity.getX();
                double dirY = (target.getY() + target.getEyeHeight() * 0.5D) - (this.entity.getY() + this.entity.getEyeHeight());
                double dirZ = target.getZ() - this.entity.getZ();
                proj.shootHook(this.entity, dirX, dirY, dirZ, 30.0D, 1.5D);
                
                this.entity.level().addFreshEntity(proj);
            }
        }
        
        this.animTick--;
    }

    @Override
    public boolean canContinueToUse() {
        return this.animTick > 0;
    }
}
