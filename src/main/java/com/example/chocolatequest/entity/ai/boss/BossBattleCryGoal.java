package com.example.chocolatequest.entity.ai.boss;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.EnumSet;
import java.util.List;

public class BossBattleCryGoal<T extends AbstractEntityCQR & GeoEntity> extends Goal {
    private final T boss;
    private int cooldown;
    private int castingTime;

    public BossBattleCryGoal(T boss) {
        this.boss = boss;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        // Random chance to use it when fighting
        if (this.boss.getTarget() != null && this.boss.getRandom().nextInt(50) == 0) {
            // Check if there are allies nearby
            List<AbstractEntityCQR> allies = this.boss.level().getEntitiesOfClass(
                AbstractEntityCQR.class, 
                this.boss.getBoundingBox().inflate(20.0D),
                entity -> entity.isAlive() && entity != this.boss && entity.getDefaultFaction() == this.boss.getDefaultFaction()
            );
            return !allies.isEmpty();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.castingTime > 0;
    }

    @Override
    public void start() {
        this.castingTime = 40; // 2 seconds cast
        this.boss.getNavigation().stop();
        this.boss.triggerAnim("action_controller", "battle_cry");
        
        if (this.boss.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, this.boss.getX(), this.boss.getY(), this.boss.getZ(), 
                SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 1.5F, 0.8F);
        }
    }

    @Override
    public void tick() {
        this.castingTime--;

        if (this.castingTime == 20) { // Halfway through the animation
            if (this.boss.level() instanceof ServerLevel serverLevel) {
                // Apply buffs
                List<AbstractEntityCQR> allies = this.boss.level().getEntitiesOfClass(
                    AbstractEntityCQR.class, 
                    this.boss.getBoundingBox().inflate(20.0D),
                    entity -> entity.isAlive() && entity != this.boss && entity.getDefaultFaction() == this.boss.getDefaultFaction()
                );

                for (AbstractEntityCQR ally : allies) {
                    ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1)); // Strength II for 10s
                    ally.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)); // Speed II for 10s
                    
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, 
                        ally.getX(), ally.getY() + 1.0, ally.getZ(), 
                        5, 0.5, 0.5, 0.5, 0.0);
                }
                
                // Visuals for the boss itself
                serverLevel.sendParticles(ParticleTypes.LAVA, 
                    this.boss.getX(), this.boss.getY() + 1.0, this.boss.getZ(), 
                    30, 1.0, 1.0, 1.0, 0.1);
            }
        }
    }

    @Override
    public void stop() {
        this.castingTime = 0;
        this.cooldown = 600; // 30 seconds cooldown
    }
}
