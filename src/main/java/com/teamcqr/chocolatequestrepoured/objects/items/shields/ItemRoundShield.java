package com.teamcqr.chocolatequestrepoured.objects.items.shields;

import net.minecraft.item.Item;

public class ItemRoundShield extends ItemShieldCQR {
	
	/*private static final String[] subShieldNames = new String[] {
		
	};*/
	
	//private static final int shieldCount = 9;

	public ItemRoundShield(int durability, Item repairItem) {
		super(durability, repairItem);
	}
	
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	
	/*@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		//super.getSubItems(tab, items);
		if (this.isInCreativeTab(tab)) {
			for(int i = 0; i < shieldCount; i++) {
				//Lang path string: item.cqr_shield_rXX.name
			}
		}
	}*/

}
