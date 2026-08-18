package com.example.chocolatequest.entity.mob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class CQFollowCommanderGoal<T extends PathfinderMob & ICQMob> extends Goal {
    private final T mob;
    private PathfinderMob commander;
    private int timeToRecalcPath;

    public CQFollowCommanderGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRole() == CQRoles.COMMANDER) {
            return false; // Commanders don't follow other commanders
        }

        // Only follow if not currently fighting
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
            return false;
        }

        List<PathfinderMob> nearbyMobs = this.mob.level().getEntitiesOfClass(PathfinderMob.class,
                new AABB(this.mob.blockPosition()).inflate(16.0D),
                entity -> entity instanceof ICQMob && ((ICQMob) entity).getRole() == CQRoles.COMMANDER);

        if (nearbyMobs.isEmpty()) {
            return false;
        }

        this.commander = nearbyMobs.get(0);
        return true;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void tick() {
        if (this.commander != null && this.commander.isAlive()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                if (this.mob.distanceToSqr(this.commander) > 25.0D) { // Keep some distance, don't overlap completely
                    this.mob.getNavigation().moveTo(this.commander, 1.0D);
                } else {
                    this.mob.getNavigation().stop();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.commander != null && this.commander.isAlive() && this.mob.getTarget() == null && this.mob.distanceToSqr(this.commander) > 16.0D;
    }

    @Override
    public void stop() {
        this.commander = null;
        this.mob.getNavigation().stop();
    }
}
