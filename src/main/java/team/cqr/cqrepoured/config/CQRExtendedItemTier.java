package team.cqr.cqrepoured.config;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import team.cqr.cqrepoured.item.IExtendedItemTier;

public class CQRExtendedItemTier implements IItemTier, IExtendedItemTier {

	private final IItemTier tier;
	private final ExtendedItemTierConfig config;

	public CQRExtendedItemTier(IItemTier tier, ExtendedItemTierConfig config) {
		this.tier = tier;
		this.config = config;
	}

	@Override
	public int getUses() {
		return tier.getUses();
	}

	@Override
	public float getSpeed() {
		return tier.getSpeed();
	}

	@Override
	public float getAttackDamageBonus() {
		return tier.getAttackDamageBonus();
	}

	@Override
	public int getLevel() {
		return tier.getLevel();
	}

	@Override
	public int getEnchantmentValue() {
		return tier.getEnchantmentValue();
	}

	@Override
	public Ingredient getRepairIngredient() {
		return tier.getRepairIngredient();
	}

	@Override
	public float getFixedAttackDamageBonus() {
		return (float) (double) config.fixedAttackDamageBonus.get();
	}

	@Override
	public float getAttackSpeedBonus() {
		return (float) (double) config.attackSpeedBonus.get();
	}

	@Override
	public double getMovementSpeedBonus() {
		return config.movementSpeedBonus.get();
	}

}
