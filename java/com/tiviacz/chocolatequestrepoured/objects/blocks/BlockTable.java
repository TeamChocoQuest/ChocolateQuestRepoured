package com.tiviacz.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.tiviacz.chocolatequestrepoured.init.ModBlocks;
import com.tiviacz.chocolatequestrepoured.init.base.BlockBase;
import com.tiviacz.chocolatequestrepoured.tileentity.TileEntityTable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTable extends BlockBase implements ITileEntityProvider
{
	public static final PropertyInteger STATE = PropertyInteger.create("state", 0, 1);
	
	public BlockTable(String name, Material material) 
	{
		super(name, material);
		
		setSoundType(SoundType.WOOD);
		setHardness(2.0F);
		setResistance(15.0F);
		setHarvestLevel("axe", 0);
		
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STATE, Integer.valueOf(meta));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(STATE)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {STATE});
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {	
    	
    /*	if(worldIn.getBlockState(pos.south()).getBlock() == ModBlocks.TABLE && worldIn.getBlockState(pos.north()).getBlock() == ModBlocks.TABLE)
    	{
    	//	worldIn.setBlockState(pos, ModBlocks.TABLE.getDefaultState().withProperty(STATE, 1));
    		worldIn.setBlockToAir(pos);
    	} */
    	
    	//TODO
    	
    	if(worldIn.getBlockState(pos.west()).getBlock() == ModBlocks.TABLE && worldIn.getBlockState(pos.east()).getBlock() == ModBlocks.TABLE 
    	&& worldIn.getBlockState(pos.south()).getBlock() == ModBlocks.TABLE && worldIn.getBlockState(pos.north()).getBlock() == ModBlocks.TABLE)
    	{
    		worldIn.setBlockState(pos, ModBlocks.TABLE.getDefaultState().withProperty(STATE, 1));
    	}
    	
    	if(worldIn.getBlockState(pos.east()).getBlock() == ModBlocks.TABLE && worldIn.getBlockState(pos.west()).getBlock() == ModBlocks.TABLE)
    	{
    		worldIn.setBlockState(pos, ModBlocks.TABLE.getDefaultState().withProperty(STATE, 1));
    	}
    		
    	else
    	{
    		worldIn.setBlockState(pos, ModBlocks.TABLE.getDefaultState().withProperty(STATE, 0));
    	}  
    }
    
    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    		TileEntityTable tile = getTileEntity(worldIn, pos);
    		ItemStack helditem = playerIn.getHeldItem(EnumHand.MAIN_HAND);
    		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
    		ItemStack pillar = itemHandler.getStackInSlot(0);
    		Random rand = new Random();
    		
    		if(pillar.isEmpty() && !helditem.isEmpty()) 
    		{
    			worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);       	
    			if(!worldIn.isRemote)
    			{
    				playerIn.setHeldItem(hand, itemHandler.insertItem(0, helditem, false));
    			}    	
    		}
    		
    		if(!pillar.isEmpty()) 
    	    { 
    			worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);       		        		
    			if(!worldIn.isRemote)
    			{
    				playerIn.inventory.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
    			}
    	    }
    				
    		tile.markDirty();   	
    		return true;
    }
    
    @Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) 
    {
		TileEntityTable tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		ItemStack stack = itemHandler.getStackInSlot(0);
		
		if (!stack.isEmpty()) 
		{
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntity(item);
		}
		super.breakBlock(world, pos, state);
	}
    
    public TileEntityTable getTileEntity(IBlockAccess world, BlockPos pos) 
	{
		return (TileEntityTable)world.getTileEntity(pos);
	}
    
    @Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityTable();
	}

}
