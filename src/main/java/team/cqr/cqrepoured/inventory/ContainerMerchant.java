package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.npc.trading.Trade;
import team.cqr.cqrepoured.objects.npc.trading.TradeInput;
import team.cqr.cqrepoured.util.CraftingHelper;

public class ContainerMerchant extends Container {

	private final AbstractEntityCQR entity;
	private final EntityPlayer player;
	private final InventoryMerchant merchantInventory;

	public ContainerMerchant(AbstractEntityCQR entity, EntityPlayer player) {
		this.entity = entity;
		this.player = player;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 139 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(player.inventory, k, 139 + k * 18, 142));
		}

		this.merchantInventory = new InventoryMerchant(entity, player);
		this.addSlotToContainer(new Slot(this.merchantInventory, 0, 141, 37));
		this.addSlotToContainer(new Slot(this.merchantInventory, 1, 167, 37));
		this.addSlotToContainer(new Slot(this.merchantInventory, 2, 193, 37));
		this.addSlotToContainer(new Slot(this.merchantInventory, 3, 219, 37));
		this.addSlotToContainer(new SlotMerchantOutput(player, this.merchantInventory, 4, 277, 37));
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.merchantInventory.resetTradeAndSlots();
		super.onCraftMatrixChanged(inventoryIn);
	}

	public void setCurrentTradeIndex(int index) {
		this.merchantInventory.setCurrentTradeIndex(index);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn == this.player;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return !(slotIn instanceof SlotMerchantOutput);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack oldStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack newStack = slot.getStack();
			oldStack = newStack.copy();

			if (index == 40) {
				if (!this.mergeItemStack(newStack, 0, 36, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(newStack, oldStack);
			} else if (index > 35) {
				if (!this.mergeItemStack(newStack, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(newStack, 36, 40, false)) {
				if (index > 26) {
					if (this.mergeItemStack(newStack, 0, 27, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (this.mergeItemStack(newStack, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (newStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (newStack.getCount() == oldStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, newStack);
		}

		return oldStack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).hasDisconnected()) {
			for (int i = 0; i < 4; i++) {
				playerIn.dropItem(this.merchantInventory.removeStackFromSlot(i), false);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				playerIn.inventory.placeItemBackInInventory(playerIn.world, this.merchantInventory.removeStackFromSlot(i));
			}
		}
	}

	public void updateInputsForTrade(int tradeIndex) {
		Trade trade = this.entity.getTrades().get(tradeIndex);
		if (trade != null) {
			NonNullList<TradeInput> input = trade.getInputItems();

			for (int i = 0; i < 4; i++) {
				ItemStack stack = this.merchantInventory.getStackInSlot(i);
				if (!stack.isEmpty()) {
					if (!this.mergeItemStack(stack, 0, 36, true)) {
						return;
					}

					this.merchantInventory.setInventorySlotContents(i, stack);
				}
			}

			for (int i = 0; i < 4 && i < input.size(); i++) {
				this.fillSlot(i, input.get(i));
			}
		}
	}

	private void fillSlot(int slotIndex, TradeInput input) {
		if (!input.getStack().isEmpty()) {
			for (int i = 0; i < 36; i++) {
				ItemStack stack1 = ((Slot) this.inventorySlots.get(i)).getStack();
				if (!stack1.isEmpty() && CraftingHelper.areItemStacksEqualIgnoreCount(input.getStack(), stack1, input.ignoreMeta(), input.ignoreNBT())) {
					ItemStack stack2 = this.merchantInventory.getStackInSlot(slotIndex);
					int j = stack2.isEmpty() ? 0 : stack2.getCount();
					int k = Math.min(input.getStack().getMaxStackSize() - j, stack1.getCount());
					ItemStack stack3 = stack1.copy();
					int l = j + k;
					stack1.shrink(k);
					stack3.setCount(l);
					this.merchantInventory.setInventorySlotContents(slotIndex, stack3);
					if (l >= input.getStack().getMaxStackSize()) {
						break;
					}
				}
			}
		}

	}

}
