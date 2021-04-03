package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItem;

public class ContainerBadge extends Container {

	private final ItemStack stack;
	private final EnumHand hand;

	public ContainerBadge(InventoryPlayer playerInv, ItemStack stack, EnumHand hand) {
		this.stack = stack;
		this.hand = hand;
		IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int currentItemIndex = playerInv.currentItem;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			if (k != currentItemIndex) {
				this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
			} else {
				this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142) {

					@Override
					public boolean canTakeStack(EntityPlayer playerIn) {
						return false;
					}

				});
			}
		}

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				int index = m + l * 3;
				this.addSlotToContainer(new SlotItemHandler(inventory, m + l * 3, 62 + m * 18, 17 + l * 18) {

					@Override
					public void onSlotChanged() {
						super.onSlotChanged();
						if (this.getItemHandler() instanceof CapabilityItemHandlerItem) {
							((CapabilityItemHandlerItem) this.getItemHandler()).onContentsChanged(index);
						}
					}

				});
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.isCreative();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot == null) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (index > 35) {
			if (!this.mergeItemStack(stack, 0, 36, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.mergeItemStack(stack, 36, this.inventorySlots.size(), false)) {
				return ItemStack.EMPTY;
			}
		}

		slot.onSlotChanged();
		return stack;
	}

}
