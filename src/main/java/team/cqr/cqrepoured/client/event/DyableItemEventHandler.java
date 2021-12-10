package team.cqr.cqrepoured.client.event;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;

@EventBusSubscriber(modid = CQRMain.MODID)
public class DyableItemEventHandler {

	@SubscribeEvent
	public static void colorItemArmors(ColorHandlerEvent.Item event) {
		Item[] dyables = CQRItems.EventHandler.ITEMS.stream().filter(ItemArmorDyable.class::isInstance).toArray(Item[]::new);

		event.getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex > 0 ? -1 : ((ItemArmorDyable) stack.getItem()).getColor(stack), dyables);
	}

}
