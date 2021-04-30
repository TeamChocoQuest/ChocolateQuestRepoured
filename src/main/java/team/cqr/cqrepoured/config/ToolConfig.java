package team.cqr.cqrepoured.config;

public class ToolConfig implements IToolConfig {

	public float damage;
	public float efficiency;
	public int enchantability;
	public int harvestLevel;
	public int maxUses;

	public ToolConfig(float damage, float efficiency, int enchantability, int harvestLevel, int maxUses) {
		this.damage = damage;
		this.efficiency = efficiency;
		this.enchantability = enchantability;
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
	}

	@Override
	public float getDamage() {
		return this.damage;
	}

	@Override
	public float getEfficiency() {
		return this.efficiency;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public int getHarvestLevel() {
		return this.harvestLevel;
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}
}
