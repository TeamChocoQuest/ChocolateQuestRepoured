package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ItemStaffWind extends Item  implements IRangedWeapon{

	public ItemStaffWind() {
		setMaxDamage(2048);
		setMaxStackSize(1);
	}

	@Override
	public void shoot(World world, Entity shooter, double x, double y, double z) {
		// TODO Auto-generated method stub
		
	}

}