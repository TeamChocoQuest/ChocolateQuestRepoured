package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockPillarDungeonBrick extends BlockRotatedPillar implements IHasModel
{
	public BlockPillarDungeonBrick(String name, Material materialIn) 
	{
		super(materialIn);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CQRMain.CQRBlocksTab);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		
		setSoundType(SoundType.STONE);
		setHardness(-1F);
	}
	
	@Override
	public void registerModels() 
	{
		CQRMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");		
	}
}