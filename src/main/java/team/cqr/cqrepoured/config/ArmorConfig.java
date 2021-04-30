package team.cqr.cqrepoured.config;

public class ArmorConfig {

	public int durability;
	public int enchantability;
	public int[] reductionAmounts;
	public float toughness;

	public ArmorConfig(int durability, int enchantability, int[] reductionAmounts, float toughness) {
		this.durability = durability;
		this.enchantability = enchantability;
		this.reductionAmounts = reductionAmounts;
		this.toughness = toughness;
	}

}
