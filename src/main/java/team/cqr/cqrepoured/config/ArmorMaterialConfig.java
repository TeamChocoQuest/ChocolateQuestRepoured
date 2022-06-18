package team.cqr.cqrepoured.config;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicates;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ArmorMaterialConfig {

	public final IntValue durability;
	public final ConfigValue<List<? extends Integer>> defense;
	public final IntValue enchantmentValue;
	public final DoubleValue toughness;
	public final DoubleValue knockbackResistance;

	public ArmorMaterialConfig(ForgeConfigSpec.Builder builder, String name, int durability, Integer[] defense, int enchantmentValue, float toughness, float knockbackResistance) {
		builder.comment("").push(name);
		this.durability = builder.comment("").defineInRange("durability", durability, 1, Integer.MAX_VALUE);
		this.defense = builder.comment("").defineList("defense", Arrays.asList(defense), Predicates.alwaysTrue());
		this.enchantmentValue = builder.comment("").defineInRange("enchantmentValue", enchantmentValue, 0, Integer.MAX_VALUE);
		this.toughness = builder.comment("").defineInRange("toughness", toughness, 0.0D, Double.MAX_VALUE);
		this.knockbackResistance = builder.comment("").defineInRange("knockbackResistance", knockbackResistance, Double.MIN_VALUE, Double.MAX_VALUE);
		builder.pop();
	}

}
