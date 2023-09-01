package team.cqr.cqrepoured.entity.trade.rules.input;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.world.item.ItemStack;

public interface ITradeMatchRule {
	
	public boolean matches(final @Nonnull ItemStack input, final @Nonnull ItemStack toMatch);
	
	public Codec<? extends ITradeMatchRule> getType();
	
}
