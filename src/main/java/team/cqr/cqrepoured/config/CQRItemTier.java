package team.cqr.cqrepoured.config;

import java.util.function.Supplier;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CQRItemTier implements Tier {

	private final ItemTierConfig config;
	private final Supplier<Ingredient> repairIngredient;

	public CQRItemTier(ItemTierConfig config, Supplier<Ingredient> repairIngredient) {
		this.config = config;
		this.repairIngredient = repairIngredient;
	}

	@Override
	public int getUses() {
		return config.uses.get();
	}

	@Override
	public float getSpeed() {
		return (float) (double) config.speed.get();
	}

	@Override
	public float getAttackDamageBonus() {
		return (float) (double) config.attackDamageBonus.get();
	}

	@Override
	public int getLevel() {
		return config.level.get();
	}

	@Override
	public int getEnchantmentValue() {
		return config.enchantmentValue.get();
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}

}
