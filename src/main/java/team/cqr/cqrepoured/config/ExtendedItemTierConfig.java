package team.cqr.cqrepoured.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class ExtendedItemTierConfig {

	public final DoubleValue fixedAttackDamageBonus;
	public final DoubleValue attackSpeedBonus;
	public final DoubleValue movementSpeedBonus;

	public ExtendedItemTierConfig(ForgeConfigSpec.Builder builder, String name, float fixedAttackDamageBonus, float attackSpeedBonus, double movementSpeedBonus) {
		builder.comment("").push(name);
		this.fixedAttackDamageBonus = builder.comment("").defineInRange("fixedAttackDamageBonus", fixedAttackDamageBonus, Double.MIN_VALUE, Double.MAX_VALUE);
		this.attackSpeedBonus = builder.comment("").defineInRange("attackSpeedBonus", attackSpeedBonus, Double.MIN_VALUE, Double.MAX_VALUE);
		this.movementSpeedBonus = builder.comment("").defineInRange("movementSpeedBonus", movementSpeedBonus, Double.MIN_VALUE, Double.MAX_VALUE);
		builder.pop();
	}

}
