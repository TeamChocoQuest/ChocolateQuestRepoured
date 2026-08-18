package com.example.chocolatequest.item.staff;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ElectricStaffItem extends Item {

    public ElectricStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getFoodData().getFoodLevel() > 0 || player.getAbilities().instabuild) {
            // Raytrace to find where the player is looking
            HitResult hitResult = player.pick(50.0D, 1.0F, false);

            if (hitResult.getType() != HitResult.Type.MISS) {
                if (!level.isClientSide()) {
                    // Spawn Lightning
                    LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                    if (lightningBolt != null) {
                        lightningBolt.moveTo(hitResult.getLocation());
                        lightningBolt.setVisualOnly(false);
                        level.addFreshEntity(lightningBolt);
                    }
                }

                // Consume Hunger (1 full drumstick)
                if (!player.getAbilities().instabuild) {
                    int food = player.getFoodData().getFoodLevel();
                    if (food > 1) {
                        player.getFoodData().setFoodLevel(food - 2);
                    } else if (food == 1) {
                        player.getFoodData().setFoodLevel(0);
                    }
                }

                // Cooldown 2 seconds
                com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 40);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
