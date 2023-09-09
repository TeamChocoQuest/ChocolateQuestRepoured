package team.cqr.cqrepoured.item.sword;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends ItemCQRWeapon implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(Tier material, Item.Properties props) {
		super(material, props);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING.get();
	}

}
