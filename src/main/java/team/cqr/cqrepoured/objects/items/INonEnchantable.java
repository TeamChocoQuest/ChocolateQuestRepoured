package team.cqr.cqrepoured.objects.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public interface INonEnchantable {

	default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	default boolean isEnchantable(ItemStack stack) {
		return false;
	}

	default boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
