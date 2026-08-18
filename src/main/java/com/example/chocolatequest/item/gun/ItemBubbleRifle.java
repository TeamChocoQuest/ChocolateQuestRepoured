package com.example.chocolatequest.item.gun;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBubbleRifle extends ItemBubblePistol {

    public ItemBubbleRifle(Properties properties) {
        super(properties.durability(400));
    }

    public int getCooldown() {
        return 160; // 2 * 80
    }

    @Override
    public double getInaccuracy() {
        return 0.125D; // 0.25D * 0.5D
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 100; // 10 * 10
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
            player.getCooldowns().addCooldown(this, getCooldown());
        }
        stack.hurtAndBreak(1, entityLiving, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        return stack; // Rifle doesn't call super here to avoid breaking logic issues, just standard finish
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        stack.hurtAndBreak(1, entityLiving, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        if (entityLiving instanceof Player player) {
            player.getCooldowns().addCooldown(this, getCooldown());
        }
    }
}
