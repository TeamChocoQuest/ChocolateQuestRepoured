package com.teamcqr.chocolatequestrepoured.init.base;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.item.Item;

public class ItemDungeonPlacer extends Item implements IHasModel {
	
	public ItemDungeonPlacer(DungeonBase dungeon) {
		setUnlocalizedName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setRegistryName("dungeon_placer_" + dungeon.getDungeonName().toLowerCase());
		setCreativeTab(CQRMain.CQRDungeonPlacerTab);
		setMaxStackSize(1);
		
		ModItems.DUNGEON_PLACERS.add(this);
	}
	
	@Override
	public void registerModels() {
		CQRMain.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
