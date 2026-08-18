package com.example.chocolatequest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;

import java.util.List;

public class ItemBullet extends Item {
    private final EBulletType type;

    public ItemBullet(Properties properties, EBulletType type) {
        super(properties);
        this.type = type;
    }

    public EBulletType getType() {
        return this.type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.bullet_damage", this.getType().getAdditionalDamage()).withStyle(ChatFormatting.BLUE));
        if (this.getType().fireDamage()) {
            tooltipComponents.add(Component.translatable("item.cqrepoured.bullet_fire.tooltip").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
