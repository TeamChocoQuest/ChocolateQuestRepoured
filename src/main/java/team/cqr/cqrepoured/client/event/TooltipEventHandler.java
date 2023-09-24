package team.cqr.cqrepoured.client.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

@EventBusSubscriber(modid = CQRConstants.MODID, value = Dist.CLIENT)
public class TooltipEventHandler {

	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && ItemArmorCrown.hasCrown(stack)) {
			event.getToolTip().add(0, Component.translatable(ChatFormatting.GOLD + "Crown attached"));
		}
	}

}
