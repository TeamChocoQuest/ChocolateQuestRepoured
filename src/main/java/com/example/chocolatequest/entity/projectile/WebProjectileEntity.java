package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class WebProjectileEntity extends ThrowableItemProjectile {

    public WebProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public WebProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.WEB_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.COBWEB;
    }

    public void tick() {
        if (!this.level().isClientSide() && !this.level().hasChunkAt(this.blockPosition())) {
            this.discard();
            return;
        }
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 2 == 0) {
            serverLevel.sendParticles(ParticleTypes.ITEM_COBWEB, this.getX(), this.getY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target) {
            if (this.getOwner() instanceof LivingEntity shooter && com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(shooter, target)) {
                return;
            }
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            
            BlockPos pos = target.blockPosition();
            if (this.level().getBlockState(pos).canBeReplaced()) {
                this.level().setBlockAndUpdate(pos, com.example.chocolatequest.registry.ModBlocks.POISONOUS_WEB.get().defaultBlockState());
            }
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos().relative(result.getDirection());
        if (this.level().getBlockState(pos).canBeReplaced()) {
            this.level().setBlockAndUpdate(pos, com.example.chocolatequest.registry.ModBlocks.POISONOUS_WEB.get().defaultBlockState());
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
