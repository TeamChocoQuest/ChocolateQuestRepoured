package com.example.chocolatequest.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import com.example.chocolatequest.registry.ModEntities;

public class ProjectileCannonBall extends ThrowableProjectile {
    public ProjectileCannonBall(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }
    
    public ProjectileCannonBall(LivingEntity shooter, Level level) {
        super(ModEntities.PROJECTILE_CANNON_BALL.get(), shooter, level);
    }
    public ProjectileCannonBall(LivingEntity shooter, Level level, boolean fast) {
        super(ModEntities.PROJECTILE_CANNON_BALL.get(), shooter, level);
    }
    public ProjectileCannonBall(net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.projectile.ThrowableProjectile> type, LivingEntity shooter, Level level) {

        super(ModEntities.PROJECTILE_CANNON_BALL.get(), shooter, level);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
			// Combat projectile: damage and knockback entities, but do not chew holes through dungeons.
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }
}
