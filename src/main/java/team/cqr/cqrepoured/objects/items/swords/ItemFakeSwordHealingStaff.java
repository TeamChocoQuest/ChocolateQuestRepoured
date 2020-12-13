package team.cqr.cqrepoured.objects.items.swords;

import net.minecraft.item.ItemSword;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.items.IFakeWeapon;
import team.cqr.cqrepoured.objects.items.staves.ItemStaffHealing;

public class ItemFakeSwordHealingStaff extends ItemSword implements IFakeWeapon<ItemStaffHealing> {

	public ItemFakeSwordHealingStaff(ToolMaterial material) {
		super(material);
	}

	@Override
	public ItemStaffHealing getOriginalItem() {
		return CQRItems.STAFF_HEALING;
	}

}
