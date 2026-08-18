package com.example.chocolatequest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import com.example.chocolatequest.registry.ModSounds;

public class MagicBellItem extends Item {

    public MagicBellItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide) {
            
            level.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.BELL_USE.get(), entityLiving.getSoundSource(), 1.0F, 1.0F);
            
            if (entityLiving instanceof Player player) {
                player.getCooldowns().addCooldown(stack.getItem(), 60);
            }
        }
        return stack;
    }
}
