package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

public class ContainerSpawner extends Container {

	private final TileEntitySpawner tileEntity;
	private final IInventory inventory;

	/** Client **/
	public ContainerSpawner(final int containerID, PlayerInventory playerInv, PacketBuffer data) {
		this(containerID, playerInv, (TileEntitySpawner) playerInv.player.level.getBlockEntity(data.readBlockPos()));
	}

	/** Server **/
	public ContainerSpawner(final int containerID, PlayerInventory playerInv, TileEntitySpawner tileEntity) {
		super(CQRContainerTypes.SPAWNER.get(), containerID);
		this.tileEntity = tileEntity;
		this.inventory = tileEntity.getInventory();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				this.addSlot(new Slot(inventory, m + l * 3, 62 + m * 18, 17 + l * 18) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return stack.getItem() instanceof ItemSoulBottle && stack.hasTag()
								&& stack.getTag().contains("EntityIn");
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

		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.moveItemStackTo(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	public TileEntitySpawner getTileEntity() {
		return tileEntity;
	}

}
