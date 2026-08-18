package com.example.chocolatequest.entity.ai.boss;

import com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import java.util.EnumSet;
import java.util.List;

public class MageLifeDrainGoal extends Goal {
    private final AbstractEntityCQRMageBase mage;
    private int cooldown;
    private LivingEntity targetMinion;

    public MageLifeDrainGoal(AbstractEntityCQRMageBase mage) {
        this.mage = mage;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        // Only drain if below 50% max HP
        if (this.mage.getHealth() >= this.mage.getMaxHealth() * 0.5F) {
            return false;
        }

        // Find a suitable minion
        List<AbstractEntityCQR> minions = this.mage.level().getEntitiesOfClass(
            AbstractEntityCQR.class, 
            this.mage.getBoundingBox().inflate(15.0D),
            entity -> entity.isAlive() && entity != this.mage && entity.getDefaultFaction() == this.mage.getDefaultFaction()
        );

        if (minions.isEmpty()) {
            return false;
        }

        // Sort by distance to find the closest minion
        minions.sort((e1, e2) -> Double.compare(this.mage.distanceToSqr(e1), this.mage.distanceToSqr(e2)));
        this.targetMinion = minions.get(0);
        
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Instant cast goal
    }

    @Override
    public void start() {
        if (this.targetMinion != null && this.targetMinion.isAlive()) {
            // Visuals at minion
            if (this.mage.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL, 
                    this.targetMinion.getX(), this.targetMinion.getY() + 1.0, this.targetMinion.getZ(), 
                    20, 0.5, 0.5, 0.5, 0.1);
                    
                serverLevel.sendParticles(ParticleTypes.WITCH, 
                    this.mage.getX(), this.mage.getY() + 1.0, this.mage.getZ(), 
                    30, 0.5, 1.0, 0.5, 0.1);
                    
                serverLevel.playSound(null, this.mage.getX(), this.mage.getY(), this.mage.getZ(), 
                    SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 1.5F); // High pitched wither sound
            }

            // Kill minion
            DamageSource source = this.mage.damageSources().magic();
            this.targetMinion.hurt(source, this.targetMinion.getMaxHealth() * 10.0F); // Guaranteed kill

            // Heal boss by 15% Max HP
            float healAmount = this.mage.getMaxHealth() * 0.15F;
            this.mage.heal(healAmount);

            // Set cooldown to 15 seconds (300 ticks)
            this.cooldown = 300;
        }
    }
}
