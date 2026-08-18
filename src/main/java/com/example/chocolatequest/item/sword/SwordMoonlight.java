package com.example.chocolatequest.item.sword;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SwordMoonlight extends CQSwordItem {
    public SwordMoonlight(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide() && pAttacker.level().isNight()) {
            if (pAttacker instanceof Player player) {
                pTarget.hurt(pAttacker.damageSources().playerAttack(player), 3.0F);
            } else {
                pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), 3.0F);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, java.util.List<net.minecraft.network.chat.Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(net.minecraft.network.chat.Component.translatable(this.getDescriptionId() + ".desc").withStyle(net.minecraft.ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
