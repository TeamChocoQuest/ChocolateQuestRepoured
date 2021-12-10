package team.cqr.cqrepoured.item.sword;

import net.minecraft.item.ItemSword;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends ItemSword implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(ToolMaterial material) {
		super(material);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING;
	}

}
