package com.example.chocolatequest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CQBannerItem extends Item {

    public CQBannerItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public net.minecraft.world.InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        Level level = context.getLevel();
        net.minecraft.core.BlockPos pos = context.getClickedPos();
        net.minecraft.core.Direction face = context.getClickedFace();
        if (level.getBlockState(pos).is(com.example.chocolatequest.registry.ModBlocks.BANNER_STAND.get())) {
            return net.minecraft.world.InteractionResult.PASS;
        }
        net.minecraft.core.BlockPos placePos = pos.relative(face);

        if (level.getBlockState(placePos).canBeReplaced()) {
            level.setBlock(placePos, com.example.chocolatequest.registry.ModBlocks.BANNER_STAND.get().defaultBlockState(), 3);
            net.minecraft.world.level.block.entity.BlockEntity blockEntity = level.getBlockEntity(placePos);
            if (blockEntity instanceof com.example.chocolatequest.block.entity.BannerStandBlockEntity bannerStand) {
                ItemStack bannerCopy = context.getItemInHand().copyWithCount(1);
                bannerStand.setBanner(bannerCopy);
                bannerStand.setRotation((int) context.getPlayer().getYRot() - 180);
            }
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            level.playSound(context.getPlayer(), placePos, net.minecraft.sounds.SoundEvents.WOOD_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
        }
        return net.minecraft.world.InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            // Give speed effect while holding/using the banner (duration 20 ticks = 1 second)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0, false, false));
        }
        super.onUseTick(level, livingEntity, stack, count);
    }
}

