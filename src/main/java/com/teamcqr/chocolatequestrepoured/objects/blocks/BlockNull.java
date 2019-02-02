package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.init.base.BlockBase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNull extends BlockBase
{
	public static final PropertyBool PASSABLE = PropertyBool.create("passable");
	
	public BlockNull(String name, Material material) 
	{
		super(name, material);
		
		setSoundType(SoundType.GLASS);
		setHardness(2.0F);
		setResistance(30.0F);
		setHarvestLevel("hand", 0);
		setDefaultState(blockState.getBaseState().withProperty(PASSABLE, false));
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote)
		{
			if(playerIn.capabilities.isCreativeMode)
			{
				return state.getValue(PASSABLE) ? worldIn.setBlockState(pos, state.withProperty(PASSABLE, false), 3) : worldIn.setBlockState(pos, state.withProperty(PASSABLE, true), 3);
			}
		}
		return true;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
		return blockState.getValue(PASSABLE) ? null : blockState.getBoundingBox(worldIn, pos);
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PASSABLE, (meta & 1) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(PASSABLE) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PASSABLE});
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}