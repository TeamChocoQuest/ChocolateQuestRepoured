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
import net.minecraft.world.phys.Vec3;

public class FlyingHeartEntity extends ThrowableItemProjectile {

    private LivingEntity targetOwner;

    public FlyingHeartEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public FlyingHeartEntity(Level level, double x, double y, double z, LivingEntity owner) {
        super(ModEntities.FLYING_HEART.get(), x, y, z, level);
        this.targetOwner = owner;
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PROJECTILE_HEART.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();

        if (!this.level().isClientSide() && this.targetOwner != null && this.targetOwner.isAlive()) {
            // Find direction to owner
            Vec3 direction = new Vec3(
                    this.targetOwner.getX() - this.getX(),
                    this.targetOwner.getY() + this.targetOwner.getEyeHeight() / 2 - this.getY(),
                    this.targetOwner.getZ() - this.getZ()
            ).normalize();
            
            this.setDeltaMovement(direction.scale(0.8)); // Move fast towards owner

            // Hit owner logic
            if (this.distanceTo(this.targetOwner) < 1.5) {
                this.targetOwner.heal(2.0F); // Heal 1 heart
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, this.targetOwner.getX(), this.targetOwner.getY() + 1.0, this.targetOwner.getZ(), 7, 0.5, 0.5, 0.5, 0.1);
                }
                this.discard();
            }
        }
        
        // Timeout
        if (this.tickCount > 100) {
            this.discard();
        }
    }
}
