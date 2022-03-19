package team.cqr.cqrepoured.config;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class CQRArmorMaterial implements IArmorMaterial
{
	public String name;
	public int durability;
	public int[] defense;
	public int enchantmentValue;
	public SoundEvent equipSound;
	public LazyValue<Ingredient> repairIngredient;
	public float toughness;
	public float knockbackResistance;

	public CQRArmorMaterial(String name, int durability, int[] defense, int enchantmentValue, SoundEvent equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance)
	{
		this.name = name;
		this.durability = durability;
		this.defense = defense;
		this.enchantmentValue = enchantmentValue;
		this.equipSound = equipSound;
		this.repairIngredient = new LazyValue<>(repairIngredient);
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slot)
	{
		return this.durability;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slot)
	{
		return this.defense[slot.getIndex()];
	}

	@Override
	public int getEnchantmentValue()
	{
		return this.enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return this.repairIngredient.get();
	}

	@Override
	public float getToughness()
	{
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance()
	{
		return this.knockbackResistance;
	}
}
