package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.registry.ModDataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID)
public class ModEvents {

    private static final Map<UUID, Float> LAST_ATTACK_STRENGTH = new HashMap<>();

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isCreative() && handleCreativeToolAttack(player, event.getTarget())) {
                event.setCanceled(true);
                return;
            }
            LAST_ATTACK_STRENGTH.put(player.getUUID(), player.getAttackStrengthScale(0.5F));
            if (!player.level().isClientSide() && !player.isCreative() &&
                    isReputationNpc(event.getTarget()) &&
                    player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                changeReputation(serverPlayer, getReputationFaction(event.getTarget()), -5);
            }
        }
    }

    private static boolean handleCreativeToolAttack(Player player, Entity target) {
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof com.example.chocolatequest.item.SuperToolItem) {
            if (!(target instanceof Player) && !player.level().isClientSide()) {
                target.discard();
            }
            return !(target instanceof Player);
        }
        if (held.getItem() instanceof com.example.chocolatequest.item.PathToolItem
                && target instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR cqr) {
            if (!player.level().isClientSide()) {
                if (player.isCrouching()) {
                    java.util.List<net.minecraft.core.BlockPos> nodes = com.example.chocolatequest.item.PathToolItem.getNodes(held);
                    if (nodes.isEmpty()) {
                        cqr.setPatrolPathFromWorld(java.util.List.of());
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                                "message.cqrepoured.path_tool.removed_from_mob", cqr.getDisplayName()), true);
                    } else {
                        cqr.setPatrolPathFromWorld(nodes);
                        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                                "message.cqrepoured.path_tool.applied", nodes.size(), cqr.getDisplayName()), true);
                    }
                } else {
                    java.util.List<net.minecraft.core.BlockPos> nodes = cqr.getPatrolPathWorld();
                    com.example.chocolatequest.item.PathToolItem.setNodes(held, nodes);
                    player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                            "message.cqrepoured.path_tool.copied", nodes.size(), cqr.getDisplayName()), true);
                }
                if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                            cqr.getX(), cqr.getY() + cqr.getBbHeight() * 0.6D, cqr.getZ(),
                            10, 0.35D, 0.45D, 0.35D, 0.02D);
                }
            }
            return true;
        }
        if (!(held.getItem() instanceof com.example.chocolatequest.item.MobToSpawnerItem)
                || target instanceof Player) {
            return false;
        }
        if (player.level().isClientSide()) return true;

        net.minecraft.nbt.CompoundTag entityTag = new net.minecraft.nbt.CompoundTag();
        if (!target.save(entityTag)) return true;
        entityTag.remove("UUID");
        entityTag.remove("Pos");
        net.minecraft.core.BlockPos pos = target.blockPosition();
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.5D;
        double z = target.getZ();
        player.level().setBlock(pos, com.example.chocolatequest.registry.ModBlocks.SPAWNER.get().defaultBlockState(), 3);
        if (player.level().getBlockEntity(pos) instanceof com.example.chocolatequest.block.entity.SpawnerBlockEntity spawner) {
            ItemStack bottle = new ItemStack(com.example.chocolatequest.registry.ModItems.SOUL_BOTTLE.get());
            bottle.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.of(entityTag));
            spawner.getInventory().setStackInSlot(0, bottle);
            spawner.setChanged();
            target.discard();
            if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z,
                        8, 0.3, 0.3, 0.3, 0.03);
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, x, y, z,
                        5, 0.25, 0.25, 0.25, 0.02);
            }
            player.level().playSound(null, pos, net.minecraft.sounds.SoundEvents.ZOMBIE_VILLAGER_CURE,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.8F);
        }
        return true;
    }

    @SubscribeEvent
    public static void onLeftClickBlock(net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (!player.isCreative()) return;
        ItemStack held = player.getItemInHand(event.getHand());
        if (held.getItem() instanceof com.example.chocolatequest.item.StructureSelectorItem) {
            if (!player.level().isClientSide()) {
                net.minecraft.core.BlockPos pos = player.isCrouching() ? player.blockPosition() : event.getPos();
                com.example.chocolatequest.item.StructureSelectorItem.setFirst(held, pos);
                player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                        "message.cqrepoured.structure_selector.first",
                        pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
            }
            event.setCanceled(true);
        } else if (held.getItem() instanceof com.example.chocolatequest.item.MobToSpawnerItem
                && player.level().getBlockEntity(event.getPos()) instanceof com.example.chocolatequest.block.entity.SpawnerBlockEntity spawner) {
            if (!player.level().isClientSide()) spawner.turnBackIntoEntity();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(net.neoforged.neoforge.event.entity.living.LivingDeathEvent event) {
        if (!event.getEntity().level().isClientSide() &&
                isReputationNpc(event.getEntity()) &&
                event.getSource().getEntity() instanceof net.minecraft.server.level.ServerPlayer player &&
                !player.isCreative()) {
            changeReputation(player, getReputationFaction(event.getEntity()), -15);
        }
    }

    private static boolean isReputationNpc(Entity entity) {
        return entity instanceof com.example.chocolatequest.entity.npc.AbstractCQRNPC ||
                entity instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR cqr && cqr.isTavernNpc();
    }

    private static com.example.chocolatequest.faction.EDefaultFaction getReputationFaction(Entity entity) {
        if (entity instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR cqr && cqr.isTavernNpc()) {
            return cqr.getTavernReputationFaction();
        }
        return ((com.example.chocolatequest.faction.IFactionRelated) entity).getFaction();
    }

    private static void changeReputation(net.minecraft.server.level.ServerPlayer player,
                                         com.example.chocolatequest.faction.EDefaultFaction faction, int amount) {
        com.example.chocolatequest.attachment.PlayerReputationAttachment reputation =
                player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION);
        reputation.addReputation(faction, amount);
        int value = reputation.getReputation(faction);
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation(
                        player.getUUID(), faction.name(), value));
        if (amount < 0) {
            player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                    "message.cqrepoured.reputation_lost", faction.name(), value), true);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(net.neoforged.neoforge.event.entity.living.LivingFallEvent event) {
        if (event.getEntity().getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET).getItem() == com.example.chocolatequest.registry.ModItems.CLOUD_BOOTS.get()) {
            event.setCanceled(true); // Negate all fall damage
            return;
        }

        // Golden Feather passive effect (reduces fall damage by half)
        if (event.getEntity() instanceof Player player) {
            boolean hasGoldenFeather = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).getItem() == com.example.chocolatequest.registry.ModItems.GOLDEN_FEATHER.get()) {
                    hasGoldenFeather = true;
                    break;
                }
            }
            if (hasGoldenFeather) {
                event.setDamageMultiplier(event.getDamageMultiplier() * 0.5f);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        Entity attacker = event.getSource().getEntity();

        // must never turn vendors and patrons against one another. This also
        // protects them from incidental melee, projectiles and area attacks.
        if (attacker instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR cqr
                && cqr.isTavernAlly(event.getEntity())) {
            event.setNewDamage(0.0F);
            cqr.setTarget(null);
            if (event.getEntity() instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR victim) {
                victim.setTarget(null);
                victim.setLastHurtByMob(null);
            }
            return;
        }

        if (event.getEntity() instanceof Player &&
                attacker instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR cqr &&
                cqr.isTavernNpc()) {
            event.setNewDamage(0.0F);
            return;
        }
        
        Entity directSource = event.getSource().getDirectEntity();
        if (directSource instanceof net.minecraft.world.entity.projectile.Projectile) {
            if (attacker instanceof net.minecraft.world.entity.LivingEntity livingShooter) {
                if (com.example.chocolatequest.entity.ai.target.TargetUtil.isAllyCheckingLeaders(livingShooter, event.getEntity())) {
                    event.setNewDamage(0.0F);
                    return;
                }
            }
        }

        if (attacker instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            
            // Lifesteal logic (needs to be moved here to actually heal based on final damage or we keep it in Post. 
            // Wait, we can keep Lifesteal in Post, but let's just add Backstab here.)
            
            if (weapon.getItem() instanceof com.example.chocolatequest.item.DaggerItem) {
                net.minecraft.world.phys.Vec3 attackerLook = player.getLookAngle().normalize();
                net.minecraft.world.phys.Vec3 targetLook = event.getEntity().getLookAngle().normalize();
        
                // Dot product of look vectors. If > 0.5, they are looking in roughly the same direction (attacker is behind the target)
                double dotProduct = attackerLook.dot(targetLook);
                
                if (dotProduct > 0.5D) {
                    // Backstab! 3x damage
                    float newDamage = event.getNewDamage() * 3.0F;
                    event.setNewDamage(newDamage);
                    
                    // Play sound and spawn particles
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
                            net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_CRIT, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.5F);
                    
                    if (player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, 
                                event.getEntity().getX(), event.getEntity().getY() + 1.0D, event.getEntity().getZ(), 
                                15, 0.2D, 0.2D, 0.2D, 0.1D);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (weapon.has(ModDataComponents.LIFESTEAL_AMOUNT)) {
                float attackStrength = LAST_ATTACK_STRENGTH.getOrDefault(player.getUUID(), 0.0F);
                if (attackStrength > 0.9F) {
                    Float healAmount = weapon.get(ModDataComponents.LIFESTEAL_AMOUNT);
                    if (healAmount != null && healAmount > 0) {
                        player.heal(healAmount);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            net.minecraft.nbt.CompoundTag persistentData = serverPlayer.getPersistentData();
            net.minecraft.nbt.CompoundTag cqrData;
            if (persistentData.contains("ChocolateQuestReDone", 10)) {
                cqrData = persistentData.getCompound("ChocolateQuestReDone");
            } else {
                cqrData = new net.minecraft.nbt.CompoundTag();
                persistentData.put("ChocolateQuestReDone", cqrData);
            }

            if (!cqrData.getBoolean("ReceivedGuideBook")) {
                cqrData.putBoolean("ReceivedGuideBook", true);
                ItemStack bookStack = new ItemStack(com.example.chocolatequest.registry.ModItems.GUIDE_BOOK.get());
                if (!serverPlayer.getInventory().add(bookStack)) {
                    serverPlayer.drop(bookStack, false);
                }
            }
        }
    }
}
