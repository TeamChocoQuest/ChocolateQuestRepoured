package team.cqr.cqrepoured.client.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.item.armor.ItemCrown;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class TooltipEventHandler {

	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && ItemCrown.hasCrown(stack)) {
			event.getToolTip().add(0, TextFormatting.GOLD + "Crown attached");
		}
	}

}
