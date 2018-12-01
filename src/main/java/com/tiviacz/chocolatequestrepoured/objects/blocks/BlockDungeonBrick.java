package com.tiviacz.chocolatequestrepoured.objects.blocks;

import com.tiviacz.chocolatequestrepoured.init.base.BlockBase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDungeonBrick extends BlockBase
{

	public BlockDungeonBrick(String name, Material material) 
	{
		super(name, material);
		setSoundType(SoundType.STONE);
	}
	
	@Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return -1;
    }

}
