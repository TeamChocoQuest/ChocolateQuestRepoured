package com.example.chocolatequest.entity.ai.boss.piratecaptain;

import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class BossAIPirateSummonParrot extends AbstractCQREntityAI<PirateCaptainEntity> {

    private int activeTicks = 0;

    public BossAIPirateSummonParrot(PirateCaptainEntity entity) {
        super(entity);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK)); // Block movement while summoning
    }

    @Override
    public boolean canUse() {
        return !this.entity.hasSpawnedParrot() && this.entity.getTarget() != null;
    }

    @Override
    public void start() {
        this.activeTicks = 20;
        this.entity.setIsSummoning(true);
        this.entity.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return this.activeTicks > 0 && this.entity.isSummoning(); // If the animation handler sets it to false, we stop
    }

    @Override
    public void tick() {
        this.activeTicks--;
        
        // Forcibly stop movement every tick
        this.entity.getNavigation().stop();
        this.entity.setDeltaMovement(0, this.entity.getDeltaMovement().y, 0); // Root to ground
        
        if (this.entity.getTarget() != null) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30.0F, 30.0F);
        }
        
        if (this.activeTicks == 14) {
            this.entity.spawnParrot();
        }
    }

    @Override
    public void stop() {
        this.entity.setIsSummoning(false);
        // Fallback: If the animation keyframe didn't fire (e.g. animation missing), force spawn it anyway so we don't get stuck in a loop
        if (!this.entity.hasSpawnedParrot()) {
            this.entity.setSpawnedParrot(true); // At least mark as spawned to break the loop
        }
    }
}
