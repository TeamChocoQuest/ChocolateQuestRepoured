package team.cqr.cqrepoured.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

public interface ITieredItem {

	public IItemTier getTier();
	
	public default int getEnchantmentValue() {
		return this.getTier().getEnchantmentValue();
	}
	
	public default boolean isValidRepairItem(ItemStack itemA, ItemStack itemB) {
		return this.getTier().getRepairIngredient().test(itemB);
	}
	
}
