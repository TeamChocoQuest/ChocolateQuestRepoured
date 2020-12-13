package team.cqr.cqrepoured.objects.items;

import net.minecraft.item.Item;

public interface ISupportWeapon<T extends Item & IFakeWeapon<?>> {

	T getFakeWeapon();

}
