package com.teamcqr.chocolatequestrepoured.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySyncClient extends TileEntity
{
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
	    return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
	    readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
	    return writeToNBT(new NBTTagCompound());
	}
	
	private void notifyBlockUpdate() 
	{
		IBlockState state = getWorld().getBlockState(getPos());
		getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}

	@Override
	public void markDirty() 
	{
		super.markDirty();
		notifyBlockUpdate();
	} 
}