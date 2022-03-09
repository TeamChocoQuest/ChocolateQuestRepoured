package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class ContainerBadge extends Container {

	private final IInventory inventory;

	/** Client **/
	public ContainerBadge(final int containerID, PlayerInventory playerInv) {
		this(containerID, playerInv, new Inventory(9));
	}

	/** Server **/
	public ContainerBadge(final int containerID, PlayerInventory playerInv, IInventory inventory) {
		super(CQRContainerTypes.BADGE.get(), containerID);
		this.inventory = inventory;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// TODO prevent moving container item
		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				this.addSlot(new Slot(inventory, m + l * 3, 62 + m * 18, 17 + l * 18));
			}
		}
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return this.inventory.stillValid(playerIn);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot == null) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (index > 35) {
			if (!this.moveItemStackTo(stack, 0, 36, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.moveItemStackTo(stack, 36, this.slots.size(), false)) {
				return ItemStack.EMPTY;
			}
		}

		slot.setChanged();
		return stack;
	}

}
