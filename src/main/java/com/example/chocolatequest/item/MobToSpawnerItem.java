package com.example.chocolatequest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Marker item; its left-click behavior is handled by ModEvents. */
public class MobToSpawnerItem extends Item {
    public MobToSpawnerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.mob_to_spawner_tool.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
