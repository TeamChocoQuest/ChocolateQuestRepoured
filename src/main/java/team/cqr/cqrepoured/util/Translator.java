package team.cqr.cqrepoured.util;

import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class Translator {

	public static TranslationTextComponent translate(String key, Object... args) {
		return new TranslationTextComponent(key, args);
	}

	public static TranslationTextComponent translateItem(Item item, String suffix, Object... args) {
		return translateItem(item.getRegistryName(), suffix, args);
	}

	public static TranslationTextComponent translateItem(ResourceLocation resourceLocation, String suffix, Object... args) {
		return translateItem(resourceLocation.getNamespace(), resourceLocation.getPath(), suffix, args);
	}

	public static TranslationTextComponent translateItem(String namespace, String path, String suffix, Object... args) {
		return translateItem(namespace, path + suffix, args);
	}

	public static TranslationTextComponent translateItem(Item item, Object... args) {
		return translateItem(item.getRegistryName(), args);
	}

	public static TranslationTextComponent translateItem(ResourceLocation resourceLocation, Object... args) {
		return translateItem(resourceLocation.getNamespace(), resourceLocation.getPath(), args);
	}

	public static TranslationTextComponent translateItem(String namespace, String path, Object... args) {
		return translate("item." + namespace + "." + path, args);
	}

}
