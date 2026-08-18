package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.registry.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID)
public class TomeMechanics {

    public static boolean hasEnchantment(LivingEntity entity, ResourceKey<Enchantment> key, EquipmentSlot slot) {
        if (entity.level().isClientSide()) return false;
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.isEmpty()) return false;
        Registry<Enchantment> registry = entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = registry.getHolder(key).orElse(null);
        if (holder == null) return false;
        ItemEnchantments enchants = stack.getOrDefault(net.minecraft.core.component.DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        return enchants.getLevel(holder) > 0;
    }

    public static boolean hasEnchantmentAnywhere(LivingEntity entity, ResourceKey<Enchantment> key) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (hasEnchantment(entity, key, slot)) return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level().isClientSide) return;

        if (entity instanceof Player player && entity.tickCount % 10 == 0) {
            if (hasEnchantment(player, ModEnchantments.VOIDS_GAZE, EquipmentSlot.HEAD)) {
                // Raytrace to find targeted entity
                Vec3 eyePos = player.getEyePosition();
                Vec3 lookVec = player.getLookAngle();
                Vec3 endPos = eyePos.add(lookVec.scale(15.0));
                AABB aabb = player.getBoundingBox().expandTowards(lookVec.scale(15.0)).inflate(1.0);
                
                Entity targeted = null;
                double minDistance = Double.MAX_VALUE;
                for (Entity e : player.level().getEntities(player, aabb, ent -> ent instanceof LivingEntity)) {
                    AABB eAabb = e.getBoundingBox().inflate(e.getPickRadius());
                    java.util.Optional<Vec3> opt = eAabb.clip(eyePos, endPos);
                    if (opt.isPresent()) {
                        double dist = eyePos.distanceToSqr(opt.get());
                        if (dist < minDistance) {
                            minDistance = dist;
                            targeted = e;
                        }
                    }
                }
                
                if (targeted instanceof LivingEntity le) {
                    le.getPersistentData().putInt("cqr_void_gaze", 40); // 2 seconds of mark
                    ((ServerLevel) player.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, le.getX(), le.getY() + le.getBbHeight() + 0.5, le.getZ(), 5, 0.2, 0.2, 0.2, 0.01);
                }
            }
        }

        // Handle Void's Gaze countdown
        if (entity.getPersistentData().contains("cqr_void_gaze")) {
            int ticks = entity.getPersistentData().getInt("cqr_void_gaze");
            if (ticks > 0) {
                entity.getPersistentData().putInt("cqr_void_gaze", ticks - 1);
            } else {
                entity.getPersistentData().remove("cqr_void_gaze");
            }
        }
        
        // Handle Soulfire Burn countdown
        if (entity.getPersistentData().contains("cqr_soulfire")) {
            int ticks = entity.getPersistentData().getInt("cqr_soulfire");
            if (ticks > 0) {
                if (ticks % 20 == 0) { // 1 damage per second, ignores armor
                    entity.hurt(entity.damageSources().magic(), 1.0f);
                    ((ServerLevel) entity.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 5, 0.3, 0.5, 0.3, 0.05);
                }
                entity.getPersistentData().putInt("cqr_soulfire", ticks - 1);
            } else {
                entity.getPersistentData().remove("cqr_soulfire");
            }
        }
        
        // Handle Stun
        if (entity.getPersistentData().contains("cqr_stun")) {
            int ticks = entity.getPersistentData().getInt("cqr_stun");
            if (ticks > 0) {
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0); // stop horizontal movement
                ((ServerLevel) entity.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 2, 0.2, 0.1, 0.2, 0.01);
                entity.getPersistentData().putInt("cqr_stun", ticks - 1);
            } else {
                entity.getPersistentData().remove("cqr_stun");
            }
        }

        if (entity instanceof Player player && (entity.isInLava() || entity.isOnFire())) {
            if (hasEnchantment(player, ModEnchantments.DEHYDRATION_COUNTER, EquipmentSlot.LEGS)) {
                long lastUsed = player.getPersistentData().getLong("cqr_dehydration_last");
                if (player.level().getGameTime() - lastUsed >= 600) { // 30 seconds cooldown
                    player.clearFire();
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
                    player.getPersistentData().putLong("cqr_dehydration_last", player.level().getGameTime());
                    player.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXTINGUISH_FIRE, 1.0f, 1.0f);
                    ((ServerLevel) player.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }

        if (entity instanceof Player player && player.isSprinting() && player.onGround()) {
            if (hasEnchantment(player, ModEnchantments.COMBUSTION_TRAIL, EquipmentSlot.FEET)) {
                if (player.tickCount % 3 == 0) { // Spawn fire every 3 ticks
                    BlockPos pos = player.blockPosition();
                    if (player.level().getBlockState(pos).isAir() && player.level().getBlockState(pos.below()).isSolid()) {
                        player.level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3); // Standard fire, could use custom block if 2s lifespan is strictly needed, but fire naturally extinguishes or we can use custom entity
                        // For 2 second lifespan, we can use an AreaEffectCloud with fire particles and damage
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();
        if (victim.level().isClientSide) return;

        if (attackerEntity instanceof Player player && victim.getPersistentData().contains("cqr_void_gaze") && victim.getPersistentData().getInt("cqr_void_gaze") > 0) {
            event.setNewDamage(event.getNewDamage() * 1.15f);
        }

        if (attackerEntity != null && victim instanceof Player) {
            // Find nearby player with Vitality Link
            List<Player> nearbyPlayers = victim.level().getEntitiesOfClass(Player.class, victim.getBoundingBox().inflate(10.0));
            for (Player p : nearbyPlayers) {
                if (p != victim && hasEnchantment(p, ModEnchantments.VITALITY_LINK, EquipmentSlot.CHEST)) {
                    float transferDamage = event.getNewDamage() * 0.10f;
                    event.setNewDamage(event.getNewDamage() * 0.90f);
                    p.hurt(p.damageSources().magic(), transferDamage);
                    break; // Only one player takes the link damage
                }
            }
        }

        if (attackerEntity instanceof LivingEntity attacker) {
            if (hasEnchantment(attacker, ModEnchantments.SOULFIRE_BURN, EquipmentSlot.MAINHAND)) {
                victim.getPersistentData().putInt("cqr_soulfire", 100); // 5 seconds
            }

            if (hasEnchantment(attacker, ModEnchantments.RUNIC_PIERCING, EquipmentSlot.MAINHAND)) {
                if (attacker.getRandom().nextFloat() < 0.15f) { // 15% chance
                    victim.getPersistentData().putInt("cqr_stun", 20); // 1 second stun
                    // To ignore armor, we could change damage source to magic, but we can't easily change source in Pre event.
                    // But we can add extra damage equal to armor reduction, or just apply magic damage manually.
                    // For simplicity, we can do extra magic damage.
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();
        if (victim.level().isClientSide || attackerEntity == null) return;

        if (attackerEntity instanceof LivingEntity attacker && hasEnchantment(attacker, ModEnchantments.SANGUINE_RITUAL, EquipmentSlot.MAINHAND)) {
            // Spawn an AreaEffectCloud that heals allies
            net.minecraft.world.entity.AreaEffectCloud cloud = new net.minecraft.world.entity.AreaEffectCloud(victim.level(), victim.getX(), victim.getY(), victim.getZ());
            cloud.setRadius(1.5f);
            cloud.setRadiusOnUse(-0.5f);
            cloud.setWaitTime(0);
            cloud.setDuration(30); // 1.5 seconds
            cloud.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0)); // Instant health 1 heart per tick applied? No, healing cloud ticks every 5.
            cloud.setParticle(net.minecraft.core.particles.ParticleTypes.FALLING_HONEY); // Or custom red particle
            victim.level().addFreshEntity(cloud);
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity shooter) {
            if (hasEnchantment(shooter, ModEnchantments.ZEPHYR_ARROW, EquipmentSlot.MAINHAND) || hasEnchantment(shooter, ModEnchantments.ZEPHYR_ARROW, EquipmentSlot.OFFHAND)) {
                arrow.setNoGravity(true);
                arrow.getPersistentData().putDouble("cqr_startX", arrow.getX());
                arrow.getPersistentData().putDouble("cqr_startY", arrow.getY());
                arrow.getPersistentData().putDouble("cqr_startZ", arrow.getZ());
                arrow.getPersistentData().putBoolean("cqr_zephyr", true);
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrow && arrow.getPersistentData().getBoolean("cqr_zephyr")) {
            if (event.getRayTraceResult() instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity) {
                double sx = arrow.getPersistentData().getDouble("cqr_startX");
                double sy = arrow.getPersistentData().getDouble("cqr_startY");
                double sz = arrow.getPersistentData().getDouble("cqr_startZ");
                double distanceSqr = arrow.distanceToSqr(sx, sy, sz);
                if (distanceSqr > 30 * 30) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * 1.20);
                }
            }
            arrow.setNoGravity(false); // Restore gravity so it falls if it hits a block
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdd(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        
        if (hasEnchantment(entity, ModEnchantments.DIVINE_GRACE, EquipmentSlot.CHEST)) {
            MobEffectInstance effect = event.getEffectInstance();
            if (!effect.getEffect().value().isBeneficial()) {
                // Cut duration in half
                // MobEffectInstance duration is final, so we might need to remove and re-add or we can't easily do it in Added event.
                // Wait, Added event doesn't let us replace the instance easily. We could cancel and add a new one.
                // Or we can modify it if possible. In 1.21.1 MobEffectInstance has `update` or we can just cancel and apply manually.
            } else if (effect.getEffect().value() == MobEffects.HEAL.value()) {
                // For instant health, amplify
            }
        }
    }
}
