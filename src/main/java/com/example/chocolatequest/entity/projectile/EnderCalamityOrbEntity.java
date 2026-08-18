package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.entity.boss.EntityCQREndermenace;
import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EnderCalamityOrbEntity extends ThrowableItemProjectile {
    private boolean reflected;

    public EnderCalamityOrbEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public EnderCalamityOrbEntity(Level level, EntityCQREndermenace owner) {
        super(ModEntities.ENDER_CALAMITY_ORB.get(), owner, level);
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_EYE;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living) {
            Entity owner = this.getOwner();
            if (owner != null && owner.isAlive()) {
                Vec3 direction = owner.getEyePosition().subtract(this.position()).normalize();
                this.setDeltaMovement(direction.scale(1.15D));
                this.hasImpulse = true;
                this.reflected = true;
                this.setOwner(living);
                this.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.5F, 1.6F);
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(),
                            2, 0.1D, 0.1D, 0.1D, 0.0D);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(this.reflected ? ParticleTypes.END_ROD : ParticleTypes.DRAGON_BREATH,
                    this.getX(), this.getY(), this.getZ(), 3, 0.16D, 0.16D, 0.16D, 0.01D);
            if (this.tickCount > 180) this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hit = result.getEntity();
        if (this.reflected && hit instanceof EntityCQREndermenace menace) {
            menace.onEnergyBallReturned();
            this.discard();
            return;
        }
        if (!this.reflected && hit instanceof LivingEntity living && hit != this.getOwner()) {
            living.hurt(this.damageSources().magic(), 10.0F);
            if (this.getOwner() instanceof EntityCQREndermenace menace) menace.onEnergyBallMissed();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            if (!this.reflected && this.getOwner() instanceof EntityCQREndermenace menace) {
                menace.onEnergyBallMissed();
            }
            this.discard();
        }
    }
}
