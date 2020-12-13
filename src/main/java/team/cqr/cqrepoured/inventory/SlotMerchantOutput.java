package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.objects.npc.trading.Trade;

public class SlotMerchantOutput extends Slot {

	private final EntityPlayer player;
	private final InventoryMerchant merchantInventory;
	private int removeCount;

	public SlotMerchantOutput(EntityPlayer player, InventoryMerchant tradeInventory, int index, int xPosition, int yPosition) {
		super(tradeInventory, index, xPosition, yPosition);
		this.merchantInventory = tradeInventory;
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);
		this.removeCount = 0;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		Trade trade = this.merchantInventory.getCurrentTrade();

		if (trade != null) {
			ItemStack[] input = new ItemStack[] { this.merchantInventory.getStackInSlot(0), this.merchantInventory.getStackInSlot(1), this.merchantInventory.getStackInSlot(2), this.merchantInventory.getStackInSlot(3) };
			if (trade.doTransaction(thePlayer, input)) {
				this.merchantInventory.setInventorySlotContents(0, input[0]);
				this.merchantInventory.setInventorySlotContents(1, input[1]);
				this.merchantInventory.setInventorySlotContents(2, input[2]);
				this.merchantInventory.setInventorySlotContents(3, input[3]);
			}
		}

		return stack;
	}

}
