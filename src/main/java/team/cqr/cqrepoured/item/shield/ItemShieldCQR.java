package team.cqr.cqrepoured.item.shield;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.ShieldItem;

import javax.annotation.Nullable;

public class ItemShieldCQR extends ShieldItem {

	public static final String[] SHIELD_NAMES = { "bull", "carl", "dragonslayer", "fire", "goblin", "monking", "moon", "mummy", "pigman", "pirate", "pirate2", "rainbow", "reflective", "rusted", "skeleton_friends", "specter", "spider", "sun", "tomb", "triton", "turtle", "walker", "warped", "zombie" };

	private Item repairItem;

	public ItemShieldCQR(Properties props, int durability, @Nullable Item repairItem) {
		super(props.durability(durability));
		this.repairItem = repairItem;
	}
	
	@Override
	public boolean isRepairable(ItemStack repair) {
		return repair.getItem() == this.repairItem;
	}

	@Override
	public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
		return stack.getItem() instanceof ShieldItem;
	}

}
