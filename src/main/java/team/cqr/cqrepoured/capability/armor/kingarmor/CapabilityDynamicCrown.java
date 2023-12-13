package team.cqr.cqrepoured.capability.armor.kingarmor;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class CapabilityDynamicCrown {

	private Item attachedItem = null;

	@Nullable
	public Item getAttachedItem() {
		return this.attachedItem;
	}

	public void attachItem(ItemStack item) {
		this.attachedItem = item.getItem();
	}

	public void attachItem(Item item) {
		this.attachedItem = item;
	}

	public void attachItem(ResourceLocation itemResLoc) {
		if (ForgeRegistries.ITEMS.containsKey(itemResLoc)) {
			this.attachedItem = ForgeRegistries.ITEMS.getValue(itemResLoc);
		}
	}

}
