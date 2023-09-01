package team.cqr.cqrepoured.entity.trade.rules.input;

import com.mojang.serialization.Codec;

import net.minecraft.world.item.ItemStack;

public class MatchEnchantmentList implements ITradeMatchRule {
	
	public static final Codec<ITradeMatchRule> CODEC = null;

	@Override
	public boolean matches(ItemStack input, ItemStack toMatch) {
		return false;
	}

	@Override
	public Codec<? extends ITradeMatchRule> getType() {
		return CODEC;
	}

}
