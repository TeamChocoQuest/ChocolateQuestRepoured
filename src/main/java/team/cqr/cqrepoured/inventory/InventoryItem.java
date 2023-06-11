package team.cqr.cqrepoured.inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryItem extends Inventory {

	private final ItemStack stack;
	private final InteractionHand hand;
	private final boolean creativeOnly;

	public InventoryItem(ItemStack stack, InteractionHand hand, int size, boolean creativeOnly) {
		super(size);
		this.stack = stack;
		this.hand = hand;
		this.creativeOnly = creativeOnly;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		if (this.creativeOnly && !pPlayer.isCreative()) {
			return false;
		}
		return pPlayer.getItemInHand(this.hand) == this.stack;
	}

}
