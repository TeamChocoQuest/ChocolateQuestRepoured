package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.item.Item;

public class ItemStaffWind extends Item  implements IRangedWeapon{

	public ItemStaffWind() {
		setMaxDamage(2048);
		setMaxStackSize(1);
	}

}