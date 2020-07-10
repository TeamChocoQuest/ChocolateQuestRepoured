package com.teamcqr.chocolatequestrepoured.client.event;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemCrown;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class TooltipEventHandler {

	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && ItemCrown.hasCrown(stack)) {
			List<String> tooltip = event.getToolTip();
			event.getToolTip().add("");
			for (int i = tooltip.size() - 1; i > 1; i--) {
				tooltip.set(i, tooltip.get(i - 1));
			}
			tooltip.set(1, TextFormatting.GOLD + "Crown attached");
		}
	}

}
