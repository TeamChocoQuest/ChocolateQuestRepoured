package com.teamcqr.chocolatequestrepoured.init.base;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockExporterChestBase extends Block implements IHasModel {

	public BlockExporterChestBase(String name, Material material) 
	{
		super(material);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CQRMain.CQRExporterChestTab);
		
		ModBlocks.BLOCKS.add(this);
		ModBlocks.LOOT_CHEST_BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() 
	{
		CQRMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");		
	}
}
