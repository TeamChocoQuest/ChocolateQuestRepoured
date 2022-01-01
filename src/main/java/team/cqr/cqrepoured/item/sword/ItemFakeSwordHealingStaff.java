package team.cqr.cqrepoured.item.sword;

import net.minecraft.item.SwordItem;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends SwordItem implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(ToolMaterial material) {
		super(material);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING;
	}

}
