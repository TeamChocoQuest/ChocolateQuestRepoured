package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.objects.base.BlockBase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDungeonBrick extends BlockBase
{
	public BlockDungeonBrick(String name, Material material) 
	{
		super(name, material);
		
		setSoundType(SoundType.STONE);
		//setResistance(999999F);
		setResistance(Float.MAX_VALUE);
		//setHardness(999999F);
		setHardness(Float.MAX_VALUE);
	}
}