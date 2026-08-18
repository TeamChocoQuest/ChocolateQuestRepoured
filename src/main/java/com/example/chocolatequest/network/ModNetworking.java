package com.example.chocolatequest.network;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.network.packet.CPacketContainerClickButton;
import com.example.chocolatequest.network.packet.CPacketOpenMerchantGui;
import com.example.chocolatequest.network.packet.CPacketSyncEntity;
import com.example.chocolatequest.network.packet.SaveStructurePayload;
import com.example.chocolatequest.network.packet.SPacketUpdatePlayerReputation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Client to Server (C2S)
        registrar.playToServer(
            com.example.chocolatequest.network.packet.CPacketReloadFirearm.TYPE,
            com.example.chocolatequest.network.packet.CPacketReloadFirearm.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() -> {
                if (context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
                    net.minecraft.world.item.ItemStack firearmStack = player.getMainHandItem();
                    com.example.chocolatequest.item.ItemRevolver firearm;
                    if (firearmStack.getItem() instanceof com.example.chocolatequest.item.ItemRevolver mainHandFirearm) {
                        firearm = mainHandFirearm;
                    } else {
                        firearmStack = player.getOffhandItem();
                        if (!(firearmStack.getItem() instanceof com.example.chocolatequest.item.ItemRevolver offHandFirearm)) {
                            return;
                        }
                        firearm = offHandFirearm;
                    }

                    if (!firearm.startReload(player, firearmStack)) {
                        if (firearm.getLoadedAmmo(firearmStack) >= firearm.getCapacity()) {
                            player.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.cqrepoured.firearm_full"), true);
                        } else if (firearm.findReloadAmmo(player, firearmStack).isEmpty() && !player.getAbilities().instabuild) {
                            player.displayClientMessage(net.minecraft.network.chat.Component.translatable("message.cqrepoured.firearm_no_ammo"), true);
                        }
                    }
                }
            })
        );

        registrar.playToServer(
            com.example.chocolatequest.network.packet.CPacketDodge.TYPE,
            com.example.chocolatequest.network.packet.CPacketDodge.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        if (com.example.chocolatequest.event.TomeMechanics.hasEnchantmentAnywhere(serverPlayer, com.example.chocolatequest.registry.ModEnchantments.COSMIC_WARP)) {
                            long lastWarp = serverPlayer.getPersistentData().getLong("cqr_warp_last");
                            if (serverPlayer.level().getGameTime() - lastWarp >= 100) { // 5 sec cooldown
                                net.minecraft.world.phys.Vec3 look = serverPlayer.getLookAngle();
                                double dx = look.x * 5.0;
                                double dz = look.z * 5.0;
                                
                                serverPlayer.teleportTo(serverPlayer.getX() + dx, serverPlayer.getY() + 1.0, serverPlayer.getZ() + dz);
                                serverPlayer.getPersistentData().putLong("cqr_warp_last", serverPlayer.level().getGameTime());
                                serverPlayer.level().playSound(null, serverPlayer.blockPosition(), net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
                            }
                        }
                    }
                });
            }
        );

        registrar.playToServer(
            SaveStructurePayload.TYPE,
            SaveStructurePayload.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        net.minecraft.world.level.block.entity.BlockEntity be = serverPlayer.level().getBlockEntity(payload.pos());
                        if (be instanceof com.example.chocolatequest.block.entity.ExporterBlockEntity exporter) {
                            exporter.setValues(payload.structureName(), payload.startX(), payload.startY(), payload.startZ(), payload.endX(), payload.endY(), payload.endZ(), payload.relativeMode(), payload.ignoreEntities());
                            
                            net.minecraft.server.level.ServerLevel level = serverPlayer.serverLevel();
                            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager manager = level.getStructureManager();
                            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate template = manager.getOrCreate(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, exporter.getStructureName()));
                            
                            net.minecraft.core.BlockPos start = exporter.isRelativeMode() ? exporter.getBlockPos().offset(exporter.getStartX(), exporter.getStartY(), exporter.getStartZ()) : new net.minecraft.core.BlockPos(exporter.getStartX(), exporter.getStartY(), exporter.getStartZ());
                            net.minecraft.core.BlockPos end = exporter.isRelativeMode() ? exporter.getBlockPos().offset(exporter.getEndX(), exporter.getEndY(), exporter.getEndZ()) : new net.minecraft.core.BlockPos(exporter.getEndX(), exporter.getEndY(), exporter.getEndZ());
                            
                            net.minecraft.core.BlockPos min = new net.minecraft.core.BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
                            net.minecraft.core.BlockPos max = new net.minecraft.core.BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
                            net.minecraft.core.BlockPos size = max.subtract(min).offset(1, 1, 1);
                            
                            template.fillFromWorld(level, min, size, !exporter.isIgnoreEntities(), com.example.chocolatequest.registry.ModBlocks.NULL_BLOCK.get());
                            manager.save(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, exporter.getStructureName()));
                            
                            serverPlayer.sendSystemMessage(net.minecraft.network.chat.Component.literal("Successfully exported structure: " + exporter.getStructureName()));
                        }
                    }
                });
            }
        );

        registrar.playToServer(
            CPacketSyncEntity.TYPE,
            CPacketSyncEntity.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (player.level().getEntity(payload.entityId()) instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR entity) {
                        if (player.isCreative() || entity.getLeader() == player) {
                            entity.setHealthScale(payload.healthScaling() / 100.0f);
                            entity.setSizeVariation(payload.sizeScaling() / 100.0f);
                            // Set base health attribute to scale properly
                            net.minecraft.world.entity.ai.attributes.AttributeInstance healthAttr = entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
                            if (healthAttr != null) {
                                healthAttr.setBaseValue(entity.getBaseHealth() * entity.getHealthScale());
                                if (entity.getHealth() > entity.getMaxHealth()) {
                                    entity.setHealth(entity.getMaxHealth());
                                }
                            }
                            
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.HEAD, payload.dropChanceHelm() / 100.0f);
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.CHEST, payload.dropChanceChest() / 100.0f);
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.LEGS, payload.dropChanceLegs() / 100.0f);
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.FEET, payload.dropChanceFeet() / 100.0f);
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.MAINHAND, payload.dropChanceMainhand() / 100.0f);
                            entity.setDropChance(net.minecraft.world.entity.EquipmentSlot.OFFHAND, payload.dropChanceOffhand() / 100.0f);
                        }
                    }
                });
            }
        );

        registrar.playToServer(
            CPacketOpenMerchantGui.TYPE,
            CPacketOpenMerchantGui.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        if (player.level().getEntity(payload.entityId()) instanceof com.example.chocolatequest.entity.bases.AbstractEntityCQR entity &&
                                entity.isTavernNpc() && player.distanceToSqr(entity) <= 64.0D) {
                            entity.openTavernTradeScreen(serverPlayer);
                        }
                    }
                });
            }
        );

        registrar.playToServer(
            CPacketContainerClickButton.TYPE,
            CPacketContainerClickButton.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (!(player.containerMenu instanceof com.example.chocolatequest.inventory.ContainerCQREntity menu)) return;
                    com.example.chocolatequest.entity.bases.AbstractEntityCQR entity = menu.getEntity();
                    if (entity == null || player.distanceToSqr(entity) > 64.0D
                            || (!player.isCreative() && entity.getLeader() != player)) return;

                    switch (payload.button()) {
                        case 1 -> entity.setHealth(entity.getMaxHealth());
                        case 2 -> entity.setSitting(!entity.isSitting());
                        case 3 -> {
                            entity.setTarget(null);
                            entity.setLastHurtByMob(null);
                        }
                        case 4 -> {
                            if (player.isCreative() && entity.isTavernNpc()) {
                                entity.addTavernTradeFromHands(player);
                            }
                        }
                        case 5 -> {
                            if (player.isCreative() && entity.isTavernNpc()) {
                                entity.removeLastTavernTrade(player);
                            }
                        }
                        default -> {
                        }
                    }
                });
            }
        );

        // Server to Client (S2C)
        registrar.playToClient(
            SPacketUpdatePlayerReputation.TYPE,
            SPacketUpdatePlayerReputation.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    net.minecraft.world.entity.player.Player player = context.player();
                    if (player != null && player.getUUID().equals(payload.playerId())) {
                        try {
                            com.example.chocolatequest.faction.EDefaultFaction faction = com.example.chocolatequest.faction.EDefaultFaction.valueOf(payload.faction());
                            player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION).setReputation(faction, payload.reputation());
                        } catch (IllegalArgumentException e) {
                            // Ignored
                        }
                    }
                });
            }
        );
    }
}
