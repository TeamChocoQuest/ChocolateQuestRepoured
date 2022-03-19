package team.cqr.cqrepoured.config;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public class CQRItemTier implements IItemTier {

	public int uses;
	public float speed;
	public float attackDamageBonus;
	public int level;
	public int enchantmentValue;
	public LazyValue<Ingredient> repairIngredient;

	public CQRItemTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient)
	{
		this.uses = uses;
		this.speed = speed;
		this.attackDamageBonus = attackDamageBonus;
		this.level = level;
		this.enchantmentValue = enchantmentValue;
		this.repairIngredient = new LazyValue<>(repairIngredient);
	}

	@Override
	public int getUses()
	{
		return this.uses;
	}

	@Override
	public float getSpeed()
	{
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus()
	{
		return this.attackDamageBonus;
	}

	@Override
	public int getLevel()
	{
		return this.level;
	}

	@Override
	public int getEnchantmentValue()
	{
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return this.repairIngredient.get();
	}
}
