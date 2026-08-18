package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BubbleTrapEntity extends Entity implements ItemSupplier {

    public BubbleTrapEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 4 == 0) {
            serverLevel.sendParticles(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 1, 0.5, 0.5, 0.5, 0.0);
        }

        if (!this.level().isClientSide()) {
            this.setDeltaMovement(0, 0.1, 0); // Float up
            this.setPos(this.getX(), this.getY() + this.getDeltaMovement().y, this.getZ());
            
            if (this.tickCount > 70) {
                this.discard();
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.PROJECTILE_BUBBLE.get());
    }
}
