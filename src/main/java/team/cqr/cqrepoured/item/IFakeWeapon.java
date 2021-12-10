package team.cqr.cqrepoured.item;

import net.minecraft.item.Item;

public interface IFakeWeapon<T extends Item & ISupportWeapon<?>> {

	T getOriginalItem();

}
