package com.teamcqr.chocolatequestrepoured.objects.items.shields;

import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemShield extends ItemShieldCQR {
	
	public static final String[] subShieldNames = new String[] {
		"bull",
		"carl",
		"dragonslayer",
		"fire",
		"goblin",
		"monking",
		"moon",
		"mummy",
		"pigman",
		"pirate",
		"pirate2",
		"rainbow",
		"reflective",
		"rusted",
		"skeleton_friends",
		"specter",
		"spider",
		"sun",
		"tomb",
		"triton",
		"turtle",
		"walker",
		"warped",
		"zombie"
	};
	
	//private static final int shieldCount = 9;

	public ItemShield(int durability, Item repairItem) {
		super(durability, repairItem);
	}
	
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		//super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab)) {
			for(int i = 0; i < subShieldNames.length; i++) {
				ItemStack stack = new ItemStack(ModItems.SHIELD, 1, i);
				
				items.add(stack);
			}
		}
	}

}
