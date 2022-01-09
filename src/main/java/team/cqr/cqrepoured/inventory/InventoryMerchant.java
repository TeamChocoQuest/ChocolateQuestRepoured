package team.cqr.cqrepoured.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TraderOffer;

public class InventoryMerchant implements IInventory {

	private final AbstractEntityCQR entity;
	private final PlayerEntity player;
	private final NonNullList<ItemStack> slots = NonNullList.withSize(5, ItemStack.EMPTY);
	@Nullable
	private Trade currentTrade;
	private int currentTradeIndex;

	public InventoryMerchant(AbstractEntityCQR entity, PlayerEntity player) {
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
		return new StringTextComponent(this.getName());
	}

	@Override
	public int getContainerSize() {
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
	public ItemStack getItem(int index) {
		return this.slots.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		ItemStack stack = ItemStackHelper.removeItem(this.slots, index, index == 5 ? this.slots.get(index).getCount() : count);
		if (!stack.isEmpty()) {
			this.resetTradeAndSlots();
		}
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack stack = ItemStackHelper.takeItem(this.slots, index);
		if (!stack.isEmpty()) {
			this.resetTradeAndSlots();
		}
		return stack;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		this.slots.set(index, stack);
		this.resetTradeAndSlots();
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		if (!this.entity.isAlive()) {
			return false;
		}
		return player.distanceToSqr(this.entity) <= 64.0D;
	}

	@Override
	public void setChanged() {
		this.resetTradeAndSlots();
	}

	public void resetTradeAndSlots() {
		TraderOffer traderOffer = this.entity.getTrades();
		if (traderOffer != null && !traderOffer.isEmpty()) {
			this.currentTrade = traderOffer.get(this.currentTradeIndex);
			ItemStack[] input = new ItemStack[] { this.getItem(0), this.getItem(1), this.getItem(2), this.getItem(3) };
			if (this.currentTrade != null && this.currentTrade.isUnlockedFor(this.player) && this.currentTrade.isInStock() && this.currentTrade.doItemsMatch(input)) {
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
	public void clearContent() {
		this.slots.clear();
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}
	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return true;
	}

	@Override
	public void startOpen(PlayerEntity player) {

	}

	@Override
	public void stopOpen(PlayerEntity player) {

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
