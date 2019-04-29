package com.teamcqr.chocolatequestrepoured.gui.inventory;

import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class InventoryBadge extends InventoryBasic
{
	private Entity entity;
	
    public InventoryBadge(String title, int slotCount, Entity entity)
    {
        super(title, false, slotCount);
        
        this.entity = entity;
        this.readFromNBT();
        this.writeToNBT();
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if(stack.getItem() == ModItems.BADGE)
        {
        	return false;
        }
    	return true;
    }

    @Override
    public void markDirty()
    {
    	writeToNBT();
	    super.markDirty();
    }
    
    public void writeToNBT()
    {
    	NBTTagCompound tag = this.entity.getEntityData();
    	
    	NBTTagList itemList = new NBTTagList();
    	
    	for(int i = 0; i < this.getSizeInventory(); i++)
    	{
    		ItemStack stack = this.getStackInSlot(i);
    		
    		if(stack != null)
    		{
    			NBTTagCompound slotTag = new NBTTagCompound();
    			slotTag.setInteger("Index", i);
    			stack.writeToNBT(slotTag);
    			itemList.appendTag(slotTag);
    		}
    	}
    	tag.setTag("Items", itemList);
    }
    
    public void readFromNBT()
    {
    	NBTTagCompound tag = this.entity.getEntityData();
    	
    	if(!tag.hasKey("Items"))
    	{
    		writeToNBT();
    		return;
    	}
    	
    	NBTTagList list = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
    	
    	if(list == null)
    	{
    		writeToNBT();
    		return;
    	}
    	
    	for(int i = 0; i < list.tagCount(); i++)
    	{
    		NBTTagCompound entry = list.getCompoundTagAt(i);
    		int index = entry.getInteger("Index");
    		setInventorySlotContents(index, new ItemStack(entry));
    	}
    }
}