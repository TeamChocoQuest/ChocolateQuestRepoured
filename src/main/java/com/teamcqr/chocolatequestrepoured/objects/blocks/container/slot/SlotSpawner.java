package com.teamcqr.chocolatequestrepoured.objects.blocks.container.slot;

import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSpawner extends SlotItemHandler
{
	public SlotSpawner(IItemHandler itemHandler, int index, int xPosition, int yPosition) 
	{
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
    {
        if(stack.getItem() == ModItems.SOUL_BOTTLE)
        {
        	if(stack.getTagCompound() != null)
        	{
        		if(stack.getTagCompound().getTag("EntityIn") != null)
        		{
        			return true;
        		}
        	}
        }
		return false;
    }
}