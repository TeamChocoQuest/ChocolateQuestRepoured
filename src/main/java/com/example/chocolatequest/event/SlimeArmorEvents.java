package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.HealingSlimeEntity;
import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Random;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SlimeArmorEvents {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof Player player) {
            
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);

            if (head.is(ModItems.SLIME_HELMET.get()) &&
                chest.is(ModItems.SLIME_CHESTPLATE.get()) &&
                legs.is(ModItems.SLIME_LEGGINGS.get()) &&
                feet.is(ModItems.SLIME_BOOTS.get())) {
                
                // Check cooldown on the chestplate (using it as the cooldown tracker)
                if (!player.getCooldowns().isOnCooldown(ModItems.SLIME_CHESTPLATE.get())) {
                    
                    // Spawn Healing Slime
                    HealingSlimeEntity healingSlime = ModEntities.HEALING_SLIME.get().create(player.level());
                    if (healingSlime != null) {
                        // Ensure slime spawns at least 1.5 blocks away, up to 3.0 blocks away
                        double offsetX = (RANDOM.nextBoolean() ? 1.5 : -1.5) + (RANDOM.nextDouble() * 1.5);
                        double offsetZ = (RANDOM.nextBoolean() ? 1.5 : -1.5) + (RANDOM.nextDouble() * 1.5);
                        
                        healingSlime.moveTo(player.getX() + offsetX, player.getY(), player.getZ() + offsetZ, 0, 0);
                        player.level().addFreshEntity(healingSlime);
                        
                        // Apply 20 seconds (400 ticks) cooldown to all slime armor pieces so it shows visually
                        player.getCooldowns().addCooldown(ModItems.SLIME_HELMET.get(), 400);
                        player.getCooldowns().addCooldown(ModItems.SLIME_CHESTPLATE.get(), 400);
                        player.getCooldowns().addCooldown(ModItems.SLIME_LEGGINGS.get(), 400);
                        player.getCooldowns().addCooldown(ModItems.SLIME_BOOTS.get(), 400);
                    }
                }
            }
        }
    }
}
