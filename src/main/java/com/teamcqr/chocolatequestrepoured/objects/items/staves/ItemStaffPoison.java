package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;

public class ItemStaffPoison extends ItemBase
{
	public ItemStaffPoison(String name) 
	{
		super(name);
		setMaxDamage(2048);
		setMaxStackSize(1);
	}
}