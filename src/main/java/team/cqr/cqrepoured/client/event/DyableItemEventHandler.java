package team.cqr.cqrepoured.client.event;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;

@Mod.EventBusSubscriber(modid = CQRConstants.MODID, value = Dist.CLIENT)
public class DyableItemEventHandler {

	@SubscribeEvent
	public static void colorItemArmors(ColorHandlerEvent.Item event) {
		Item[] dyables = CQRItems.ITEMS.getEntries().stream().filter(itemRegistryObject -> itemRegistryObject.get() instanceof ItemArmorDyable).map(RegistryObject::get).toArray(Item[]::new);

		ItemColors itemColors = event.getItemColors();
		itemColors.register(
				(stack, tintIndex) -> tintIndex > 0 ? -1 : ((ItemArmorDyable) stack.getItem()).getColor(stack),
				dyables);
	}

}
