package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DarkProjectileEntity extends ThrowableItemProjectile {

    public DarkProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public DarkProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.DARK_PROJECTILE.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PROJECTILE_DARK.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SQUID_INK, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(this.damageSources().magic(), 4.0F); // 2 hearts damage
            
            // Spawn returning heart to heal the shooter
            if (this.getOwner() instanceof LivingEntity shooter && !this.level().isClientSide()) {
                FlyingHeartEntity heart = new FlyingHeartEntity(this.level(), this.getX(), this.getY(), this.getZ(), shooter);
                this.level().addFreshEntity(heart);
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

