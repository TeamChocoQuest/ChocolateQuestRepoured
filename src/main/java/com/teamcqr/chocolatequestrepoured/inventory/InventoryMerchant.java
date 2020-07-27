package com.teamcqr.chocolatequestrepoured.inventory;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TraderOffer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryMerchant implements IInventory {

	private final AbstractEntityCQR entity;
	private final EntityPlayer player;
	private final NonNullList<ItemStack> slots = NonNullList.withSize(5, ItemStack.EMPTY);
	@Nullable
	private Trade currentTrade;
	private int currentTradeIndex;

	public InventoryMerchant(AbstractEntityCQR entity, EntityPlayer player) {
		this.entity = entity;
		this.player = player;
	}

	@Override
	public String getName() {
		return this.entity.getName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return this.slots.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.slots) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.slots.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = ItemStackHelper.getAndSplit(this.slots, index, index == 5 ? this.slots.get(index).getCount() : count);
		if (!stack.isEmpty()) {
			this.resetTradeAndSlots();
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = ItemStackHelper.getAndRemove(this.slots, index);
		if (!stack.isEmpty()) {
			this.resetTradeAndSlots();
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		this.slots.set(index, stack);
		this.resetTradeAndSlots();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player == this.player;
	}

	@Override
	public void markDirty() {
		this.resetTradeAndSlots();
	}

	public void resetTradeAndSlots() {
		TraderOffer traderOffer = this.entity.getTrades();
		if (traderOffer != null && !traderOffer.isEmpty()) {
			this.currentTrade = traderOffer.get(this.currentTradeIndex);
			if (this.currentTrade != null && this.currentTrade.doItemsMatch(new ItemStack[] { this.getStackInSlot(0), this.getStackInSlot(1), this.getStackInSlot(2), this.getStackInSlot(3) })) {
				this.slots.set(4, this.currentTrade.getOutput());
			} else {
				this.slots.set(4, ItemStack.EMPTY);
			}
		} else {
			this.currentTrade = null;
			this.slots.set(4, ItemStack.EMPTY);
		}
	}

	public void setCurrentTradeIndex(int index) {
		if (this.currentTradeIndex != index) {
			this.currentTradeIndex = index;
			this.resetTradeAndSlots();
		}
	}

	public Trade getCurrentTrade() {
		return this.currentTrade;
	}

	public int getCurrentTradeIndex() {
		return this.currentTradeIndex;
	}

	@Override
	public void clear() {
		this.slots.clear();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

}
