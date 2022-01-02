package team.cqr.cqrepoured.client.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.armor.ItemCrown;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class TooltipEventHandler {

	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && ItemCrown.hasCrown(stack)) {
			event.getToolTip().add(0, TextFormatting.GOLD + "Crown attached");
		}
	}

}
