package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.init.CQRContainerTypes;

public class AlchemyBagContainer extends Container {

	//private final IInventory inventory;
	private final ItemStack stack;
	private final Hand hand;

	public AlchemyBagContainer(final int containerID, PlayerInventory playerInv, PacketBuffer data)
	{
		this(containerID, playerInv, data.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	public AlchemyBagContainer(final int containerID, PlayerInventory playerInv, Hand hand)
	{
		super(CQRContainerTypes.ALCHEMY_BAG.get(), containerID);

		this.hand = hand;
		this.stack = playerInv.player.getItemInHand(hand);
		LazyOptional<IItemHandler> cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		int currentItem = playerInv.selected;

		//this.inventory = inventory;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for(int k = 0; k < 9; k++)
		{
			if(k != currentItem)
			{
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109));
			} else {
				this.addSlot(new Slot(playerInv, k, 8 + k * 18, 109)
				{
					@Override
					public boolean mayPickup(PlayerEntity playerIn) {
						return false;
					}
				});
			}
		}

		if(cap.isPresent())
		{
			IItemHandler inventory = cap.resolve().get();

			for (int l = 0; l < 5; l++) {
				this.addSlot(new SlotItemHandler(inventory, l, 44 + l * 18, 20) {
					@Override
					public boolean mayPlace(ItemStack stack) {
						return stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION;
					}
				});
			}
		}
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		//return this.inventory.stillValid(playerIn);
		return playerIn.getItemInHand(hand).getItem() == this.stack.getItem();
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
