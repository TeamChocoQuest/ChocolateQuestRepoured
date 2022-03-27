package team.cqr.cqrepoured.inventory;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.armor.ItemBackpack;

import java.util.Objects;

public class ContainerBackpack extends Container {

	private final IInventory inventory;

	public ContainerBackpack(final int containerID, PlayerInventory playerInv, PacketBuffer data)
	{
		this(containerID, playerInv, new BackpackInventory(27, playerInv.player.getMainHandItem()));
	}

	public ContainerBackpack(final int containerID, PlayerInventory playerInv, IInventory inventory)
	{
		super(CQRContainerTypes.BACKPACK.get(), containerID);
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
			for (int m = 0; m < 9; m++) {
				this.addSlot(new Slot(inventory, m + l * 9, 8 + m * 18, 18 + l * 18) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						Item item = stack.getItem();
						if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof ShulkerBoxBlock) {
							return false;
						}
						return !(item instanceof ItemBackpack);
					}
				});
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
