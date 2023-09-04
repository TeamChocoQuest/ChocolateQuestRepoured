package team.cqr.cqrepoured.entity.trade.rules.input;

import java.util.Map;

import com.mojang.serialization.Codec;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import team.cqr.cqrepoured.init.CQRTradeRules;

public class MatchEnchants implements ITradeMatchRule {

	@Override
	public boolean matches(ItemStack input, ItemStack toMatch) {
		for (Map.Entry<Enchantment, Integer> entry : toMatch.getAllEnchantments().entrySet()) {
			if (input.getAllEnchantments().getOrDefault(entry.getKey(), -1) == entry.getValue()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Codec<? extends ITradeMatchRule> getType() {
		return CQRTradeRules.MATCH_ENCHANTS.get();
	}

}
