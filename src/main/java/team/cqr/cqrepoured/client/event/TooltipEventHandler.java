package team.cqr.cqrepoured.client.event;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

@OnlyIn(Dist.CLIENT)
//@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class TooltipEventHandler {

	@SubscribeEvent
	public static void onItemTooltipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && ItemArmorCrown.hasCrown(stack)) {
			event.getToolTip().add(0, new StringTextComponent(TextFormatting.GOLD + "Crown attached"));
		}
	}

}
