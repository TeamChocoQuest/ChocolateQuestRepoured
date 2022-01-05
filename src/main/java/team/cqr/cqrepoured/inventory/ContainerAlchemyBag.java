package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItem;

public class ContainerAlchemyBag extends Container {

	private final ItemStack stack;
	private final Hand hand;

	public ContainerAlchemyBag(ContainerType<?> containerType, final int containerID, PlayerInventory playerInv, ItemStack stack, Hand hand) {
		super(containerType, containerID);
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
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			if (k != currentItemIndex) {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109));
			} else {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109) {

					@Override
					public boolean mayPickup(PlayerEntity playerIn) {
						return false;
					}

				});
			}
		}

		for (int l = 0; l < 5; l++) {
			int index = l;
			this.addSlot(new SlotItemHandler(inventory, l, 44 + l * 18, 20) {
				
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION;
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
