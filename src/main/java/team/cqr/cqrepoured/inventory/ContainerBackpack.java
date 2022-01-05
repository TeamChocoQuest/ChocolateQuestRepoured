package team.cqr.cqrepoured.inventory;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItem;
import team.cqr.cqrepoured.item.armor.ItemBackpack;

public class ContainerBackpack extends Container {

	private final ItemStack stack;
	private final Hand hand;

	public ContainerBackpack(final int containerID, PlayerInventory playerInv, ItemStack stack, Hand hand) {
		super(ContainerType.GENERIC_9x3, containerID);
		this.stack = stack;
		this.hand = hand;
		LazyOptional<IItemHandler> lOpCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		IItemHandler inventory = null;
		if(lOpCap.isPresent()) {
			inventory = lOpCap.resolve().get();
		}
		int currentItemIndex = playerInv.selected;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			if (k != currentItemIndex) {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
			} else {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142) {

					@Override
					public boolean mayPickup(PlayerEntity playerIn) {
						return false;
					}

				});
			}
		}

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 9; m++) {
				int index = m + l * 9;
				this.addSlot(new SlotItemHandler(inventory, m + l * 9, 8 + m * 18, 18 + l * 18) {

					@Override
					public boolean mayPlace(ItemStack stack) {
						return !(stack.getItem() == (Blocks.SHULKER_BOX.asItem())) && !(stack.getItem() instanceof ItemBackpack);
					}

					@Override
					public void setChanged() {
						super.setChanged();
						if (this.getItemHandler() instanceof CapabilityItemHandlerItem) {
							((CapabilityItemHandlerItem) this.getItemHandler()).onContentsChanged(index);
						}
					}

				});
			}
		}
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return playerIn.getItemInHand(this.hand) == this.stack;
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
