package com.tiviacz.chocolatequestrepoured.objects.blocks;

import com.tiviacz.chocolatequestrepoured.init.base.BlockBase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDungeonBrick extends BlockBase
{
	public BlockDungeonBrick(String name, Material material) 
	{
		super(name, material);
		
		setSoundType(SoundType.STONE);
		setHardness(-1F);
	}
}