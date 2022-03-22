package team.cqr.cqrepoured.client.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.client.util.SphereRenderer;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class CQRItemModels {

	private static final List<Item> REGISTERED_ITEM_MODELS = new ArrayList<>();

	@SubscribeEvent
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
	}

}
