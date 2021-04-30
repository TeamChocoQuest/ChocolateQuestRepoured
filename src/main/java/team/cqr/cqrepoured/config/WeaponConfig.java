package team.cqr.cqrepoured.config;

public class WeaponConfig implements IToolConfig {

	public float damage;
	public int enchantability;
	public int maxUses;

	public WeaponConfig(float damage, int enchantability, int maxUses) {
		this.damage = damage;
		this.enchantability = enchantability;
		this.maxUses = maxUses;
	}

	@Override
	public float getDamage() {
		return this.damage;
	}

	@Override
	public float getEfficiency() {
		return 0.0F;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public int getHarvestLevel() {
		return 0;
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}
}
