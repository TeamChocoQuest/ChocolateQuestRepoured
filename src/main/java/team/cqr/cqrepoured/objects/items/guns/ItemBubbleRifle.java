package team.cqr.cqrepoured.objects.items.guns;

import net.minecraft.item.ItemStack;

public class ItemBubbleRifle extends ItemBubblePistol {

	public ItemBubbleRifle() {
		super();
	}

	@Override
	public int getMaxUses() {
		return 2 * super.getMaxUses();
	}

	@Override
	public int getCooldown() {
		return 2 * super.getCooldown();
	}

	@Override
	public double getInaccurary() {
		return 0.25D * super.getInaccurary();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10 * super.getMaxItemUseDuration(stack);
	}

}
