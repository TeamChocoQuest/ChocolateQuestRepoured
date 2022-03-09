package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.ItemSoulBottle;

public class ContainerBossBlock extends Container {

	private final IInventory inventory;

	/** Client **/
	public ContainerBossBlock(final int containerID, PlayerInventory playerInv) {
		this(containerID, playerInv, new Inventory(1));
	}

	/** Server **/
	public ContainerBossBlock(final int containerID, PlayerInventory playerInv, IInventory inventory) {
		super(CQRContainerTypes.BOSS_BLOCK.get(), containerID);
		this.inventory = inventory;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 108));
		}

		this.addSlot(new Slot(inventory, 0, 80, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemSoulBottle && stack.hasTag() && stack.getTag().contains("EntityIn");
			}
		});
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return this.inventory.stillValid(playerIn);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.moveItemStackTo(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else if (this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
				return itemstack;
			} else if (index > 26) {
				if (this.moveItemStackTo(itemstack1, 0, 27, false)) {
					return itemstack;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 27, 36, false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

}
