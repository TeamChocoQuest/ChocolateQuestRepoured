package com.example.chocolatequest.entity.projectile;

import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;

import java.util.List;

public class CQRPotionEntity extends ThrowableItemProjectile {
    private String elementType = "fire";

    public CQRPotionEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public CQRPotionEntity(Level level, LivingEntity shooter) {
        super(ModEntities.CQR_POTION.get(), shooter, level);
    }

    public CQRPotionEntity(Level level, double x, double y, double z) {
        super(ModEntities.CQR_POTION.get(), x, y, z, level);
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementType() {
        return this.elementType;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WATER_POTION.get(); // Fallback
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            String element = resolveElementType();
            ParticleOptions particle = switch (element) {
                case "fire" -> ParticleTypes.FLAME;
                case "water" -> ParticleTypes.SPLASH;
                case "ice" -> ParticleTypes.SNOWFLAKE;
                case "poison" -> ParticleTypes.WITCH;
                case "dark" -> ParticleTypes.SQUID_INK;
                case "wind" -> ParticleTypes.CLOUD;
                case "electric" -> ParticleTypes.ELECTRIC_SPARK;
                case "heal" -> ParticleTypes.HEART;
                default -> ParticleTypes.EFFECT;
            };
            net.minecraft.world.phys.Vec3 motion = this.getDeltaMovement();
            for (int i = 0; i < 2; i++) {
                double tail = 0.2D + this.random.nextDouble() * 0.8D;
                double x = this.getX() - motion.x * tail + (this.random.nextDouble() - 0.5D) * 0.08D;
                double y = this.getY() - motion.y * tail + (this.random.nextDouble() - 0.5D) * 0.08D;
                double z = this.getZ() - motion.z * tail + (this.random.nextDouble() - 0.5D) * 0.08D;
                this.level().addParticle(particle, x, y, z,
                        -motion.x * 0.08D, 0.012D, -motion.z * 0.08D);
            }
            if (this.tickCount % 2 == 0) {
                ParticleOptions accent = switch (element) {
                    case "fire", "dark" -> ParticleTypes.SMOKE;
                    case "water" -> ParticleTypes.BUBBLE_POP;
                    case "ice" -> ParticleTypes.CLOUD;
                    case "poison" -> ParticleTypes.MYCELIUM;
                    case "wind" -> ParticleTypes.POOF;
                    case "electric" -> ParticleTypes.END_ROD;
                    case "heal" -> ParticleTypes.HAPPY_VILLAGER;
                    default -> ParticleTypes.POOF;
                };
                this.level().addParticle(accent, this.getX(), this.getY(), this.getZ(),
                        -motion.x * 0.04D, 0.01D, -motion.z * 0.04D);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;
        
        String hitElementType = resolveElementType();
        AABB bounds = this.getBoundingBox().inflate(4.0D);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, bounds);
        
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleOptions splashParticle = new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
            serverLevel.sendParticles(splashParticle, this.getX(), this.getY(), this.getZ(), 20, 0.5D, 0.5D, 0.5D, 0.1D);
            
            ParticleOptions effectParticle = switch (hitElementType) {
                case "fire" -> ParticleTypes.FLAME;
                case "water" -> ParticleTypes.BUBBLE;
                case "ice" -> ParticleTypes.SNOWFLAKE;
                case "poison" -> ParticleTypes.EFFECT;
                case "dark" -> ParticleTypes.LARGE_SMOKE;
                case "wind" -> ParticleTypes.CLOUD;
                case "electric" -> ParticleTypes.ELECTRIC_SPARK;
                case "heal" -> ParticleTypes.HEART;
                default -> ParticleTypes.EFFECT;
            };
            serverLevel.sendParticles(effectParticle, this.getX(), this.getY(), this.getZ(), 40, 1.5D, 1.5D, 1.5D, 0.1D);
        }

        for (LivingEntity entity : entities) {
            double distSq = entity.distanceToSqr(this);
            if (distSq < 16.0D) {
                switch (hitElementType) {
                    case "fire":
                        entity.setRemainingFireTicks(100);
                        entity.hurt(this.damageSources().inFire(), 4.0F);
                        break;
                    case "water":
                        entity.clearFire();
                        if (entity.fireImmune()) { // Or check for enderman/blaze
                            entity.hurt(this.damageSources().drown(), 6.0F);
                        }
                        break;
                    case "ice":
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
                        break;
                    case "poison":
                        entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                        break;
                    case "dark":
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                        entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                        break;
                    case "wind":
                        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1));
                        entity.knockback(1.0, entity.getX() - this.getX(), entity.getZ() - this.getZ());
                        break;
                    case "heal":
                        if (entity.isInvertedHealAndHarm()) {
                            entity.hurt(this.damageSources().magic(), 6.0F);
                        } else {
                            entity.heal(6.0F);
                        }
                        break;
                    case "electric":
                        // Will handle outside loop to strike once
                        break;
                }
            }
        }

        if ("electric".equals(hitElementType) && this.level() instanceof ServerLevel serverLevel) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (lightning != null) {
                lightning.moveTo(this.position());
                serverLevel.addFreshEntity(lightning);
            }
        }
        
        this.discard();
    }

    private String resolveElementType() {
        Item item = this.getItem().getItem();
        if (item == ModItems.WATER_POTION.get()) return "water";
        if (item == ModItems.ICE_POTION.get()) return "ice";
        if (item == ModItems.POISON_POTION.get()) return "poison";
        if (item == ModItems.DARK_POTION.get()) return "dark";
        if (item == ModItems.WIND_POTION.get()) return "wind";
        if (item == ModItems.ELECTRIC_POTION.get()) return "electric";
        if (item == ModItems.HEAL_POTION.get()) return "heal";
        if (item == ModItems.FIRE_POTION.get()) return "fire";
        return this.elementType;
    }
}
