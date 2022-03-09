package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class InventoryItem extends Inventory {

	private final ItemStack stack;
	private final Hand hand;
	private final boolean creativeOnly;

	public InventoryItem(ItemStack stack, Hand hand, int size, boolean creativeOnly) {
		super(size);
		this.stack = stack;
		this.hand = hand;
		this.creativeOnly = creativeOnly;
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		return pPlayer.getItemInHand(this.hand) == this.stack;
	}

}
