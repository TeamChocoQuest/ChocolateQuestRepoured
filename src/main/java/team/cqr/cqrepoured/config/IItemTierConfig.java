package team.cqr.cqrepoured.config;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public interface IItemTierConfig extends IItemTier {

	int getUses();

	float getSpeed();

	float getAttackDamageBonus();

	int getLevel();

	int getEnchantmentValue();

	Ingredient getRepairIngredient();

	//float getEfficiency();

	//int getEnchantability();

	//int getHarvestLevel();

}
