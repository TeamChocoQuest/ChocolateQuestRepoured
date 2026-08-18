package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.ShelobEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.util.Mth;
import java.util.EnumSet;

public class ShelobLeapGoal extends Goal {
    private final ShelobEntity leaper;
    private LivingEntity leapTarget;
    private final float leapMotionY = 0.8F;
    private int cooldown;

    private static final double MIN_VERTICAL_DISTANCE = 2;
    private static final double MIN_DISTANCE = 9 * 9;
    private static final double MAX_DISTANCE = 16 * 16;
    private static final int MAX_COOLDOWN = 40;

    public ShelobLeapGoal(ShelobEntity leaper) {
        this.leaper = leaper;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        this.leapTarget = this.leaper.getTarget();

        if (this.leapTarget == null) {
            return false;
        }

        double d0 = this.leaper.distanceToSqr(this.leapTarget);
        double distVert = Math.abs(this.leapTarget.getY() - this.leaper.getY());

        if ((d0 >= MIN_DISTANCE || distVert >= MIN_VERTICAL_DISTANCE) && d0 <= MAX_DISTANCE) {
            if (!this.leaper.onGround()) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.leaper.onGround();
    }

    @Override
    public void start() {
        double d0 = this.leapTarget.getX() - this.leaper.getX();
        double d1 = this.leapTarget.getZ() - this.leaper.getZ();
        float f = Mth.sqrt((float)(d0 * d0 + d1 * d1));

        this.leaper.getLookControl().setLookAt(this.leapTarget, 100F, 100F);

        double vx = this.leaper.getDeltaMovement().x;
        double vy = this.leaper.getDeltaMovement().y;
        double vz = this.leaper.getDeltaMovement().z;
        
        if (f >= 1.0E-4D) {
            vx += d0 / f * 0.8D * 1.8F + vx * 0.4F;
            vz += d1 / f * 0.8D * 1.8F + vz * 0.4F;
        }

        vy = (this.leapTarget.getY() - this.leaper.getY()) * 0.5;
        vy = Math.max(vy, this.leapMotionY * 1.3);

        this.leaper.setDeltaMovement(vx, vy, vz);
        this.leaper.hasImpulse = true;
        
        this.leaper.triggerAnim("action_controller", "jump");
    }

    @Override
    public void stop() {
        this.cooldown = MAX_COOLDOWN;
    }
}
