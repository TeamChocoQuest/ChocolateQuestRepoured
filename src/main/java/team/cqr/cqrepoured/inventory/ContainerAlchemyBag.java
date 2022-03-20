package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class ContainerAlchemyBag extends Container {

	private final IInventory inventory;

	/** Client **/
	public ContainerAlchemyBag(final int containerID, PlayerInventory playerInv, PacketBuffer data) {
		this(containerID, playerInv, new Inventory(9));
	}

	/** Server **/
	public ContainerAlchemyBag(final int containerID, PlayerInventory playerInv, IInventory inventory) {
		super(CQRContainerTypes.ALCHEMY_BAG.get(), containerID);
		this.inventory = inventory;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		// TODO prevent moving container item
		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109));
		}

		for (int l = 0; l < 5; l++) {
			this.addSlot(new Slot(inventory, l, 44 + l * 18, 20) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION;
				}
			});
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
