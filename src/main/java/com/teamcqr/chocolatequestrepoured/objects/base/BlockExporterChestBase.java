package com.teamcqr.chocolatequestrepoured.objects.base;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

import net.minecraft.block.material.Material;

public class BlockExporterChestBase extends BlockBase
{
	public BlockExporterChestBase(String name, Material material) 
	{
		super(name, material);
		
		setCreativeTab(CQRMain.CQRExporterChestTab);
		
		ModBlocks.LOOT_CHEST_BLOCKS.add(this);
	}
}
