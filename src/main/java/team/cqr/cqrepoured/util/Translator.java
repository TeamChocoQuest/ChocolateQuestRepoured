package team.cqr.cqrepoured.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class Translator {

	public static Component translate(String key, Object... args) {
		return Component.translatable(key, args);
	}
	
	static ResourceLocation getRegistryNameOf(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}

	public static Component translateItem(Item item, String suffix, Object... args) {
		return translateItem(getRegistryNameOf(item), suffix, args);
	}

	public static Component translateItem(ResourceLocation resourceLocation, String suffix, Object... args) {
		return translateItem(resourceLocation.getNamespace(), resourceLocation.getPath(), suffix, args);
	}

	public static Component translateItem(String namespace, String path, String suffix, Object... args) {
		return translateItem(namespace, path + suffix, args);
	}

	public static Component translateItem(Item item, Object... args) {
		return translateItem(getRegistryNameOf(item), args);
	}

	public static Component translateItem(ResourceLocation resourceLocation, Object... args) {
		return translateItem(resourceLocation.getNamespace(), resourceLocation.getPath(), args);
	}

	public static Component translateItem(String namespace, String path, Object... args) {
		return translate("item." + namespace + "." + path, args);
	}

}
