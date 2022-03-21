package team.cqr.cqrepoured.item.gun;

import net.minecraft.item.ItemStack;

public class ItemBubbleRifle extends ItemBubblePistol {

	public ItemBubbleRifle(Properties properties) {
		super(properties.durability(400));
	}

	//@Override
	//public int getMaxUses() {
	//	return 2 * super.getMaxUses();
	//}

	@Override
	public int getCooldown() {
		return 2 * super.getCooldown();
	}

	@Override
	public double getInaccurary() {
		return 0.25D * super.getInaccurary();
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 10 * super.getUseDuration(stack);
	}

}
