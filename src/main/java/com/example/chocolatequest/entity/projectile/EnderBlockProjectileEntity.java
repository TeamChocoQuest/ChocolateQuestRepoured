package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EnderBlockProjectileEntity extends ThrowableItemProjectile {
    public EnderBlockProjectileEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public EnderBlockProjectileEntity(Level level, LivingEntity owner) {
        super(ModEntities.ENDER_BLOCK_PROJECTILE.get(), owner, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.END_STONE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.END_STONE.defaultBlockState()),
                    this.getX(), this.getY(), this.getZ(), 2, 0.15D, 0.15D, 0.15D, 0.02D);
        }
        if (!this.level().isClientSide && this.tickCount > 100) this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target && result.getEntity() != this.getOwner()) {
            if (this.getOwner() instanceof LivingEntity owner) {
                target.hurt(this.damageSources().mobProjectile(this, owner), 7.0F);
            } else {
                target.hurt(this.damageSources().magic(), 7.0F);
            }
            target.knockback(1.0D, this.getX() - target.getX(), this.getZ() - target.getZ());
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) this.discard();
    }
}
