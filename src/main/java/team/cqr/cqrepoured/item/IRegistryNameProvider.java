package team.cqr.cqrepoured.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.registries.ForgeRegistries;

public interface IRegistryNameProvider extends IForgeItem {
	
	public default ResourceLocation getRegistryName() {
		if(this instanceof Item self) {
			return ForgeRegistries.ITEMS.getKey(self);
		}
		return null;
	}
	
	public static ResourceLocation getRegistryNameOf(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}

}
