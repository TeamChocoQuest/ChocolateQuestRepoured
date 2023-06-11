package team.cqr.cqrepoured.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.trade.Trade;

public class SlotMerchantOutput extends Slot {

	private final Player player;
	private final InventoryMerchant merchantInventory;
	private int removeCount;

	public SlotMerchantOutput(Player player, InventoryMerchant tradeInventory, int index, int xPosition, int yPosition) {
		super(tradeInventory, index, xPosition, yPosition);
		this.merchantInventory = tradeInventory;
		this.player = player;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack remove(int amount) {
		if (this.hasItem()) {
			this.removeCount += Math.min(amount, this.getItem().getCount());
		}

		return super.remove(amount);
	}

	//TODO: Is this correct?
	@Override
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	//@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCraftedBy(this.player.level, this.player, this.removeCount);
		this.removeCount = 0;
	}

	@Override
	public ItemStack onTake(Player thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		Trade trade = this.merchantInventory.getCurrentTrade();

		if (trade != null) {
			ItemStack[] input = new ItemStack[] { this.merchantInventory.getItem(0), this.merchantInventory.getItem(1), this.merchantInventory.getItem(2), this.merchantInventory.getItem(3) };
			if (trade.doTransaction(thePlayer, input)) {
				this.merchantInventory.setItem(0, input[0]);
				this.merchantInventory.setItem(1, input[1]);
				this.merchantInventory.setItem(2, input[2]);
				this.merchantInventory.setItem(3, input[3]);
			}
		}

		return stack;
	}

}
