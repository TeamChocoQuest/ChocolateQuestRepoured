package com.tiviacz.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import com.tiviacz.chocolatequestrepoured.ChocolateQuestRepouredMain;
import com.tiviacz.chocolatequestrepoured.init.ModBlocks;
import com.tiviacz.chocolatequestrepoured.network.PacketRequestUpdateTable;
import com.tiviacz.chocolatequestrepoured.network.PacketUpdateTable;
import com.tiviacz.chocolatequestrepoured.objects.blocks.BlockTable;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTable extends TileEntity
{
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				lastChangeTime = world.getTotalWorldTime();
				ChocolateQuestRepouredMain.network.sendToAllAround(new PacketUpdateTable(TileEntityTable.this), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
			}
		}
	};
	public long lastChangeTime;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setLong("lastChangeTime", lastChangeTime);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void onLoad() {
		if (world.isRemote) {
			ChocolateQuestRepouredMain.network.sendToServer(new PacketRequestUpdateTable(this));
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() 
	{
		return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		lastChangeTime = compound.getLong("lastChangeTime");
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
	}
}
