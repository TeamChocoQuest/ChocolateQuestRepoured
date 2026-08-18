package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.CQBullEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BullEarthFuryGoal extends Goal {
    private final CQBullEntity bull;
    private int tickCounter;
    private int cooldown;
    private Set<Entity> hitEntities;

    public BullEarthFuryGoal(CQBullEntity bull) {
        this.bull = bull;
        this.hitEntities = new HashSet<>();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.bull.wantsToComboEarthFury()) {
            return true;
        }
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        LivingEntity target = this.bull.getTarget();
        if (target != null && target.isAlive()) {
            int chance = this.bull.isEnraged() ? 25 : 50;
            return this.bull.distanceToSqr(target) < 144.0 && this.bull.getRandom().nextInt(chance) == 0;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.tickCounter < 35; // The animation is ~32 ticks
    }

    @Override
    public void start() {
        this.tickCounter = 0;
        this.hitEntities.clear();
        this.bull.setWantsToComboEarthFury(false);
        this.bull.getNavigation().stop();
        this.bull.triggerAnim("action_controller", "earth_fury");
        
        LivingEntity target = this.bull.getTarget();
        if (target != null) {
            this.bull.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
    }

    @Override
    public void tick() {
        this.tickCounter++;
        
        if (this.tickCounter < 10) {
            LivingEntity target = this.bull.getTarget();
            if (target != null) {
                this.bull.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }

        // Tick 12 is the final adjusted moment the bull smashes the ground
        if (this.tickCounter == 12) {
            if (this.bull.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, this.bull.getX(), this.bull.getY(), this.bull.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 2.0F, 0.8F);
            }
        }

        // Expand shockwave from tick 12 to 18 (radius 1 to 7)
        if (this.tickCounter >= 12 && this.tickCounter <= 18) {
            int radius = this.tickCounter - 11;
            this.expandShockwave(radius);
            this.damageEntitiesInRadius(radius);
        }
    }

    private void expandShockwave(int radius) {
        if (this.bull.level() instanceof ServerLevel serverLevel) {
            BlockPos center = this.bull.blockPosition();
            int points = radius * 12; // More points for larger radius
            
            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI * i / points;
                double x = center.getX() + 0.5 + radius * Math.cos(angle);
                double z = center.getZ() + 0.5 + radius * Math.sin(angle);
                double y = center.getY();
                
                // Find surface block
                BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
                while(serverLevel.isEmptyBlock(pos) && pos.getY() > center.getY() - 3) {
                    pos = pos.below();
                }
                while(!serverLevel.isEmptyBlock(pos.above()) && pos.getY() < center.getY() + 3) {
                    pos = pos.above();
                }
                
                BlockState state = serverLevel.getBlockState(pos);
                if (!state.isAir()) {
                    // Block dust flying upwards!
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), x, pos.getY() + 1.2, z, 3, 0.2, 0.5, 0.2, 0.15);
                }
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, pos.getY() + 1.2, z, 1, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }

    private void damageEntitiesInRadius(int radius) {
        List<Entity> entities = this.bull.level().getEntities(this.bull, this.bull.getBoundingBox().inflate(radius + 1.0));
        
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && !this.hitEntities.contains(entity)) {
                double distance = this.bull.distanceTo(entity);
                if (distance >= radius - 1.5 && distance <= radius + 1.5) {
                    // Hit!
                    this.hitEntities.add(entity);
                    DamageSource source = this.bull.damageSources().mobAttack(this.bull);
                    
                    float damageMultiplier = (float) (1.0 - (distance / 10.0));
                    float baseDamage = (float) this.bull.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) * 2.0f;
                    entity.hurt(source, baseDamage * damageMultiplier);
                    
                    double dx = entity.getX() - this.bull.getX();
                    double dz = entity.getZ() - this.bull.getZ();
                    
                    // Normalize knockback direction
                    double length = Math.sqrt(dx * dx + dz * dz);
                    if (length > 0) {
                        dx /= length;
                        dz /= length;
                    }
                    
                    entity.setDeltaMovement(dx * 0.8, 0.8, dz * 0.8);
                }
            }
        }
    }

    @Override
    public void stop() {
        this.tickCounter = 0;
        this.cooldown = this.bull.isEnraged() ? 70 : 140; // 3.5s or 7s cooldown
        this.hitEntities.clear();
        this.bull.triggerAnim("action_controller", "stop_action");
    }
}
