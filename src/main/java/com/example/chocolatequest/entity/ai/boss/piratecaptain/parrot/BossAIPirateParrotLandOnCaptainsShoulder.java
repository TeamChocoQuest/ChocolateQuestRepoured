package com.example.chocolatequest.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import com.example.chocolatequest.entity.boss.PirateParrotEntity;

public class BossAIPirateParrotLandOnCaptainsShoulder extends Goal {

    private final PirateParrotEntity entity;
    private LivingEntity owner;
    private boolean isSittingOnShoulder;

    public BossAIPirateParrotLandOnCaptainsShoulder(PirateParrotEntity parrot) {
        this.entity = parrot;
    }

    @Override
    public boolean canUse() {
        LivingEntity entitylivingbase = this.entity.getOwner();
        boolean flag = entitylivingbase != null && !entitylivingbase.isInWater();
        return !this.entity.isOrderedToSit() && flag && this.entity.canSitOnShoulder();
    }

    @Override
    public boolean isInterruptable() {
        return !this.isSittingOnShoulder;
    }

    @Override
    public void start() {
        this.owner = this.entity.getOwner();
        this.isSittingOnShoulder = false;
    }

    public void tick() {
        if (!this.isSittingOnShoulder && !this.entity.isOrderedToSit() && !this.entity.isLeashed()) {
            if (this.entity.getBoundingBox().intersects(this.owner.getBoundingBox())) {
                this.isSittingOnShoulder = this.entity.startRiding(this.owner, true);
            }
        }
    }
}
