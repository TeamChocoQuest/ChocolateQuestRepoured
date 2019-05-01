package com.teamcqr.chocolatequestrepoured.gui.container;

import com.teamcqr.chocolatequestrepoured.gui.container.slot.SlotDisabled;
import com.teamcqr.chocolatequestrepoured.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class ContainerAlchemyBag extends Container
{
	private final int numRows = 1;
	private final int numColumns = 5;

	public ContainerAlchemyBag(InventoryPlayer playerInv, IInventory inventory)
	{
		int currentItemIndex = playerInv.currentItem;
		
		for(int j = 0; j < this.numColumns; ++j)
		{
			this.addSlotToContainer(new Slot(inventory, j, 44 + j * 18, 20)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{	
					if(stack.getItem() == ModItems.POTION_HEALING)
					{
						return true;
					}
				    return stack.getItem() instanceof ItemPotion ? true : false;
				}
			});
		}
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInv, x + y*9 + 9, 8 + x*18, 51 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			if(x == currentItemIndex)
			{
				this.addSlotToContainer(new SlotDisabled(playerInv, x, 8 + x*18, 109));
			}
			else
			{
				this.addSlotToContainer(new Slot(playerInv, x, 8 + x*18, 109));
			}
		}							
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
	}
}