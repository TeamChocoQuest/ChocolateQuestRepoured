package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.sounds.SoundEvents;

public class BubbleProjectileEntity extends ThrowableItemProjectile {

    public BubbleProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public BubbleProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BUBBLE_PROJECTILE.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PROJECTILE_BUBBLE.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 1, 0.2, 0.2, 0.2, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target) {
            if (this.getOwner() instanceof LivingEntity shooter && com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(shooter, target)) return;
        }
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(this.damageSources().magic(), 1.0F); // Small impact damage
            
            if (!this.level().isClientSide()) {
                // One victim may only be carried by one bubble.  Previously every
                // projectile spawned another trap even when startRiding() failed,
                // leaving stacks of empty BubbleTrapEntity instances around the
                // player and allowing several bubbles to fight over the passenger.
                if (target.getVehicle() instanceof BubbleTrapEntity) {
                    this.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 0.65F, 1.2F);
                    return;
                }

                com.example.chocolatequest.entity.projectile.BubbleTrapEntity trap = new com.example.chocolatequest.entity.projectile.BubbleTrapEntity(ModEntities.BUBBLE_TRAP.get(), this.level());
                trap.setPos(target.getX(), target.getY(), target.getZ());
                this.level().addFreshEntity(trap);
                if (!target.startRiding(trap, true)) {
                    trap.discard();
                    return;
                }
                
                this.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F);
                
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SPLASH, target.getX(), target.getY() + 1.0, target.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
}

