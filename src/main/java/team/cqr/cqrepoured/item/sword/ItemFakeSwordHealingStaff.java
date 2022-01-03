package team.cqr.cqrepoured.item.sword;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends SwordItem implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(IItemTier material, int attackDamage, float attackSpeed, Item.Properties props) {
		super(material, attackDamage, attackSpeed, props);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING;
	}

}
