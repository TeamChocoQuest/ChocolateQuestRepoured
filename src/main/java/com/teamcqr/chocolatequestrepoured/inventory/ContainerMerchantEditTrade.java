package com.teamcqr.chocolatequestrepoured.inventory;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TradeInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ContainerMerchantEditTrade extends Container {

	private final AbstractEntityCQR entity;
	private final EntityPlayer player;
	private final IInventory tradeInventory;

	public ContainerMerchantEditTrade(AbstractEntityCQR entity, EntityPlayer player, int tradeIndex) {
		this.entity = entity;
		this.player = player;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 72 + j * 18, 60 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(player.inventory, k, 72 + k * 18, 118));
		}

		this.tradeInventory = new InventoryBasic("", false, 5);
		this.addSlotToContainer(new Slot(this.tradeInventory, 0, 74, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 1, 100, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 2, 126, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 3, 152, 12));
		this.addSlotToContainer(new Slot(this.tradeInventory, 4, 210, 12));

		Trade trade = entity.getTrades().get(tradeIndex);
		if (trade != null) {
			NonNullList<TradeInput> tradeInputs = trade.getInputItems();
			for (int i = 0; i < tradeInputs.size() && i < this.tradeInventory.getSizeInventory() - 1; i++) {
				this.tradeInventory.setInventorySlotContents(i, tradeInputs.get(i).getStack());
			}
			this.tradeInventory.setInventorySlotContents(this.tradeInventory.getSizeInventory() - 1, trade.getOutput());
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn == this.player;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		/*
		 * if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) { for (int i = 0; i < 4; i++) { playerIn.dropItem(this.tradeInventory.removeStackFromSlot(i), false); } } else { for (int
		 * i = 0; i < 4; i++) { playerIn.inventory.placeItemBackInInventory(playerIn.world, this.tradeInventory.removeStackFromSlot(i)); } }
		 */
	}

	public ItemStack getOutput() {
		return this.tradeInventory.getStackInSlot(4);
	}

	public ItemStack[] getInput() {
		ItemStack[] input = new ItemStack[4];
		for (int i = 0; i < 4; i++) {
			input[i] = this.tradeInventory.getStackInSlot(i);
		}
		return input;
	}

}
