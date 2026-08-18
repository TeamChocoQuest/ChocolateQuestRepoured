package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.item.EBulletType;
import com.example.chocolatequest.item.ItemBullet;
import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ProjectileBulletEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Integer> BULLET_TYPE = SynchedEntityData.defineId(ProjectileBulletEntity.class, EntityDataSerializers.INT);

    public ProjectileBulletEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public ProjectileBulletEntity(Level level, LivingEntity shooter, EBulletType type) {
        super(ModEntities.PROJECTILE_BULLET.get(), shooter, level);
        this.setNoGravity(true);
        this.entityData.set(BULLET_TYPE, type.ordinal());
        
        Item item = switch (type) {
            case GOLD -> ModItems.BULLET_GOLD.get();
            case DIAMOND -> ModItems.BULLET_DIAMOND.get();
            case FIRE -> ModItems.BULLET_FIRE.get();
            default -> ModItems.BULLET_IRON.get();
        };
        this.setItem(new net.minecraft.world.item.ItemStack(item));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BULLET_TYPE, 0);
    }

    public EBulletType getBulletType() {
        return EBulletType.values()[this.entityData.get(BULLET_TYPE)];
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BULLET_IRON.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            if (this.getBulletType().fireDamage()) {
                serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            float baseDamage = 5.0F; // Base gun damage
            float totalDamage = baseDamage + this.getBulletType().getAdditionalDamage();
            target.hurt(this.damageSources().thrown(this, this.getOwner()), totalDamage);
            
            if (this.getBulletType().fireDamage()) {
                target.igniteForSeconds(5);
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
