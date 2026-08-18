package com.example.chocolatequest.entity.ai;

import com.example.chocolatequest.entity.FactionMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class HealAlliesGoal extends Goal {
    private final Mob mob;
    private FactionMob targetAlly;
    private int cooldown = 0;
    private int pathUpdateCountdown = 0;
    private final double speedModifier;
    private final float healAmount;

    public HealAlliesGoal(Mob mob, double speedModifier, float healAmount) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.healAmount = healAmount;
        // This goal takes priority over movement and looking
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        // Find allies in a 10 block radius that need healing
        List<FactionMob> allies = this.mob.level().getEntitiesOfClass(FactionMob.class, this.mob.getBoundingBox().inflate(10.0D, 3.0D, 10.0D), 
                ally -> ally != this.mob && ally.getHealth() < ally.getMaxHealth() && ally.isAlive());

        if (allies.isEmpty()) {
            return false;
        }

        this.targetAlly = allies.get(0);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetAlly != null 
                && this.targetAlly.isAlive() 
                && this.targetAlly.getHealth() < this.targetAlly.getMaxHealth();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetAlly, this.speedModifier);
        this.pathUpdateCountdown = 0;
    }

    @Override
    public void stop() {
        this.targetAlly = null;
        this.mob.getNavigation().stop();
    }

    public void tick() {
        if (this.targetAlly == null) return;
        
        this.mob.getLookControl().setLookAt(this.targetAlly, 30.0F, 30.0F);
        
        // Recalculate path periodically so we don't just run to the old position
        if (--this.pathUpdateCountdown <= 0) {
            this.pathUpdateCountdown = 10; // Update twice a second
            this.mob.getNavigation().moveTo(this.targetAlly, this.speedModifier);
        }
        
        // Distance squared < 4 means we are within 2 blocks
        if (this.mob.distanceToSqr(this.targetAlly) < 4.0D) {
            this.targetAlly.heal(this.healAmount);
            
            // Add visual heart particles
            if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HEART, 
                        this.targetAlly.getX(), this.targetAlly.getY() + this.targetAlly.getBbHeight() + 0.5D, this.targetAlly.getZ(), 
                        5, 0.3D, 0.3D, 0.3D, 0.0D);
            }
            
            this.cooldown = 40; // 2 seconds before healing again
            this.targetAlly = null; // Forces stop() and recalculation
        }
    }
}
