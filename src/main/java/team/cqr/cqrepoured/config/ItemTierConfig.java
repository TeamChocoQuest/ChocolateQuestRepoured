package team.cqr.cqrepoured.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ItemTierConfig {

	public final IntValue uses;
	public final DoubleValue speed;
	public final DoubleValue attackDamageBonus;
	public final IntValue level;
	public final IntValue enchantmentValue;

	public ItemTierConfig(ForgeConfigSpec.Builder builder, String name, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue) {
		builder.comment("").push(name);
		this.uses = builder.comment("").defineInRange("uses", uses, 1, Integer.MAX_VALUE);
		this.speed = builder.comment("").defineInRange("speed", speed, 0.0D, Double.MAX_VALUE);
		this.attackDamageBonus = builder.comment("").defineInRange("attackDamageBonus", attackDamageBonus, 0.0D, Double.MAX_VALUE);
		this.level = builder.comment("").defineInRange("level", level, 1, Integer.MAX_VALUE);
		this.enchantmentValue = builder.comment("").defineInRange("enchantmentValue", enchantmentValue, 1, Integer.MAX_VALUE);
		builder.pop();
	}

}
