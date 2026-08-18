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

public class PoisonProjectileEntity extends ThrowableItemProjectile {

    public PoisonProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public PoisonProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.POISON_PROJECTILE.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PROJECTILE_POISON.get();
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target) {
            if (this.getOwner() instanceof LivingEntity shooter && com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(shooter, target)) return;
        }
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(this.damageSources().magic(), 1.0F); // 0.5 hearts damage
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1)); // Poison II for 5s
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

