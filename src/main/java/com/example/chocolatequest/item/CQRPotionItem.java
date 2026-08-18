package com.example.chocolatequest.item;

import com.example.chocolatequest.entity.projectile.CQRPotionEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;

public class CQRPotionItem extends Item implements ProjectileItem {
    private final String elementType;

    public CQRPotionItem(String elementType, Item.Properties properties) {
        super(properties);
        this.elementType = elementType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SPLASH_POTION_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            CQRPotionEntity potion = new CQRPotionEntity(level, player);
            potion.setItem(itemstack);
            potion.setElementType(this.elementType);
            potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 1.2F, 1.0F);
            level.addFreshEntity(potion);
        }

        player.swing(hand, true);
        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, net.minecraft.core.Position pos, ItemStack itemStack, net.minecraft.core.Direction direction) {
        CQRPotionEntity potion = new CQRPotionEntity(level, pos.x(), pos.y(), pos.z());
        potion.setItem(itemStack);
        potion.setElementType(this.elementType);
        return potion;
    }
}
