package team.cqr.cqrepoured.objects.items;

import net.minecraft.item.Item;

public interface IFakeWeapon<T extends Item & ISupportWeapon<?>> {

	T getOriginalItem();

}
