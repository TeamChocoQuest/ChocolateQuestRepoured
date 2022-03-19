package team.cqr.cqrepoured.config;

import net.minecraft.inventory.EquipmentSlotType;

public class ArmorMaterialConfig
{
    public int durability;
    public int[] defense;
    public int enchantmentValue;
    public float toughness;
    public float knockbackResistance;

    public ArmorMaterialConfig(int durability, int[] defense, int enchantmentValue, float toughness, float knockbackResistance)
    {
        this.durability = durability;
        this.defense = defense;
        this.enchantmentValue = enchantmentValue;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    public int getDurabilityForSlot()
    {
        return this.durability;
    }

    public int[] getDefenseForSlot()
    {
        return this.defense;
    }

    public int getEnchantmentValue()
    {
        return this.enchantmentValue;
    }

    public float getToughness()
    {
        return this.toughness;
    }

    public float getKnockbackResistance()
    {
        return this.knockbackResistance;
    }
}