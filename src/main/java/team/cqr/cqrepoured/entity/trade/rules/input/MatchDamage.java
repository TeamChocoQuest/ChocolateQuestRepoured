package team.cqr.cqrepoured.entity.trade.rules.input;

import com.mojang.serialization.Codec;

import net.minecraft.world.item.ItemStack;

public class MatchDamage implements ITradeMatchRule {
	
	public static final Codec<MatchEnchantmentList> CODEC = null;

	@Override
	public boolean matches(ItemStack input, ItemStack toMatch) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Codec<? extends ITradeMatchRule> getType() {
		return CODEC;
	}

}
