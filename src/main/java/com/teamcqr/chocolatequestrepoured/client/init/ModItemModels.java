package com.teamcqr.chocolatequestrepoured.client.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporterChest;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ModItemModels {

	private static final List<Item> REGISTERED_ITEM_MODELS = new ArrayList<Item>();

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) {
		for (Block block : BlockExporterChest.EXPORTER_CHESTS) {
			ModelLoader.setCustomStateMapper(block, stateMapper -> Collections.emptyMap());
		}

		// register custom item models first
		for (Item item : CQRItems.ItemRegistrationHandler.SPAWN_EGGS) {
			String registryName = item.getRegistryName().toString();
			registerCustomItemModel(item, 0, registryName.substring(0, registryName.length() - 2), "inventory");
		}

		// register all other item models
		for (Item item : CQRItems.ItemRegistrationHandler.ITEMS) {
			if (!REGISTERED_ITEM_MODELS.contains(item)) {
				registerItemModel(item);
			}
		}

		// register all other item block models
		for (ItemBlock itemBlock : CQRBlocks.BlockRegistrationHandler.ITEM_BLOCKS) {
			if (!REGISTERED_ITEM_MODELS.contains(itemBlock)) {
				registerItemModel(itemBlock);
			}
		}
	}

	private static void registerItemModel(Item item) {
		registerCustomItemModel(item, 0, item.getRegistryName().toString(), "inventory");
	}

	private static void registerCustomItemModel(Item item, int meta, String modelLocation, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelLocation, variant));
		REGISTERED_ITEM_MODELS.add(item);
	}

}
