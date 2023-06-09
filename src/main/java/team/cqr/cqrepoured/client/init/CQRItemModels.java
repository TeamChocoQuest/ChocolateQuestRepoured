package team.cqr.cqrepoured.client.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.Item;

//@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class CQRItemModels {

	private static final List<Item> REGISTERED_ITEM_MODELS = new ArrayList<>();

	/*@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) {
		for (Block block : BlockExporterChest.getExporterChests()) {
			ModelLoader.setCustomStateMapper(block, stateMapper -> Collections.emptyMap());
		}

		// register custom item models first
		for (Item item : CQRItems.EventHandler.SPAWN_EGGS) {
			String registryName = item.getRegistryName().toString();
			registerCustomItemModel(item, 0, registryName.substring(0, registryName.length() - 2), "inventory");
		}

		// register all other item models
		for (Item item : CQRItems.EventHandler.ITEMS) {
			if (!REGISTERED_ITEM_MODELS.contains(item)) {
				registerItemModel(item);
			}
		}

		// register all other item block models
		for (BlockItem itemBlock : CQRBlocks.EventHandler.ITEM_BLOCKS) {
			if (!REGISTERED_ITEM_MODELS.contains(itemBlock)) {
				registerItemModel(itemBlock);
			}
		}

		SphereRenderer.init();
	}

	private static void registerItemModel(Item item) {
		registerCustomItemModel(item, 0, item.getRegistryName().toString(), "inventory");
	}

	private static void registerCustomItemModel(Item item, int meta, String modelLocation, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelLocation, variant));
		REGISTERED_ITEM_MODELS.add(item);
	}*/

}
