package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.Item;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;

public class CQRArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<String> ELEMENT_TYPE = SynchedEntityData.defineId(CQRArrowEntity.class, EntityDataSerializers.STRING);
    private String elementType = "fire";

    public CQRArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CQRArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @org.jetbrains.annotations.Nullable ItemStack firedFromWeapon) {
        super(ModEntities.CQR_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public CQRArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, @org.jetbrains.annotations.Nullable ItemStack firedFromWeapon) {
        super(ModEntities.CQR_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ELEMENT_TYPE, "fire");
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
        this.entityData.set(ELEMENT_TYPE, elementType);
    }

    public String getElementType() {
        return this.entityData.get(ELEMENT_TYPE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("ElementType", this.getElementType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ElementType")) {
            this.setElementType(compound.getString("ElementType"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            switch (this.getElementType()) {
                case "fire":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "water":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "ice":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "poison":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.WITCH, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "dark":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SQUID_INK, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "wind":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "heal":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
                case "electric":
                    this.level().addParticle(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
                    break;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level().isClientSide) return;
        
        if (result.getEntity() instanceof LivingEntity entity) {
            switch (this.getElementType()) {
                case "fire":
                    entity.setRemainingFireTicks(100);
                    break;
                case "water":
                    entity.clearFire();
                    if (entity.fireImmune()) {
                        entity.hurt(this.damageSources().drown(), 4.0F);
                    }
                    break;
                case "ice":
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                    break;
                case "poison":
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                    break;
                case "dark":
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                    break;
                case "wind":
                    entity.knockback(1.0, entity.getX() - this.getX(), entity.getZ() - this.getZ());
                    break;
                case "heal":
                    if (entity.isInvertedHealAndHarm()) {
                        entity.hurt(this.damageSources().magic(), 4.0F);
                    } else {
                        entity.heal(4.0F);
                    }
                    break;
                case "electric":
                    if (this.level() instanceof ServerLevel serverLevel) {
                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                        if (lightning != null) {
                            lightning.moveTo(entity.position());
                            serverLevel.addFreshEntity(lightning);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        if (this.getElementType() == null) {
            return new ItemStack(com.example.chocolatequest.registry.ModItems.FIRE_ARROW.get());
        }
        Item item = switch (this.getElementType()) {
            case "water" -> com.example.chocolatequest.registry.ModItems.WATER_ARROW.get();
            case "ice" -> com.example.chocolatequest.registry.ModItems.ICE_ARROW.get();
            case "poison" -> com.example.chocolatequest.registry.ModItems.POISON_ARROW.get();
            case "dark" -> com.example.chocolatequest.registry.ModItems.DARK_ARROW.get();
            case "wind" -> com.example.chocolatequest.registry.ModItems.WIND_ARROW.get();
            case "electric" -> com.example.chocolatequest.registry.ModItems.ELECTRIC_ARROW.get();
            case "heal" -> com.example.chocolatequest.registry.ModItems.HEAL_ARROW.get();
            default -> com.example.chocolatequest.registry.ModItems.FIRE_ARROW.get();
        };
        return new ItemStack(item);
    }
}
