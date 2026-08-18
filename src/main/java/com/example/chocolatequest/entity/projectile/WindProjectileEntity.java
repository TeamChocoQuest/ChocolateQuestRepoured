package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class WindProjectileEntity extends ThrowableItemProjectile {

    public WindProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public WindProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.WIND_PROJECTILE.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PROJECTILE_WIND.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            
            // Massive AoE Knockback
            double radius = 4.0D;
            AABB aabb = this.getBoundingBox().inflate(radius);
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, aabb, e -> e != this.getOwner() && e.isAlive());

            for (LivingEntity target : targets) {
                if (this.getOwner() instanceof LivingEntity shooter && com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(shooter, target)) continue;
                double distance = this.distanceTo(target);
                if (distance <= radius) {
                    double dx = target.getX() - this.getX();
                    double dz = target.getZ() - this.getZ();
                    
                    target.knockback(1.5D * (1.0 - (distance / radius)), -dx, -dz);
                }
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 15, 1.0, 1.0, 1.0, 0.1);
            }

            this.discard();
        }
    }
}

