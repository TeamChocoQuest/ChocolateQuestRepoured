package com.example.chocolatequest.item.staff;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public class HealStaffItem extends Item {

    public HealStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Needs 4 full drumsticks (8 food points)
        if (player.getFoodData().getFoodLevel() >= 8 || player.getAbilities().instabuild) {
            
            if (!level.isClientSide()) {
                player.heal(12.0F); // 6 hearts
                
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1.0, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }

            if (!player.getAbilities().instabuild) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 8);
            }

            // 60 seconds cooldown
            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 1200);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (player.getFoodData().getFoodLevel() >= 1 || player.getAbilities().instabuild) {
                target.heal(2.0F); // 1 heart
                
                if (attacker.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1.0, target.getZ(), 5, 0.3, 0.3, 0.3, 0.1);
                }

                if (!player.getAbilities().instabuild) {
                    player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
                }
            }
        } else {
            target.heal(2.0F); // 1 heart
            if (attacker.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1.0, target.getZ(), 5, 0.3, 0.3, 0.3, 0.1);
            }
        }
        return true;
    }
}
