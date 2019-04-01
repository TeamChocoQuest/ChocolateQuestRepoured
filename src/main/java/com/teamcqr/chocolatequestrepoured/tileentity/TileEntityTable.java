package com.teamcqr.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTable extends TileEntitySyncClient
{
	private float rotation = 0F;
	public ItemStackHandler inventory = new ItemStackHandler(1)
	{
		@Override
		protected void onContentsChanged(int slot) 
		{
			if(!world.isRemote)
			{
				markDirty();
			}
		}
		
		@Override
	    public int getSlotLimit(int slot)
	    {
	        return 1;
	    }
	};
	
	public void setRotation(EntityPlayer playerIn)
	{
		int direction = MathHelper.floor((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		rotation = direction * 90F;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	@Override
    public ITextComponent getDisplayName()
    {
		Style style = new Style();
		ITextComponent itemName = new TextComponentString(inventory.getStackInSlot(0).getDisplayName());
		
        if(inventory.getStackInSlot(0).hasDisplayName())
        {
        	return itemName.setStyle(style);
        }
        else
        {
        	return null;
        }
    }
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return(oldState.getBlock() != newSate.getBlock());
    }
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setFloat("rotation", rotation);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		rotation = compound.getFloat("rotation");
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) 
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) 
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}
}