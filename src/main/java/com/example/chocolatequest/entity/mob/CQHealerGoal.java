package com.example.chocolatequest.entity.mob;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class CQHealerGoal<T extends PathfinderMob & ICQMob> extends Goal {
    private final T mob;
    private LivingEntity healTarget;
    private int cooldown;

    public CQHealerGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRole() != CQRoles.HEALER) {
            return false;
        }
        
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        // Look for damaged allies
        List<PathfinderMob> nearbyAllies = this.mob.level().getEntitiesOfClass(PathfinderMob.class,
                new AABB(this.mob.blockPosition()).inflate(16.0D),
                entity -> entity != this.mob && entity instanceof ICQMob && entity.getHealth() < entity.getMaxHealth());

        if (nearbyAllies.isEmpty()) {
            return false;
        }

        // Pick the one with the lowest health percentage
        this.healTarget = nearbyAllies.get(0);
        float lowestHealthPct = this.healTarget.getHealth() / this.healTarget.getMaxHealth();

        for (PathfinderMob ally : nearbyAllies) {
            float healthPct = ally.getHealth() / ally.getMaxHealth();
            if (healthPct < lowestHealthPct) {
                lowestHealthPct = healthPct;
                this.healTarget = ally;
            }
        }

        return true;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.healTarget, 1.0D);
    }

    public void tick() {
        if (this.healTarget == null || !this.healTarget.isAlive() || this.healTarget.getHealth() >= this.healTarget.getMaxHealth()) {
            return;
        }

        this.mob.getLookControl().setLookAt(this.healTarget, 30.0F, 30.0F);

        double distance = this.mob.distanceToSqr(this.healTarget);
        if (distance < 225.0D) { // Within 15 blocks
            this.mob.getNavigation().stop();
            
            // Swing arm and heal instantly
            this.mob.swing(InteractionHand.MAIN_HAND);
            
            this.healTarget.heal(4.0F); // Heal 2 hearts
            
            if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HEART, 
                    this.healTarget.getX(), this.healTarget.getY() + 1.0, this.healTarget.getZ(), 
                    7, 0.5, 0.5, 0.5, 0.1);
            }
            
            this.cooldown = 40 + this.mob.getRandom().nextInt(40); // 2-4 seconds cooldown
            this.healTarget = null; // Done healing for now
        } else {
            this.mob.getNavigation().moveTo(this.healTarget, 1.0D);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.healTarget != null && this.healTarget.isAlive() && this.healTarget.getHealth() < this.healTarget.getMaxHealth() && this.cooldown <= 0;
    }

    @Override
    public void stop() {
        this.healTarget = null;
        this.mob.getNavigation().stop();
    }
}

