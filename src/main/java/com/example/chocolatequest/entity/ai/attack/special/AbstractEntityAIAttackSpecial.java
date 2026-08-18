package com.example.chocolatequest.entity.ai.attack.special;

import net.minecraft.world.entity.LivingEntity;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import java.util.Random;

public abstract class AbstractEntityAIAttackSpecial {

    protected final Random random = new Random();
    protected boolean needsTargetToContinue;
    protected boolean needsSightToContinue;
    protected final int maxUseTime;
    protected final int cooldown;

    protected AbstractEntityAIAttackSpecial(boolean needsTargetToContinue, boolean needsSightToContinue, int maxUseTime, int cooldown) {
        this.needsTargetToContinue = needsTargetToContinue;
        this.needsSightToContinue = needsSightToContinue;
        this.maxUseTime = maxUseTime;
        this.cooldown = cooldown;
    }

    public abstract boolean shouldStartAttack(AbstractEntityCQR attacker, LivingEntity target);

    public abstract boolean shouldContinueAttack(AbstractEntityCQR attacker, LivingEntity target);

    public abstract boolean isInterruptible(AbstractEntityCQR entity);

    public abstract void startAttack(AbstractEntityCQR attacker, LivingEntity target);

    public abstract void continueAttack(AbstractEntityCQR attacker, LivingEntity target, int tick);

    public abstract void stopAttack(AbstractEntityCQR attacker, LivingEntity target);

    public abstract void resetAttack(AbstractEntityCQR attacker);

    public double getAttackRange(AbstractEntityCQR attacker, LivingEntity target) {
        return attacker.getBoundingBox().inflate(1.0).distanceToSqr(target.position()); // fallback for now
    }

    public double getAttackChance(AbstractEntityCQR attacker, LivingEntity target) {
        return 0.1D;
    }

    public boolean needsTargetToContinue() {
        return this.needsTargetToContinue;
    }

    public boolean needsSightToContinue() {
        return this.needsSightToContinue;
    }

    public int getMaxUseTime() {
        return this.maxUseTime;
    }

    public int getCooldown(AbstractEntityCQR attacker) {
        return this.cooldown;
    }

}
