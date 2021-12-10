package team.cqr.cqrepoured.item;

import net.minecraft.item.Item;

public interface ISupportWeapon<T extends Item & IFakeWeapon<?>> {

	T getFakeWeapon();

}
